package com.reactnativephotoeditor.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class   ImageDownloader() {
    companion object {
        fun downloadAsBitmap(url: String, feedPoster: (feed: Bitmap) -> Unit): Unit {
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    val connection = URL(url).openConnection()
                    connection.doInput = true
                    connection.connect()
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    launch(Dispatchers.Main) {
                        feedPoster(bitmap)
                    }
                }
            } catch (e: Throwable) {
                throw e
            } finally {
                Log.d("request-completion", "request completed")
            }
        }
    }
}

