package com.patmy.ourfridge.screens.registration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.model.MUser
import kotlinx.coroutines.launch

class RegistrationScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val loading = MutableLiveData(false)

    fun signUp(
        email: String,
        password: String,
        username: String,
        toHome: () -> Unit,
        emailAlreadyAtUse: () -> Unit,
        changeLoadingValue: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                changeLoadingValue()
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        loading.value = true
                        if (task.isSuccessful) {
                            Log.d("FB", "signUp Successful: ${task.result}")
                            val userUId = Firebase.auth.currentUser?.uid.toString()
                            val user = MUser(email, username)

                            db.collection("users").document(userUId).set(user)
                                .addOnSuccessListener {
                                    Log.d("FB", "User successfully created in firestore")
                                }.addOnFailureListener {
                                    Log.d(
                                        "FB", "Exception occurs when setting user in firestore: $it"
                                    )
                                }
                            changeLoadingValue()
                            toHome()
                        } else {
                            Log.d("FB", "signUp unsuccessful: ${task.exception}")
                            changeLoadingValue()
                            emailAlreadyAtUse()
                        }
                    }
            } catch (e: Exception) {
                println("signUP error occur: ${e.message.toString()}")
                changeLoadingValue()
            }
        }
    }
}