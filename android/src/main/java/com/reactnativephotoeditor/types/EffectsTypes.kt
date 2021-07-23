package com.reactnativephotoeditor.types

import ja.burhanrashid52.photoeditor.PhotoFilter

enum class EffectsTypes(val type: PhotoFilter) {
    BRIGHTNESS( PhotoFilter.BRIGHTNESS),
    BLACKWHITE(PhotoFilter.BLACK_WHITE),
    AUTOFIX(PhotoFilter.AUTO_FIX),
    CONTRAST(PhotoFilter.CONTRAST),
    CROSSPROCESS(PhotoFilter.CROSS_PROCESS),
    DOCUMENTARY(PhotoFilter.DOCUMENTARY),
    DUOTONE(PhotoFilter.DUE_TONE),
    FILLLIGHT(PhotoFilter.FILL_LIGHT),
    FISHEYE(PhotoFilter.FISH_EYE),
    GRAIN(PhotoFilter.GRAIN),
    GRAYSCALE(PhotoFilter.GRAY_SCALE),
    VIGNETTE(PhotoFilter.VIGNETTE),
    POSTERIZE(PhotoFilter.POSTERIZE),
    SATURATE(PhotoFilter.SATURATE),
    SEPIA(PhotoFilter.SEPIA),
    SHARPEN(PhotoFilter.SHARPEN),
    TEMPERATURE(PhotoFilter.TEMPERATURE),
    TINT(PhotoFilter.TINT),
    FLIPVERTICAL(PhotoFilter.FLIP_VERTICAL),
    FLIPHORIZONTAL(PhotoFilter.FLIP_HORIZONTAL),
    NONE(PhotoFilter.NONE),


}
