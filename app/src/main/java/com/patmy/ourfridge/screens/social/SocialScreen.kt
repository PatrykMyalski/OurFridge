package com.patmy.ourfridge.screens.social

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.R
import com.patmy.ourfridge.components.ErrorMessage
import com.patmy.ourfridge.components.OurFridgeAppBottomBar
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.components.ProfileSideBar
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch

@Composable
fun FridgeScreen(
    navController: NavController,
    viewModel: SocialScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {


    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val loadingData = remember {
        mutableStateOf(true)
    }

    val loggingOut = remember {
        mutableStateOf(false)
    }

    val joiningToFridgeLoading = remember {
        mutableStateOf(false)
    }

    val throwInvalidFridgeId = remember {
        mutableStateOf(false)
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            OurFridgeAppTopBar(onProfileClicked = {
                scope.launch { scaffoldState.drawerState.open() }
            })
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            ProfileSideBar {
                loggingOut.value = true
                Firebase.auth.signOut()
                navController.navigate(OurFridgeScreens.LoginScreen.name)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            OurFridgeAppBottomBar(navController, currentScreen = "social")
        }) {


        SocialScreenView(
            fridge = UserAndFridgeData.fridge,
            joiningToFridgeLoading = joiningToFridgeLoading.value,
            fridgeNotFound = throwInvalidFridgeId.value,
            onJoinToFridge = { idInput ->
                joiningToFridgeLoading.value = true
                viewModel.joinFridge(idInput,
                    joinedToFridge = { currentUserUpdate, fridgeUpdate ->
                        UserAndFridgeData.setData(currentUserUpdate, fridgeUpdate)
                        joiningToFridgeLoading.value = false
                    }, fridgeNotFound = {
                        throwInvalidFridgeId.value = true
                        joiningToFridgeLoading.value = false

                    })
            })

    }
}

@Composable
fun SocialScreenView(
    fridge: MFridge?,
    joiningToFridgeLoading: Boolean,
    fridgeNotFound: Boolean,
    onJoinToFridge: (String) -> Unit,
) {
    println(fridge?.id)
    if (fridge?.id == null) {
        if (joiningToFridgeLoading) {
            CircularProgressIndicator()
        } else {
            JoinToFridgeComponent(fridgeNotFound) {
                onJoinToFridge(it)
            }
        }
    } else {
        SocialMainView()

    }

}

@Composable
fun SocialMainView() {

    val fridgeId = UserAndFridgeData.fridge?.id.toString()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Your fridge share code",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 30.dp),
                color = MaterialTheme.colors.primaryVariant)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center) {
                TextField(value = fridgeId,
                    onValueChange = {},
                    modifier = Modifier,
                    textStyle = TextStyle(textAlign = TextAlign.Center,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.secondary,
                        backgroundColor = MaterialTheme.colors.primary,
                        unfocusedIndicatorColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = MaterialTheme.colors.primary
                    ),
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = {
                        val interactionSource = MutableInteractionSource()

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, fridgeId)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        val context = LocalContext.current

                        Icon(imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.clickable(interactionSource = interactionSource,
                                indication = null) {
                                context.startActivity(shareIntent)
                            },
                            tint = MaterialTheme.colors.primaryVariant)
                    })
            }
            Text(text = "Send this 6-digit code to people you want invite to fridge.",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                color = MaterialTheme.colors.primaryVariant,
                textAlign = TextAlign.Center)
            println(UserAndFridgeData.user?.fridge)
            if (UserAndFridgeData.user?.fridge !== null) {
                UsersList()
            }
        }

    }
}

@Composable
fun UsersList() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 65.dp)
        .verticalScroll(rememberScrollState())) {
        for (user in UserAndFridgeData.fridge?.fridgeUsers!!) {
            UserLabel(user)
        }

    }
}

@Composable
fun UserLabel(user: MUser?) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.user_icon),
                contentDescription = "User",
                tint = MaterialTheme.colors.primaryVariant)
            Text(text = user?.username.toString(),
                style = TextStyle(color = MaterialTheme.colors.primaryVariant, fontSize = 24.sp))
            Icon(painter = painterResource(id = R.drawable.history_icon),
                contentDescription = "View history",
                modifier = Modifier, tint = MaterialTheme.colors.primaryVariant)
        }


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

        if (fridgeNotFound) {
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