package com.patmy.ourfridge.data

import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser

class UserAndFridgeDataTemplate {

    var user: MUser? = null
    var fridge: MFridge? = null
    var loggingOut: Boolean = false

    fun setData(updateUser: MUser? = user, updateFridge: MFridge? = fridge){
        user = updateUser
        fridge = updateFridge
    }

    fun clearData(){
        user = null
        fridge = null

    }
}

var UserAndFridgeData = UserAndFridgeDataTemplate()