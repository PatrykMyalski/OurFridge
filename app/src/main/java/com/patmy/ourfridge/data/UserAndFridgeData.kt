package com.patmy.ourfridge.data

import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MShoppingList
import com.patmy.ourfridge.model.MUser

class UserAndFridgeDataTemplate {

    var user: MUser? = null
    var fridge: MFridge? = null
    var shoppingList: MShoppingList? = null
    var loggingOut: Boolean = false

    fun setData(updateUser: MUser? = user, updateFridge: MFridge? = fridge, updateShoppingList: MShoppingList? = shoppingList){
        user = updateUser
        fridge = updateFridge
        shoppingList = updateShoppingList
    }

    fun clearData(){
        user = null
        fridge = null
        shoppingList = null


    }
}

var UserAndFridgeData = UserAndFridgeDataTemplate()