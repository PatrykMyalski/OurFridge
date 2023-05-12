package com.patmy.ourfridge.components.sideBar

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MArticle
import com.patmy.ourfridge.model.MFHistory
import com.patmy.ourfridge.model.MFood
import com.patmy.ourfridge.utilities.MyUtils
import java.util.*

class SideBarViewModel : ViewModel() {

    private val userUId = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore
    private val fridgeRef = db.collection("fridges")
    private val userRef = db.collection("users")


    /**
     * This function allows a user to leave the current fridge they are in.
     * It updates the fridge's user list by removing the current user and reassigning the admin role if necessary.
     * If the current user is the only user in the fridge, the fridge will be deleted.
     *
     * @param onDone a lambda function that will be called if the operation is successful.
     * @param onFailure a lambda function that will be called if the operation fails.
     */
    fun leaveFridge(onDone: () -> Unit, onFailure: () -> Unit) {

        if (UserAndFridgeData.user?.fridge == null){
            onFailure()
        } else {
            val newUserList =
                UserAndFridgeData.fridge?.fridgeUsers!!.filterNot { it == UserAndFridgeData.user }
            val updatedUser = UserAndFridgeData.user!!.copy()
            updatedUser.role = null
            updatedUser.fridge = null

            // leaving fridge when user is admin of the fridge, passing admin role to next user

            if (UserAndFridgeData.user?.role == "admin" && newUserList.isNotEmpty()) {
                newUserList[0]?.role = "admin"
                fridgeRef.document(UserAndFridgeData.user!!.fridge.toString())
                    .update("fridgeUsers", newUserList).addOnSuccessListener {
                        userRef.document(userUId).set(updatedUser).addOnSuccessListener {
                            userRef.whereEqualTo("email", newUserList[0]?.email).get()
                                .addOnSuccessListener {
                                    userRef.document(it.documents[0].id).update("role", "admin")
                                        .addOnSuccessListener {
                                            UserAndFridgeData.clearData()
                                            UserAndFridgeData.user = updatedUser
                                            onDone()
                                        }.addOnFailureListener {
                                            onFailure()
                                        }
                                }.addOnFailureListener {
                                    onFailure()
                                }
                        }.addOnFailureListener {
                            onFailure()
                        }
                    }.addOnFailureListener {
                        onFailure()
                    }
            } else if (newUserList.isEmpty()) {
                // deleting fridge when user is the only one using fridge and want to leave
                fridgeRef.document(UserAndFridgeData.user!!.fridge.toString()).delete()
                    .addOnSuccessListener {
                        userRef.document(userUId).set(updatedUser).addOnSuccessListener {
                            db.collection("shopping_lists")
                                .document(UserAndFridgeData.user!!.fridge.toString()).delete()
                                .addOnSuccessListener {
                                    UserAndFridgeData.clearData()
                                    UserAndFridgeData.user = updatedUser
                                    onDone()
                                }.addOnFailureListener {
                                    onFailure()
                                }
                        }
                    }
            } else {
                // ordinary case when normal user want to leave fridge
                fridgeRef.document(UserAndFridgeData.user!!.fridge.toString())
                    .update("fridgeUsers", newUserList).addOnSuccessListener {
                        userRef.document(userUId).set(updatedUser).addOnSuccessListener {
                            UserAndFridgeData.clearData()
                            UserAndFridgeData.user = updatedUser
                            onDone()
                        }.addOnFailureListener {
                            onFailure()
                        }
                    }.addOnFailureListener {
                        onFailure()
                    }
            }
        }


    }

    /**
     * Clears the fridge history and adds a new event with the current time indicating that the user
     * has cleared the history. The updated history is stored in the Firestore database and also
     * updated in the local `UserAndFridgeData` object.
     *
     * @param onDone A function that will be called when the operation is successful.
     * @param onFailure A function that will be called if the operation fails.
     */
    fun clearHistory(onDone: () -> Unit, onFailure: () -> Unit) {

        val currentDateTime = MyUtils.getCurrentTime()
        val newHistoryList = listOf<MFHistory?>(
            MFHistory(
                historyId = UUID.randomUUID().toString(),
                creatorId = userUId,
                event = "${UserAndFridgeData.user!!.username} cleared fridge history $currentDateTime."
            )
        )

        fridgeRef.document(UserAndFridgeData.user!!.fridge.toString())
            .update("fridgeHistory", newHistoryList).addOnSuccessListener {
                UserAndFridgeData.fridge!!.fridgeHistory = newHistoryList
                onDone()
            }.addOnFailureListener {
                onFailure()
            }
    }

    // Deleting all food from fridge, and adding event on it to history
    fun deleteFood(onDone: () -> Unit, onFailure: () -> Unit) {

        val currentDateTime = MyUtils.getCurrentTime()

        val newFoodList = listOf(MFood())
        val newEvent = MFHistory(
            historyId = UUID.randomUUID().toString(),
            creatorId = userUId,
            event = "${UserAndFridgeData.user!!.username} deleted all food from fridge $currentDateTime."
        )

        val historyListToUpdate = UserAndFridgeData.fridge?.fridgeHistory?.plus(newEvent)

        fridgeRef.document(UserAndFridgeData.user!!.fridge.toString())
            .update("foodInside", newFoodList, "fridgeHistory", historyListToUpdate).addOnSuccessListener {
                UserAndFridgeData.fridge?.fridgeHistory?.plus(newEvent)
                UserAndFridgeData.fridge?.foodInside = newFoodList
                onDone()
            }.addOnFailureListener {
                onFailure()
            }
    }

    // Deleting all shopping articles added to the shopping list
    fun clearShopping(onDone: () -> Unit, onFailure: () -> Unit) {

        val newShoppingList = listOf(MArticle())

        db.collection("shopping_lists").document(UserAndFridgeData.user!!.fridge.toString())
            .update("shoppingList", newShoppingList).addOnSuccessListener {
                UserAndFridgeData.shoppingList?.shoppingList = newShoppingList
                onDone()
            }.addOnFailureListener {
                onFailure()
            }
    }

    //Lets user deletes account and all user data
    fun deleteAccount(onDone: () -> Unit, onFailure: () -> Unit){

        fun deletingAccountFromFirebase(){
            userRef.document(userUId).delete()
            Firebase.auth.currentUser?.delete()
            UserAndFridgeData.clearData()
        }

        if (UserAndFridgeData.user?.fridge !== null){
            leaveFridge(onDone = {
                deletingAccountFromFirebase()
                onDone()
            }, onFailure = { onFailure() })
        } else {
            deletingAccountFromFirebase()
            onDone()
        }

    }
}