package com.patmy.ourfridge.screens.login

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.UserForm
import com.patmy.ourfridge.navigation.OurFridgeScreens
import com.patmy.ourfridge.screens.login.googleAuth.SignInState
import com.patmy.ourfridge.screens.login.googleAuth.SignInViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import com.patmy.ourfridge.screens.login.googleAuth.GoogleAuthUiClient
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel = viewModel()) {


    val gViewModel = viewModel<SignInViewModel>()
    val state by gViewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val applicationContext = context.applicationContext

    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {

                    scope.launch(lifecycleOwner.lifecycleScope.coroutineContext) {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            intent = result.data ?: return@launch
                        )
                        gViewModel.onSignInResult(signInResult)
                    }

                }

            })


    val userNotFound = remember {
        mutableStateOf(false)
    }
    val loadingValue = remember {
        mutableStateOf(false)
    }

    val singInState = remember {
        mutableStateOf(SignInState())
    }


    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            viewModel.signInWithGoogle {
                navController.navigate(OurFridgeScreens.FridgeHomeScreen.name)
            }
        }
    }


    Scaffold(topBar = { OurFridgeAppTopBar() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), color = MaterialTheme.colors.background
        ) {
            LaunchedEffect(key1 = singInState.value.signInError) {
                singInState.value.signInError?.let { error ->
                    Toast.makeText(
                        context, error, Toast.LENGTH_SHORT
                    ).show()
                }
            }
            UserForm(navController,
                userNotFound = userNotFound.value,
                loading = loadingValue.value,
                onGoogleLogin = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }) { email: String, password: String, username: String ->
                viewModel.signIn(email,
                    password,
                    toHome = { navController.navigate(OurFridgeScreens.FridgeHomeScreen.name) },
                    userNotFound = { userNotFound.value = true },
                    changeLoadingValue = { loadingValue.value = it })
            }
        }
    }
}
