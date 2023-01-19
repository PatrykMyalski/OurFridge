package com.patmy.ourfridge.screens.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.UserForm
import com.patmy.ourfridge.navigation.OurFridgeScreens


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel = viewModel()) {

    val userNotFound = remember {
        mutableStateOf(false)
    }
    val loadingValue = remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = { OurFridgeAppTopBar() }) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            UserForm(navController,
                userNotFound = userNotFound.value,
                loading = loadingValue.value) { email: String, password: String ->
                viewModel.signIn(email,
                    password,
                    toHome = { navController.navigate(OurFridgeScreens.FridgeHomeScreen.name) },
                    userNotFound = { userNotFound.value = true },
                    changeLoadingValue = {loadingValue.value = !loadingValue.value})
            }
        }
    }
}

