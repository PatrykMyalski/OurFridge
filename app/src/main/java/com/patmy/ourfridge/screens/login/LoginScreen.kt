package com.patmy.ourfridge.screens.login

import android.widget.HorizontalScrollView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.UserForm
import com.patmy.ourfridge.navigation.OurFridgeScreens


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel = viewModel()) {

    Scaffold(topBar = { OurFridgeAppTopBar(title = "OurFridge", navController = navController, showProfile = false) }) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            UserForm { email: String, password: String ->
                viewModel.signIn(email, password) {
                    navController.navigate(OurFridgeScreens.FridgeHomeScreen.name)
                }
            }
        }
    }
}

