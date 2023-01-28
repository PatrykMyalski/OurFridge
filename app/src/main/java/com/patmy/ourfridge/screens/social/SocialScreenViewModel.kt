package com.patmy.ourfridge.screens.social

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser

class SocialScreenViewModel : ViewModel() {

    private val userUId = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore

    fun joinFridge(fridgeId: String, joinedToFridge: (MUser?, MFridge?) -> Unit, fridgeNotFound: () -> Unit) {

        val fridgeRef = db.collection("fridges")
        fridgeRef.whereEqualTo("id", fridgeId).get().addOnSuccessListener { fridgeArr ->

            val fridge = fridgeArr.toObjects<MFridge>()
            if (fridge.isNotEmpty()) {
                db.collection("users").document(userUId).get().addOnSuccessListener { userData ->
                    val currentUser = userData.toObject<MUser>()
                    val fridgeUsersToUpdate = fridge[0].fridgeUsers + currentUser    // adding user to list of fridge users
                    val fridgeUId = fridge[0].fridgeUsers[0]?.fridge.toString()     // getting fridge id from fridge creator
                    currentUser?.fridge = fridgeUId
                    db.collection("users").document(userUId).update("fridge", fridgeUId)
                        .addOnSuccessListener {
                            fridgeRef.document(fridgeUId).update("fridgeUsers", fridgeUsersToUpdate).addOnSuccessListener {
                                joinedToFridge(currentUser, fridge[0])
                            }.addOnFailureListener {
                                Log.d("FB", "Exception occurs when updating fridge users data: $it")
                            }
                        }.addOnFailureListener {
                        Log.d("FB", "Exception occur when updating user data: $it ")
                    }
                }.addOnFailureListener {
                    Log.d("FB", "Exception occurs when getting user data: $it")
                }
            } else {
                fridgeNotFound()
            }
        }.addOnFailureListener {
            Log.d("FB", "Exception occurs when getting fridge data: $it")
        }
    }
}