package com.patmy.ourfridge.screens.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.model.MFHistory
import com.patmy.ourfridge.model.MFoodInside
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser

class FridgeHomeScreenViewModel : ViewModel() {

    private val database = Firebase.database
    private val myRef =
        database.getReferenceFromUrl("https://ourfridge-efd59-default-rtdb.europe-west1.firebasedatabase.app/")
    private val userUId = Firebase.auth.currentUser?.uid.toString()

    @Composable
    fun GetData(onDone: (MUser) -> Unit) {
        val emailState = remember {
            mutableStateOf("")
        }
        val usernameState = remember {
            mutableStateOf("")
        }
        val fridgeState = remember {
            mutableStateOf("")
        }
        myRef.child("users").child(userUId).child("email").get().addOnSuccessListener {
            emailState.value = it.value.toString()
        }.addOnFailureListener {
            Log.d("FB", "Exception during email request: $it")
        }
        myRef.child("users").child(userUId).child("username").get().addOnSuccessListener {
            usernameState.value = it.value.toString()
        }.addOnFailureListener {
            Log.d("FB", "Exception during username request: $it")
        }
        myRef.child("users").child(userUId).child("fridge").get().addOnSuccessListener {
            fridgeState.value = it.value.toString()
        }.addOnFailureListener {
            Log.d("FB", "Exception during fridgeId request: $it")
        }
        onDone(MUser(emailState.value, usernameState.value, fridgeState.value))
    }

    fun createFridge(creator: MUser) {

        creator.fridge = userUId

        val emptyMFHistory =
            MFHistory(historyId = "0", foodId = "0", fridgeId = "0", creatorId = "0", event = "0")

        val emptyMFoodInside = MFoodInside(id = "0",
            title = "0",
            quantity = "0",
            unit = "0",
            date = "0",
            idOfCreator = "0")

        val newFridge = MFridge(fridgeUsers = listOf(creator),
            foodInside = listOf(emptyMFoodInside),
            fridgeHistory = listOf(emptyMFHistory))

        myRef.child("fridges").child(userUId).setValue(newFridge).addOnSuccessListener {
            myRef.child("users").child(userUId).child("fridge").setValue(userUId)
                .addOnSuccessListener {
                    println("succes")
                }
        }
    }
}