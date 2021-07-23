package com.reactnativephotoeditor.types


enum class ImageScaleType(val type: String) {
    CONTAIN("FIT_CENTER"),
    COVER("MATRIX"),
    STRETCH("FIT_XY"),
    CENTER("CENTER_INSIDE"),
    REPEAT("CENTER_CROP")
}
