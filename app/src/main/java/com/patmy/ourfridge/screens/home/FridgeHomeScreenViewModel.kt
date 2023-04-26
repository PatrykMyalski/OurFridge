package com.patmy.ourfridge.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
                        db.collection("shopping_lists")
                            .document(currentFridge!!.fridgeUsers[0]?.fridge.toString()).get()
                            .addOnSuccessListener { shoppingListData ->
                                val currentShoppingList = shoppingListData.toObject<MShoppingList>()
                                UserAndFridgeData.shoppingList = currentShoppingList
                                onDone(currentUser, currentFridge)
                            }

                    }.addOnFailureListener {
                        Log.d(
                            "FB", "Exception occurs when tries to get fridge data: $it"
                        )
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
        currentUser?.role = "admin"

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val currentDateTime = LocalDateTime.now().format(formatter)

        val historyArray = listOf<MFHistory?>(
            MFHistory(
                historyId = UUID.randomUUID().toString(),
                creatorId = userUId,
                event = "Fridge created $currentDateTime"
            )
        )
        val emptyFridgeFoodArray = listOf<MFood?>(MFood())
        val userList = listOf(currentUser)
        val fridgeId = userUId.substring(userUId.length - 6, userUId.length)

        val newFridge = MFridge(fridgeId, userList, emptyFridgeFoodArray, historyArray)

        val newShoppingList = MShoppingList(listOf<MArticle?>(MArticle()))

        db.collection("fridges").document(userUId).set(newFridge).addOnSuccessListener {
            db.collection("users").document(userUId)
                .update("fridge", userUId, "role", currentUser?.role).addOnSuccessListener {
                    db.collection("shopping_lists").document(userUId).set(newShoppingList)
                        .addOnSuccessListener {
                            onFridgeCreated(newFridge, currentUser)
                            UserAndFridgeData.shoppingList = newShoppingList
                        }
                }.addOnFailureListener {
                    Log.d(
                        "FB", "Exception occurs when updating fridgeId in User data: $it"
                    )
                }

        }.addOnFailureListener {
            Log.d("FB", "Exception occur when creating new fridge: $it")
        }
    }

    fun addFoodToFridge(
        newFood: MFood,
        currentUser: MUser?,
        fridge: MFridge,
        onFoodAdded: (MFridge) -> Unit,
    ) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val currentDateTime = LocalDateTime.now().format(formatter)

        newFood.idOfCreator = userUId
        newFood.nameOfCreator = currentUser?.username
        newFood.date = currentDateTime
        newFood.id = UUID.randomUUID().toString()

        if (fridge.foodInside[0]?.id == null) {
            fridge.foodInside = listOf(newFood)
        } else {
            fridge.foodInside += newFood
            fridge.foodInside = fridge.foodInside.sortedBy { it?.title }
        }

        val newEvent = MFHistory(
            historyId = UUID.randomUUID().toString(),
            creatorId = userUId,
            foodId = newFood.id,
            event = "${currentUser?.username} added ${newFood.title} to fridge. $currentDateTime"
        )

        fridge.fridgeHistory += newEvent

        db.collection("fridges").document(currentUser?.fridge.toString())
            .update("foodInside", fridge.foodInside, "fridgeHistory", fridge.fridgeHistory)
            .addOnSuccessListener {
                onFoodAdded(fridge)
            }.addOnFailureListener {
                Log.d(
                    "FB", "Exception occurs during adding food to fridge: $it"
                )
            }
    }

    fun deleteFood(food: MFood?, onDone: () -> Unit) {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val currentDateTime = LocalDateTime.now().format(formatter)

        val newArray =
            UserAndFridgeData.fridge?.foodInside?.filterNot { it?.id == food?.id }?.toMutableList()
        if (newArray?.size == 0) {
            newArray += MFood()
        }

        val historyEvent = MFHistory(
            historyId = UUID.randomUUID().toString(),
            foodId = food?.id,
            creatorId = userUId,
            event = "${UserAndFridgeData.user?.username} took out all of ${food?.title} $currentDateTime"
        )

        val historyUpdate = UserAndFridgeData.fridge?.fridgeHistory?.plus(historyEvent)

        val fridgeUId = UserAndFridgeData.fridge!!.fridgeUsers[0]?.fridge!!

        db.collection("fridges").document(fridgeUId)
            .update("foodInside", newArray, "fridgeHistory", historyUpdate).addOnSuccessListener {
                val updateFridge = UserAndFridgeData.fridge
                updateFridge?.fridgeHistory = historyUpdate!!
                updateFridge?.foodInside = newArray!!
                UserAndFridgeData.setData(updateFridge = updateFridge)
                onDone()
            }
    }

    fun changeFoodQuantity(action: String, food: MFood?, quantity: String, onDone: () -> Unit) {

        val fridgeUId = UserAndFridgeData.fridge!!.fridgeUsers[0]?.fridge!!

        val index = UserAndFridgeData.fridge?.foodInside?.indexOfFirst { it == food }

        val newQuantity = if (action == "-") food?.quantity!!.toInt() - quantity.toInt()
        else food?.quantity!!.toInt() + quantity.toInt()

        UserAndFridgeData.fridge?.foodInside!![index!!]?.quantity = newQuantity.toString()


        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val currentDateTime = LocalDateTime.now().format(formatter)

        val historyEvent = MFHistory(
            historyId = UUID.randomUUID().toString(),
            foodId = food.id,
            creatorId = userUId,
            event = "${UserAndFridgeData.user?.username} ${
                if (action == "-") "took out"
                else "added"
            } $quantity ${food.unit} of ${food.title} $currentDateTime"
        )

        UserAndFridgeData.fridge?.fridgeHistory?.plus(historyEvent)

        db.collection("fridges").document(fridgeUId).update(
                "foodInside",
                UserAndFridgeData.fridge?.foodInside,
                "fridgeHistory",
                UserAndFridgeData.fridge?.fridgeHistory
            ).addOnSuccessListener {
                onDone()
            }
    }
}





