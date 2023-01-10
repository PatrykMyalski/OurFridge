package com.patmy.ourfridge.screens.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
}