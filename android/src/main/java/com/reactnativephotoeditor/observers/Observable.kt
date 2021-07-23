package com.reactnativephotoeditor.observers

interface Observable<T, ObservedData> {
    val observers: ArrayList<T>
    val observedData: ObservedData?
    fun add(observer: T) {
        observers.add(observer)
    }

    fun remove(observer: T) {
        observers.remove(observer)
    }
    fun sendUpdate(param: ObservedData?)
}
