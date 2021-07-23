package com.reactnativephotoeditor.helpers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import java.lang.Exception

class ImagePicker(context: ReactApplicationContext, onDone: (Uri?, WritableMap?) -> Unit) {
    private val IMAGE_PICKER_REQUEST = 1
    private val E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST"
    private val E_PICKER_CANCELLED = "E_PICKER_CANCELLED"
    private val E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER"
    private val onDoneFunc = onDone
    private val E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND"

    private fun buildErrorMap(errorType: String, message: String): WritableMap {
        val newError = Arguments.createMap()
        newError.putString(errorType, message)
        return newError
    }

    private val activityEventListener = object : ActivityEventListener {
        override fun onNewIntent(intent: Intent?) {
            TODO("Not yet implemented")
        }

        override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
            performResultAction(activity, requestCode, resultCode, data)
        }

        private fun performResultAction(activity: Activity?, requestCode: Int?, resultCode: Int?, intent: Intent?) {
            Log.d("intent-result ", requestCode.toString())
            if (requestCode == IMAGE_PICKER_REQUEST) {
                Log.d("intent-result: ", resultCode.toString())
                if (resultCode == Activity.RESULT_CANCELED) {
                    onDoneFunc(null, buildErrorMap(E_PICKER_CANCELLED, "Image picker was cancelled"))
                } else if (resultCode == Activity.RESULT_OK) {
                    val uri = intent?.data;
                    if (uri == null) {
                        onDoneFunc(null, buildErrorMap(E_NO_IMAGE_DATA_FOUND, "No image data found"))
                    } else {
                        Log.d("image-picker: ", "url is $uri")
                        onDoneFunc(uri, null)
                    }
                }

            }
        }

    }

    init {
        context.addActivityEventListener(activityEventListener)
    }

    fun pickImage(currentActivity: Activity? = null): Unit {
        if (currentActivity == null) {
            onDoneFunc(null, buildErrorMap(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist"))
            return;
        }
        val galleryIntent = Intent(Intent.ACTION_PICK);
        galleryIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(galleryIntent, "Select an Image");
        try {
            GlobalScope.launch {
                launch() {
                    currentActivity.startActivityForResult(chooserIntent, IMAGE_PICKER_REQUEST);
                }
            }
        } catch (e: Exception) {
            Log.d("picker-exception: ", e.toString())
            onDoneFunc(null, buildErrorMap(E_FAILED_TO_SHOW_PICKER, e.toString()));
        }
    }

}
