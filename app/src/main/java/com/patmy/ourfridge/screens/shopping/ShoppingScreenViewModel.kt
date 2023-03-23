package com.patmy.ourfridge.screens.shopping

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MArticle
import com.patmy.ourfridge.model.MFHistory
import com.patmy.ourfridge.model.MFood
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ShoppingScreenViewModel : ViewModel() {

    private val userUId = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore
    private val adminId = UserAndFridgeData.fridge?.fridgeUsers?.get(0)?.fridge.toString()
    private val docRef = db.collection("shopping_lists").document(adminId)


    fun onAddArticle(articleToTransform: MFood, onDone: (List<MArticle?>) -> Unit) {
        val article =
            MArticle(
                id = UUID.randomUUID().toString(),
                title = articleToTransform.title,
                quantity = articleToTransform.quantity,
                unit = articleToTransform.unit,
                checked = false
            )

        val shoppingListUpdate =
            if (UserAndFridgeData.shoppingList?.shoppingList!![0]?.id == null || UserAndFridgeData.shoppingList?.shoppingList!![0]?.id == "null") {
                listOf(article)
            } else {
                UserAndFridgeData.shoppingList?.shoppingList?.plus(article)!!
            }



        docRef.update("shoppingList", shoppingListUpdate).addOnSuccessListener {
            UserAndFridgeData.shoppingList?.shoppingList = shoppingListUpdate
            onDone(shoppingListUpdate)
        }
    }

    fun changeCheck(changeTo: Boolean, article: MArticle?, onDone: () -> Unit) {


        val index = UserAndFridgeData.shoppingList?.shoppingList?.indexOfFirst { it == article }

        val shoppingListUpdate = UserAndFridgeData.shoppingList?.shoppingList

        shoppingListUpdate?.get(index!!)!!.checked = changeTo

        docRef.update("shoppingList", shoppingListUpdate).addOnSuccessListener {
            UserAndFridgeData.shoppingList?.shoppingList?.get(index!!)!!.checked = changeTo
            onDone()
        }
    }

    fun deleteArticle(article: MArticle?, onDone: () -> Unit) {

        val newList = UserAndFridgeData.shoppingList?.shoppingList?.filterNot { it == article }
            ?.toMutableList()

        if (newList?.size == 0) {
            newList += MArticle()
        }

        docRef.update("shoppingList", newList).addOnSuccessListener {
            UserAndFridgeData.shoppingList?.shoppingList = newList
            onDone()
        }
    }

    fun finishShopping(delete: Boolean, articleList: List<MArticle?>, onDone: () -> Unit) {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val currentDateTime = LocalDateTime.now().format(formatter)
        var articleListUpdate =
            if (delete) mutableListOf(MArticle()) else articleList.toMutableList()
        var fridgeAfterAdding = UserAndFridgeData.fridge?.foodInside
        var fridgeHistoryUpdate = UserAndFridgeData.fridge?.fridgeHistory
        val articlesAdded = mutableListOf<String>()


        for (article in articleList) {

            if (article?.checked == true) {
                val newFood = MFood(
                    id = article.id,
                    title = article.title,
                    quantity = article.quantity,
                    unit = article.unit,
                    date = currentDateTime,
                    nameOfCreator = UserAndFridgeData.user?.username,
                    idOfCreator = userUId
                )
                articlesAdded += article.title!!
                fridgeAfterAdding = fridgeAfterAdding!!.plus(newFood)
            }
        }

        if (!delete) {
            articleListUpdate = articleListUpdate.filterNot { it?.checked == true }.toMutableList()
        }

        fridgeAfterAdding!!.sortedBy { it?.title }

        val newEvent = MFHistory(
            historyId = UUID.randomUUID().toString(),
            creatorId = userUId,
            event = "${UserAndFridgeData.user?.username} added articles from shopping list: ${articlesAdded.joinToString { ", " }} $currentDateTime"
        )

        fridgeHistoryUpdate = fridgeHistoryUpdate?.plus(newEvent)

        db.collection("fridges").document(adminId)
            .update("foodInside", fridgeAfterAdding, "fridgeHistory", fridgeHistoryUpdate)
            .addOnSuccessListener {
                UserAndFridgeData.fridge?.foodInside = fridgeAfterAdding
                docRef.update("shoppingList", articleListUpdate).addOnSuccessListener {
                    UserAndFridgeData.shoppingList?.shoppingList = articleListUpdate
                    onDone()
                }
            }
    }
}
