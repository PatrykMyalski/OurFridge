package com.patmy.ourfridge.model

data class MFridge(
    var fridgeUsers: List<MUser?> = listOf(),
    var foodInside: List<MFoodInside?> = listOf(),
    var fridgeHistory: List<MFHistory?> = listOf(),
)
