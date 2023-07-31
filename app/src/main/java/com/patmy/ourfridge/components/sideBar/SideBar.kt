package com.patmy.ourfridge.components.sideBar

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.data.UserAndFridgeData


@Composable
fun ProfileSideBar(
    viewModel: SideBarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    screen: String = "",
    drawerState: Boolean,
    onLogout: () -> Unit,
    onSettingChanged: () -> Unit,
    onAccountDelete: () -> Unit,
    onShowHistory: () -> Unit = {},
) {

    val interactionSource = MutableInteractionSource()

    val userState = remember {
        mutableStateOf(UserAndFridgeData.user)
    }

    val sidebarViewState = remember {
        mutableStateOf("")
    }

    fun confirmText(type: String): String {
        return "Are you sure you want to $type? You can't undo this action!"
    }

    fun goBack() {
        sidebarViewState.value = ""
    }

    LaunchedEffect(key1 = UserAndFridgeData.user) {
        userState.value = UserAndFridgeData.user
    }

    LaunchedEffect(key1 = drawerState) {
        goBack()
    }


    val context = LocalContext.current
    val toastDuration = Toast.LENGTH_SHORT
    val toast = Toast.makeText(
        context, "Something went wrong!", toastDuration
    )


    Card(modifier = Modifier.fillMaxSize(), backgroundColor = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            when (sidebarViewState.value) {
                "LEAVE" -> ConfirmView(confirmText("leave this fridge"), onConfirm = {
                    viewModel.leaveFridge(onDone = {
                        onSettingChanged()
                        goBack()
                    }, onFailure = {
                        toast.show()
                        goBack()
                    })
                }, onDecline = { goBack() })
                "CLEAR_HISTORY" -> ConfirmView(confirmText("clear fridge history"), onConfirm = {
                    viewModel.clearHistory(onDone = {
                        onSettingChanged()
                        goBack()
                    }, onFailure = {
                        toast.show()
                        goBack()
                    })
                }, onDecline = { goBack() })
                "DELETE_FOOD" -> ConfirmView(confirmText("delete all food from fridge"),
                    onConfirm = {
                        viewModel.deleteFood(onDone = {
                            onSettingChanged()
                            goBack()
                        }, onFailure = {
                            toast.show()
                            goBack()
                        })
                    },
                    onDecline = { goBack() })
                "CLEAR_SHOPPING" -> ConfirmView(confirmText("clear all positions on shopping list"),
                    onConfirm = {
                        viewModel.clearShopping(onDone = {
                            onSettingChanged()
                            goBack()
                        }, onFailure = {
                            toast.show()
                            goBack()
                        })
                    },
                    onDecline = { goBack() })
                "DELETE_ACCOUNT" -> ConfirmView(
                    text = confirmText("delete your account"),
                    onConfirm = {
                        viewModel.deleteAccount(onDone = {
                            onAccountDelete()
                            goBack()
                        }, onFailure = {
                            toast.show()
                            goBack()
                        })
                    }, onDecline = { goBack() })


                else -> {
                    Column(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OurFridge",
                            color = MaterialTheme.colors.secondary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Hi ${if (userState.value == null) "" else userState.value?.username}!",
                            modifier = Modifier.padding(top = 15.dp),
                            fontSize = 26.sp,
                            color = MaterialTheme.colors.secondary
                        )
                    }


                    SettingsView(screen = screen,
                        onLeaveFridge = { sidebarViewState.value = "LEAVE" },
                        onClearHistory = { sidebarViewState.value = "CLEAR_HISTORY" },
                        onDeleteAllFood = { sidebarViewState.value = "DELETE_FOOD" },
                        onClearShoppingList = { sidebarViewState.value = "CLEAR_SHOPPING" },
                        onDeleteAccount = { sidebarViewState.value = "DELETE_ACCOUNT" },
                        onShowHistory = { onShowHistory() })

                    Column(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            onLogout.invoke()
                            UserAndFridgeData.clearData()
                        }, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Logout",
                            modifier = Modifier.padding(10.dp),
                            color = MaterialTheme.colors.secondary
                        )
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            modifier = Modifier.size(35.dp),
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmView(text: String, onConfirm: () -> Unit, onDecline: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(0.8f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.primaryVariant,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onConfirm() }) {
                Text(text = "Yes", fontSize = 20.sp)
            }
            Button(onClick = { onDecline() }) {
                Text(text = "No", fontSize = 20.sp)
            }
        }
    }
}


@Composable
fun SettingsView(
    screen: String,
    onLeaveFridge: () -> Unit,
    onClearHistory: () -> Unit,
    onDeleteAllFood: () -> Unit,
    onClearShoppingList: () -> Unit,
    onDeleteAccount: () -> Unit,
    onShowHistory: () -> Unit,
) {


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        if (screen == "home") {
            SettingsOption(title = "Show fridge history") {
                onShowHistory()
            }
        }
        SettingsOption("Leave fridge") {
            onLeaveFridge()
        }
        SettingsOption(title = "Clear fridge history") {
            onClearHistory()
        }
        SettingsOption(title = "Delete all food from fridge") {
            onDeleteAllFood()
        }
        SettingsOption(title = "Clear shopping list") {
            onClearShoppingList()
        }
        SettingsOption(title = "Delete account") {
            onDeleteAccount()
        }
    }
}

@Composable
fun SettingsOption(title: String, onClick: () -> Unit) {

    val color = MaterialTheme.colors.primaryVariant
    val canDo = if (UserAndFridgeData.user!!.role == "child"){
        title == "Delete account"
    } else true

    val context = LocalContext.current
    val toastDuration = Toast.LENGTH_SHORT
    val toast =
        Toast.makeText(context, "As children you are not able to do that!", toastDuration)

    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(5.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.clickable { if (canDo) onClick() else toast.show() }, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                modifier = Modifier.padding(vertical = 5.dp),
                fontSize = 20.sp,
                color = if (canDo) color else Color(0, 0, 0, 51)
            )
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = color)
        }
    }

}

