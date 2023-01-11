package com.patmy.ourfridge.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val loading = MutableLiveData(false)

    fun signIn(email: String, password: String, toHome: () -> Unit){
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        loading.value = true
                        if (task.isSuccessful){
                            Log.d("Login", "signIn Successful: ${task.result.toString()}")
                            loading.value = false
                            toHome()
                        } else {
                            Log.d("Login", "signIn unsuccessful: ${task.result.toString()}")
                        }

                    }
            } catch (e: Exception) {
                Log.d("FB", "signIn error occur: ${e.message.toString()}")
            }
        }
    }
}