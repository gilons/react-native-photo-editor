package com.reactnativephotoeditor

import android.app.Activity
import android.net.Uri
import com.facebook.react.bridge.*
import com.reactnativephotoeditor.helpers.ImagePicker
import com.reactnativephotoeditor.observers.Observables

class PEditorModule(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {

    private val appContext = context
    override fun getName(): String {
        return "PEditorModule"
    }


    @ReactMethod
    fun selectImage(promise: Promise) {
        val picker = ImagePicker(appContext, fun(url: Uri?, error: WritableMap?): Unit {
            if (url != null) {
                Observables.photoUrl.observedData = url
                promise.resolve(url.toString())
            } else promise.reject(Throwable(message = error.toString()))
        })

        picker.pickImage(currentActivity as Activity)
    }

}
