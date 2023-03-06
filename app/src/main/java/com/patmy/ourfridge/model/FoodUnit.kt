package com.patmy.ourfridge.model

enum class FoodUnit(val displayValue: String) {
    PIECE("pc"),
    GRAM("g"),
    KILOGRAM("kg"),
    LITER("l")
}

val radioUnits = FoodUnit.values()