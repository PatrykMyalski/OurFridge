package com.patmy.ourfridge.model

data class MFHistory(
    var historyId: String? = null,
    var foodId: String? = null,
    var foodItem: MFood? = null,
    var fridgeId: String? = null,
    var creatorId: String? = null,
    var event: String? = null,
)

