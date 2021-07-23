package com.reactnativephotoeditor.helpers

import android.graphics.Color


class EditionUtils {
    companion object {
        fun convertToBoolean(stringVal: String): Boolean{
            return stringVal == "true"
        }

        fun formatColor(color: String?): String {
            return (color ?: "#FFF").let {
                if (it.length <= 4 && it.contains("#")) "#${it[1]}${it[1]}${it[2]}${it[2]}${it[3]}${it[3]}" else it
            }
        }

        fun parseColor(color: String?): Int {
            val newColor = formatColor(color)
            return Color.parseColor(newColor)
        }
    }
}
