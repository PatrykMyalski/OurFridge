package com.patmy.ourfridge.screens.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.UserForm
import com.patmy.ourfridge.navigation.OurFridgeScreens

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationScreenViewModel = viewModel(),
) {

    val emailAlreadyAtUse = remember {
        mutableStateOf(false)
    }
    val loadingValue = remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = { OurFridgeAppTopBar() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), color = MaterialTheme.colors.background
        ) {
            UserForm(
                navController,
                registration = true,
                emailAlreadyAtUse = emailAlreadyAtUse.value,
                loading = loadingValue.value
            ) { email: String, password: String, username: String ->
                viewModel.signUp(email = email,
                    password = password,
                    username = username,
                    toHome = { navController.navigate(OurFridgeScreens.FridgeHomeScreen.name) },
                    emailAlreadyAtUse = { emailAlreadyAtUse.value = true },
                    changeLoadingValue = { loadingValue.value = !loadingValue.value })
            }
        }
    }
}