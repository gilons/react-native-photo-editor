package com.reactnativephotoeditor


import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.reactnativephotoeditor.helpers.EditionUtils
import com.reactnativephotoeditor.types.DrawingOptions


class PEditorViewManager(context: ReactApplicationContext? = null) : ViewGroupManager<MainEditorView>() {
    private val mainEditorView = context?.let { MainEditorView(it) }
    private val SAVE_RESULTS = 1
    private val RESET_EDITOR = 2
    private val RESET_EFFECTS = 3
    private val RESET_BRUSHES = 4
    private val UNDO = 5
    private val REDO = 6
    private val ADD_PHOTO = 7
    private val ADD_TEXT = 8
    private val EDIT_TEXT = 9
    private val GET_IMOJIES = 10
    override fun getName(): String {
        return "PhotoEditorView"
    }

    @ReactProp(name = "src")
    fun setUrl(@Suppress("UNUSED_PARAMETER") view: MainEditorView, sources: ReadableMap) {
        mainEditorView?.setImageByUri(sources)
    }


    @ReactProp(name = "resizeMode")
    fun setResizeMode(@Suppress("UNUSED_PARAMETER") view: MainEditorView, newMode: String? = null) {
        if (newMode != null)
            mainEditorView?.setViewResizeMode(newMode)
        else mainEditorView?.setViewResizeMode("contain")
    }

    @ReactProp(name = "savePath")
    fun setSavePath(@Suppress() view: MainEditorView, newPath: String) {
        view.savePath = newPath
    }

    @ReactProp(name = "drawingOptions")
    fun setBrushOptions(@Suppress("UNUSED_PARAMETER") view: MainEditorView, brushOptions: ReadableMap) {
        val newOptions = object :  DrawingOptions {
            override val enabled = brushOptions.getBoolean("enabled")
            override val brushSize = brushOptions.getDouble("size").toFloat()
            override val colorOpacity = brushOptions.getInt("opacity")
            override val brushColor = EditionUtils.formatColor(brushOptions.getString("color"))
            override val brushErases = brushOptions.getBoolean("erases")
            override val eraserSize = brushOptions.getDouble("eraserSize").toFloat()
        }
        mainEditorView?.setEditorBrushOptions(newOptions)
    }

    @ReactProp(name = "effectOptions")
    fun setFilterOptions(@Suppress("UNUSED_PARAMETER") viw: MainEditorView, filterOptions: ReadableMap) {
        mainEditorView?.setEffectsOptions(filterOptions)
    }


    override fun getCommandsMap(): MutableMap<String, Int> {
        val map = MapBuilder.of(
                "ADD_PHOTO", ADD_PHOTO,
                "SAVE_RESULT", SAVE_RESULTS,
                "RESET_EDITOR", RESET_EDITOR,
                "RESET_EFFECTS", RESET_EFFECTS,
                "RESET_BRUSHES", RESET_BRUSHES,
                "UNDO", UNDO,
                "REDO", REDO

        ) // this initialization method only supports 7 initial values

        map["ADD_TEXT"] = ADD_TEXT
        map["EDIT_TEXT"] = EDIT_TEXT
        map["GET_IMOJIES"] = GET_IMOJIES
        return map
    }

    override fun receiveCommand(root: MainEditorView, commandId: Int, args: ReadableArray?) {
        Log.d("command-received: ", commandId.toString())
        when (commandId) {
            ADD_PHOTO -> {
                root.addPhoto()
            }
            SAVE_RESULTS -> {
                root.saveResult()
            }
            RESET_EDITOR -> {
                root.resetEditor()
            }
            RESET_EFFECTS -> {
                root.resetEffects()
            }
            RESET_BRUSHES -> {
                root.resetBrushes()
            }
            UNDO -> {
                root.redo()
            }
            REDO -> {
                root.undo()
            }
            ADD_TEXT -> {
                root.addText(args?.getString(0), args?.getString(1))
            }
            EDIT_TEXT -> {
                root.editText(args?.getString(0), args?.getString(1))
            }
            GET_IMOJIES -> {
                root.getImojies()
            }
        }
    }

    override fun createViewInstance(reactContext: ThemedReactContext): MainEditorView {

        return mainEditorView as MainEditorView
    }
}
