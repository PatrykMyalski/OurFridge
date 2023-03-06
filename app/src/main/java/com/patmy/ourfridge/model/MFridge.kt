package com.patmy.ourfridge.model

data class MFridge(
    var id: String? = null,
    var fridgeUsers: List<MUser?> = listOf(),
    var foodInside: List<MFood?> = listOf(),
    var fridgeHistory: List<MFHistory?> = listOf(),
)
