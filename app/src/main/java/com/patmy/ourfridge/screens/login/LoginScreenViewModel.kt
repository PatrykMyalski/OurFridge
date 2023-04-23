package com.patmy.ourfridge.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MUser
import com.patmy.ourfridge.screens.login.googleAuth.UserData
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth


    fun signInWithGoogle(onDone: () -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val userUId = Firebase.auth.currentUser?.uid.toString()
        val newUser = MUser(user?.email, user?.displayName)

        Firebase.firestore.collection("users").document(userUId).set(newUser)
            .addOnSuccessListener {
                UserAndFridgeData.user = newUser
                onDone()
            }.addOnFailureListener {
                Log.d("FB",
                    "Exception occurs when setting user in firestore: $it")
            }
    }

    fun signIn(
        email: String,
        password: String,
        toHome: () -> Unit,
        userNotFound: () -> Unit,
        changeLoadingValue: (Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                changeLoadingValue(true)
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "signIn Successful: ${task.result}")
                            changeLoadingValue(false)
                            toHome()
                        } else {
                            Log.d("FB", "signIn unsuccessful: ${task.exception}")
                            changeLoadingValue(false)
                            userNotFound()
                        }
                    }.addOnFailureListener {
                        changeLoadingValue(false)
                    }
            } catch (e: Exception) {
                changeLoadingValue(false)
            }
        }
    }
}