package com.patmy.ourfridge.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth



    fun signIn(email: String, password: String, toHome: () -> Unit, userNotFound: () -> Unit, changeLoadingValue: () -> Unit) {
        viewModelScope.launch {
            try {
                changeLoadingValue()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "signIn Successful: ${task.result}")
                            changeLoadingValue()
                            toHome()
                        } else {
                            Log.d("FB", "signIn unsuccessful: ${task.exception}")
                            changeLoadingValue()
                            userNotFound()
                        }
                    }.addOnFailureListener {
                        println(it)
                        changeLoadingValue()
                    }
            } catch (e: Exception) {
                println("signIn error occur: ${e.message.toString()}")
                changeLoadingValue()
            }
        }
    }
}