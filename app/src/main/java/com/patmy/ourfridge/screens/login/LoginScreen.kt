package com.patmy.ourfridge.screens.login

import android.app.Activity
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.patmy.ourfridge.screens.login.googleAuth.GoogleAuthUiClient
import com.patmy.ourfridge.screens.login.googleAuth.UserData
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
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
        if(state.isSignInSuccessful){
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
                        context,
                        error,
                        Toast.LENGTH_SHORT
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

/*@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel = viewModel()) {
    val userNotFound = remember {
        mutableStateOf(false)
    }
    val loadingValue = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("195580127662-k43k3g1timgdsvogovhmna9dhamq21e6.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val firebaseAuth = FirebaseAuth.getInstance()

    LaunchedEffect(key1 = firebaseAuth.currentUser) {
        println(firebaseAuth.currentUser)
        if (firebaseAuth.currentUser != null) {
            viewModel.signInWithGoogle{
                navController.navigate(OurFridgeScreens.FridgeHomeScreen.name)
            }
        }
    }

    Scaffold(topBar = { OurFridgeAppTopBar() }) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it), color = MaterialTheme.colors.background) {
            UserForm(navController,
                userNotFound = userNotFound.value,
                loading = loadingValue.value,
                onGoogleLogin = {
                    val signInIntent = googleSignInClient.signInIntent
                    (context as? Activity)?.startActivityForResult(signInIntent, RC_SIGN_IN)
                }
            ) { email: String, password: String, username: String ->
                viewModel.signIn(email,
                    password,
                    toHome = { navController.navigate(OurFridgeScreens.FridgeHomeScreen.name) },
                    userNotFound = { userNotFound.value = true },
                    changeLoadingValue = { loadingValue.value = it })
            }
        }
    }
}

private const val RC_SIGN_IN = 9001*/
