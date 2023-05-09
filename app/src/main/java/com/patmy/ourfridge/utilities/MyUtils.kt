package com.patmy.ourfridge.utilities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyUtils {
    companion object {
        fun getCurrentTime(): String? {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            return LocalDateTime.now().format(formatter)
        }

        fun checkIfMoreThanThreeDecimals(value: String) : Boolean{
            val transformedToFloat = value.toFloat().toString()
            val dotIndex = transformedToFloat.indexOf('.')
            return transformedToFloat.length - dotIndex >= 4
        }

    }
}