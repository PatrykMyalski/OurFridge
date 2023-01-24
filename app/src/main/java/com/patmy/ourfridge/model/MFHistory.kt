package com.patmy.ourfridge.model

data class MFHistory(
    var historyId: String,
    var foodId: String,
    var fridgeId: String,
    var creatorId: String,
    var event: String,
)
