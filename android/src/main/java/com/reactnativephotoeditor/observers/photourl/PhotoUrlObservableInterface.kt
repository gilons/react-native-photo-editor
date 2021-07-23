package com.reactnativephotoeditor.observers.photourl

import android.net.Uri
import com.reactnativephotoeditor.observers.Observable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface PhotoUrlObservableInterface: Observable<PhotoUrlObserver, Uri> {
    override val observedData: Uri?
    override fun sendUpdate(param: Uri?) {
        try {
            GlobalScope.launch {
                (observers).asFlow().collect {
                    it?.setImageFromUrl(param)
                }
            }
        } catch (e: Throwable) {

        }
    }
}
