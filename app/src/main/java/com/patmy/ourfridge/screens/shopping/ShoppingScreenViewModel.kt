package com.patmy.ourfridge.screens.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser

class ShoppingScreenViewModel: ViewModel() {

    private val userUId = Firebase.auth.currentUser?.uid.toString()
    private val db = Firebase.firestore


}