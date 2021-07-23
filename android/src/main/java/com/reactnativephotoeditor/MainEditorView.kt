 package com.reactnativephotoeditor

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.reactnativephotoeditor.helpers.EditionUtils
import com.reactnativephotoeditor.helpers.ImageDownloader
import com.reactnativephotoeditor.helpers.ImagePicker
import com.reactnativephotoeditor.observers.Observables
import com.reactnativephotoeditor.observers.photourl.PhotoUrlObserver
import com.reactnativephotoeditor.types.*
import ja.burhanrashid52.photoeditor.*
import java.io.File
import java.net.MalformedURLException


class MainEditorView(context: ReactApplicationContext, activity: Activity? = null) : PhotoEditorView(context), View.OnClickListener {
    private var defaultImageSet = false
    private val activity = activity
    private var isListenerSet = false
    private val saveResultEvent = "SAVE_RESULT"
    private val editTextEvent = "EDIT_TEXT"
    private val textRobotoTypeFace = ResourcesCompat.getFont(context, R.font.roboto_medium)
    private val emojiTypeFace = Typeface.createFromAsset(context.assets, "emojione-android.ttf")
    private var currentTextEditView: View? = null
    var savePath: String? = null
        get() {
            return createFileDir(field ?: "edited-photo.png")
        }
    private val editorViewId = R.id.rnPhotoEditorId
    private var photoEditor: PhotoEditor? = null
        get() {
             val value = field
                    ?: PhotoEditor.Builder(context, this).setPinchTextScalable(true).setDefaultTextTypeface(textRobotoTypeFace).setDefaultEmojiTypeface(emojiTypeFace).build()
            if(!isListenerSet) {
                Log.d("setting-listener", "photo-editor listener");
                value.setOnPhotoEditorListener(photoEditorListener)
                isListenerSet = true
            }
            return value
        }

    private fun setImageWithUri(uri: Uri?) {
        source?.setImageURI(uri)
    }

    override fun requestLayout() {
        super.requestLayout();
        post(measureAndLayout)

    }

    private val measureAndLayout = Runnable {
        measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
        layout(left, top, right, bottom)
    }

    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri as Uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }
    private fun setDefaultImage() {
        source?.setImageResource(R.drawable.white_image)
    }

    private fun redrawUi() {
        invalidate()
    }

    private fun isWebUrl(urlString: String): Boolean {
        try {
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (ignored: MalformedURLException) {
        }
        return false
    }

    private val imageFromUrl = object : PhotoUrlObserver {
        override fun setImageFromUrl(url: Uri?) {
            setImageWithUri(url)
        }
    }

    private fun setEffect(effect: String) {
        photoEditor?.setFilterEffect(EffectsTypes.valueOf(effect.toUpperCase()).type)
    }

    private val photoEditorListener = object: OnPhotoEditorListener {
        override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
            currentTextEditView = rootView
            val data = Arguments.createMap()
            Log.d("text-edit", text)
            data.putString("currentText", text?.toString())
            data.putString("color", Integer.toHexString(colorCode))
            val event = Arguments.createMap()
            event.putMap("data", data)
            event.putString("type", editTextEvent)
            sendChangeEvent(event)
        }

        override fun onStartViewChangeListener(viewType: ViewType?) {
          Log.d("on-view", "listening is On StartView")
        }

        override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
          Log.d("on-view", "listening is On Remove")
        }

        override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
          Log.d("on-view", "listening is On AddView")
        }

        override fun onStopViewChangeListener(viewType: ViewType?) {
          Log.d("on-view", "listening is On StopView")
        }

    }
    private fun sendChangeEvent(arguments: WritableMap) {
        try {
            val con = context as ReactContext
            con.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "topChange", arguments)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createFileDir(filename: String): String? {
        val saveDir = context.getExternalFilesDir("photo-editor")
        if (saveDir != null) {
            if (!saveDir.exists()) {
            }
            saveDir.mkdir()
            val file = File(saveDir, filename)
            if (file.exists()) file.delete()
            return file.path
        }
        return null
    }





    fun saveResult() {
        if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        savePath?.let {
            photoEditor?.saveAsFile(it, object : PhotoEditor.OnSaveListener {
                override fun onSuccess(imagePath: String) {
                    val data = Arguments.createMap()
                    data.putString("path", imagePath)
                    val event = Arguments.createMap()
                    event.putMap("data", data)
                    event.putString("type", saveResultEvent)
                    sendChangeEvent(event)
                }

                override fun onFailure(exception: Exception) {
                    exception.printStackTrace()
                }

            })
        }
    }

    fun setEffectsOptions(options: ReadableMap) {
        val effect = options.getString("effect") ?: "NONE"
        setEffect(effect)
    }

    fun setImageByUri(sources: ReadableMap?) {
        if (sources != null) {
            val src = sources.getString("uri")
            if (src != null) {
                if (isWebUrl(src)) {
                    try {
                        ImageDownloader.downloadAsBitmap(src) { bitmap: Bitmap ->
                            Log.d("thread-log", bitmap.toString())
                            source?.setImageBitmap(bitmap)
                            redrawUi()
                        }
                    } catch (e: Throwable) {

                    }
                } else {
                    setImageWithUri(Uri.parse(src))
                }
            }
        }
    }

    fun setViewResizeMode(newMode: String) {
        source?.scaleType = ImageView.ScaleType.valueOf(ImageScaleType.valueOf(newMode.toUpperCase()).type)
    }

    fun setEditorBrushOptions(newOptions: DrawingOptions) {

        if (newOptions.enabled) {
            photoEditor?.brushSize = newOptions.brushSize
            photoEditor?.brushColor = Color.parseColor(newOptions.brushColor)
            photoEditor?.setOpacity(newOptions.colorOpacity)
            photoEditor?.setBrushDrawingMode(true)
        }

        if (!newOptions.enabled) {
            photoEditor?.setBrushDrawingMode(false)
        }

        if(newOptions.shape != null) {
          // photoEditor?.addShape(newOptions.shape)
        }

        if (newOptions.brushErases) {
            photoEditor?.brushEraser()
            photoEditor?.setBrushEraserSize(newOptions.eraserSize)
        }

        redrawUi()
    }

    fun resetEditor() {
        photoEditor?.clearAllViews()
        redrawUi()
    }

    fun resetEffects() {
        setEffect("NONE")
        redrawUi()
    }

    fun redo() {
        photoEditor?.redo()
        redrawUi()
    }

    fun undo() {
        photoEditor?.undo()
        redrawUi()
    }

    fun addPhoto() {
        ImagePicker(context as ReactApplicationContext,fun (url:Uri?,  _: WritableMap?): Unit {
            val imageBitmap = getBitmapFromUri(url)
            photoEditor?.addImage(imageBitmap)
            redrawUi()
        })
    }

    fun resetBrushes() {
        val brushOptions = object : DrawingOptions {
            override val enabled: Boolean
                get() = false
            override val brushSize: Float
                get() = 0f
            override val colorOpacity: Int
                get() = 0
            override val brushColor: String
                get() = ""
            override val brushErases: Boolean
                get() = false
            override val eraserSize: Float
                get() = 0f
          override val shape: DrawingShapes
            get() = DrawingShapes.BRUSH
        }

        setEditorBrushOptions(brushOptions)
    }

    fun addText(text: String?, color: String?) {
        val newColor = EditionUtils.parseColor(color)
        Log.d("text-adding:", text.toString())
        photoEditor?.addText(text, newColor)
        requestLayout()
    }

    fun editText(text: String?, color: String?) {
        photoEditor?.editText((currentTextEditView as View), text, EditionUtils.parseColor(color))
    }

    fun getImojies() {
        // val imojies = PhotoEditor.getImojies(activity)
    }

    init {
        gravity = Gravity.FILL_VERTICAL
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        id = editorViewId
        Observables.photoUrl.add(imageFromUrl)
        setDefaultImage()
    }

    override fun onClick(v: View) {
        val text = (v as Button).text.toString()
        Log.d("clicked-response ", text)
        if (text.toLowerCase().contains(saveButtonText.toLowerCase())) {
            saveResult()
        } else if (text.toLowerCase().contains(resetButtonText.toLowerCase())) {
            resetEditor()
        }
    }

    companion object {
        private const val saveButtonText = "Save"
        private const val resetButtonText = "Reset"
    }
}
