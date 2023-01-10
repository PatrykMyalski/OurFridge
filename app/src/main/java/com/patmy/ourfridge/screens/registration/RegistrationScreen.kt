package com.patmy.ourfridge.screens.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.UserForm

@Composable
fun RegistrationScreen(navController: NavController) {
    Scaffold(topBar = { OurFridgeAppTopBar(title = "OurFridge", navController = navController, showProfile = false) }) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            UserForm(registration = true){
                TODO("onSubmit")
            }
        }
    }
}