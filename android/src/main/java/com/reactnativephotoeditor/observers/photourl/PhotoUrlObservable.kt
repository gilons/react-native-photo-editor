package com.reactnativephotoeditor.observers.photourl

import android.net.Uri


class PhotoUrlObservable : PhotoUrlObservableInterface {
    override var observedData: Uri? = null
        set(value) {
            sendUpdate(value)
            field = value
        }

    override val observers = ArrayList<PhotoUrlObserver>()
}
