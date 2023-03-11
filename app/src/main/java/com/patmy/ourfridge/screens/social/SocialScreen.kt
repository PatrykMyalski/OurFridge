package com.patmy.ourfridge.screens.social

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.*
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MFridge
import com.patmy.ourfridge.model.MUser
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch


@Composable
fun SocialScreen(
    navController: NavController,
    viewModel: SocialScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {


    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()


    val loggingOut = remember {
        mutableStateOf(false)
    }

    val joiningToFridgeLoading = remember {
        mutableStateOf(false)
    }

    val throwInvalidFridgeId = remember {
        mutableStateOf(false)
    }

    val confirmUserRoleChangePopUp = remember {
        mutableStateOf(false)
    }
    val userRoleToChange = remember {
        mutableStateOf<MUser?>(null)
    }
    val loadingRoleChange: MutableState<Boolean> = remember {
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

        Box(modifier = Modifier.padding(it))
        SocialScreenView(
            fridge = UserAndFridgeData.fridge,
            joiningToFridgeLoading = joiningToFridgeLoading.value,
            roleChangeLoading = loadingRoleChange.value,
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
            }, changeUserRole = { user ->
                userRoleToChange.value = user
                confirmUserRoleChangePopUp.value = true
            })
    }

    if (confirmUserRoleChangePopUp.value) {
        ConfirmPopUp("Are you sure you want to change role of ${userRoleToChange.value?.username}? User now " +
                "${if (userRoleToChange.value?.role == "user") "won't" else "will"} be able to add food to fridge",
            onConfirm = {
                loadingRoleChange.value = true
                viewModel.changeRole(userRoleToChange.value) {
                    loadingRoleChange.value = false

                }
                confirmUserRoleChangePopUp.value = false
            },
            onClose = { confirmUserRoleChangePopUp.value = false })
    }
}


@Composable
fun SocialScreenView(
    fridge: MFridge?,
    roleChangeLoading: Boolean,
    joiningToFridgeLoading: Boolean,
    fridgeNotFound: Boolean,
    onJoinToFridge: (String) -> Unit,
    changeUserRole: (MUser?) -> Unit,
) {
    if (fridge?.id == null) {
        if (joiningToFridgeLoading) {
            CircularProgressIndicator()
        } else {
            JoinToFridgeComponent(fridgeNotFound) {
                onJoinToFridge(it)
            }
        }
    } else {
        SocialMainView(roleChangeLoading) {
            changeUserRole(it)
        }
    }
}
