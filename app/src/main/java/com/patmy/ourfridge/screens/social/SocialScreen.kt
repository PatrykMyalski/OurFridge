package com.patmy.ourfridge.screens.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.ErrorMessage
import com.patmy.ourfridge.components.OurFridgeAppBottomBar
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.ProfileSideBar
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch

@Composable
fun FridgeScreen(
    navController: NavController,
    viewModel: SocialScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    val fridge = remember {
        mutableStateOf<MFridge?>(MFridge())
    }

    val currentUser = remember {
        mutableStateOf<MUser?>(null)
    }

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val loadingData = remember {
        mutableStateOf(true)
    }

    val joiningToFridgeLoading = remember {
        mutableStateOf(false)
    }

    val throwInvalidFridgeId = remember {
        mutableStateOf(false)
    }

    if (currentUser.value == null){
        viewModel.getData{ updateUser, updateFridge ->
            currentUser.value = updateUser
            fridge.value = updateFridge
            loadingData.value = false
        }
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            OurFridgeAppTopBar(onProfileClicked = {
                scope.launch { scaffoldState.drawerState.open() }
            })
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            ProfileSideBar() {
                Firebase.auth.signOut()
                navController.navigate(OurFridgeScreens.LoginScreen.name)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            OurFridgeAppBottomBar(navController, currentScreen = "social")
        }) {

        if (loadingData.value) {
            CircularProgressIndicator()
        } else {
            SocialScreenView(currentUser = currentUser.value,
                fridge = fridge.value,
                joiningToFridgeLoading = joiningToFridgeLoading.value,
                fridgeNotFound = throwInvalidFridgeId.value,
                onJoinToFridge = { idInput ->
                    joiningToFridgeLoading.value = true
                    viewModel.joinFridge(idInput,
                        joinedToFridge = { currentUserUpdate, fridgeUpdate ->
                            currentUser.value = currentUserUpdate
                            fridge.value = fridgeUpdate
                            joiningToFridgeLoading.value = false
                        }, fridgeNotFound = {
                            throwInvalidFridgeId.value = true
                            joiningToFridgeLoading.value = false

                        })
                })
        }
    }
}

@Composable
fun SocialScreenView(
    currentUser: MUser?,
    fridge: MFridge?,
    joiningToFridgeLoading: Boolean,
    fridgeNotFound: Boolean,
    onJoinToFridge: (String) -> Unit,
) {
    println(fridge?.id)
    if (fridge?.id == null){
        if (joiningToFridgeLoading){
            CircularProgressIndicator()
        } else{
            JoinToFridgeComponent(fridgeNotFound) {
                onJoinToFridge(it)
            }
        }
    } else {
        Text("Main Social Screen")
    }

}

@Composable
fun JoinToFridgeComponent(fridgeNotFound: Boolean, onJoinToFridge: (String) -> Unit) {

    val text = remember {
        mutableStateOf("")
    }
    val maxChars = 6

    val isTextValid = remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Pass here 6-digit code which you will get from users you want to join",
            modifier = Modifier.padding(top = 100.dp, bottom = 30.dp, start = 30.dp, end = 30.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primaryVariant,
            overflow = TextOverflow.Ellipsis)

        TextField(value = text.value,
            onValueChange = {
                val newString = it.trim()
                text.value = newString.take(maxChars)
                isTextValid.value = text.value.isNotEmpty()
            },
            placeholder = {
                Text(text = "6-digit",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.primaryVariant,
                    textAlign = TextAlign.Center)
            },
            modifier = Modifier.width(150.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp),
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(cursorColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.secondary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary))
        
        if (fridgeNotFound){
            ErrorMessage(text = "Unable to find fridge with that id!")
        }

        Button(onClick = { onJoinToFridge(text.value) },
            modifier = Modifier.padding(top = 20.dp),
            enabled = isTextValid.value,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(disabledBackgroundColor = MaterialTheme.colors.primary,
                disabledContentColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primaryVariant)) {
            Text(text = "Confirm",
                modifier = Modifier.padding(5.dp),
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp))
        }
    }
}
