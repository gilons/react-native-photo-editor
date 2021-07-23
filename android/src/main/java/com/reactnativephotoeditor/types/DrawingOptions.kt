package com.reactnativephotoeditor.types

interface DrawingOptions {
    // enable disable brush
    val enabled: Boolean
    // brush sized
    val brushSize: Float
    // color opacity in percentage
    val colorOpacity: Int
    // brush color in color code string format
    val brushColor: String
    // brush erasing boolean specifying if erasing or not
    val brushErases: Boolean
    val eraserSize: Float
    val shape: DrawingShapes?
      get() = null
}
