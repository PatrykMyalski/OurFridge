package com.patmy.ourfridge.model

data class MFridge(
    var fridgeUsers: List<MUser>,
    var foodInside: List<MFoodInside>,
    var fridgeHistory: List<MFHistory>?,
)
