package com.reactnativephotoeditor.observers.photourl

import android.net.Uri

interface PhotoUrlObserver {
    fun setImageFromUrl(url: Uri?): Unit
}
