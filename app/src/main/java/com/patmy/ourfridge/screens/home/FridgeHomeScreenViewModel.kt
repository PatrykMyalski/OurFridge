package com.patmy.ourfridge.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.model.MFHistory
import com.patmy.ourfridge.model.MFoodInside
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser

class FridgeHomeScreenViewModel : ViewModel() {

    private val userUId = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore

    fun getData(onDone: (MUser?, MFridge?) -> Unit) {
        db.collection("users").document(userUId).get().addOnSuccessListener { userData ->
            val currentUser = userData.toObject<MUser>()
            if (currentUser?.fridge !== null) {
                db.collection("fridges").document(currentUser.fridge.toString()).get()
                    .addOnSuccessListener { fridgeData ->
                        val currentFridge = fridgeData.toObject<MFridge>()
                        onDone(currentUser, currentFridge)
                    }.addOnFailureListener {
                    Log.d("FB",
                        "Exception occurs when tries to get fridge data: $it")
                }
            } else {
                val currentFridge = null
                onDone(currentUser, currentFridge)
            }
        }.addOnFailureListener { Log.d("FB", "Exception occurs when tries to get user data: $it") }
    }

    fun createFridge(
        currentUser: MUser?,
        onFridgeCreated: (newFridge: MFridge?, currentUser: MUser?) -> Unit,
    ) {

        currentUser?.fridge = userUId

        val emptyHistoryArray = listOf<MFHistory?>(MFHistory())
        val emptyFridgeFoodArray = listOf<MFoodInside?>(MFoodInside())
        val userList = listOf(currentUser)

        val newFridge = MFridge(userList, emptyFridgeFoodArray, emptyHistoryArray)

        db.collection("fridges").document(userUId).set(newFridge).addOnSuccessListener {
            db.collection("users").document(userUId).update("fridge", userUId)
                .addOnSuccessListener {
                    onFridgeCreated(newFridge, currentUser)
                }.addOnFailureListener {
                    Log.d("FB",
                        "Exception occurs when updating fridgeId in User data: $it")
                }

        }.addOnFailureListener {
            Log.d("FB", "Exception occur when creating new fridge: $it")
        }
    }
}

