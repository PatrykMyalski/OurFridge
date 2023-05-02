package com.patmy.ourfridge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.data.UserAndFridgeData


@Composable
fun ProfileSideBar(onClick: () -> Unit) {
    val interactionSource = MutableInteractionSource()

    //TODO create viewModel actions

    val userState = remember {
        mutableStateOf(UserAndFridgeData.user)
    }

    val sidebarViewState = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = UserAndFridgeData.user) {
        userState.value = UserAndFridgeData.user
    }


    Card(modifier = Modifier.fillMaxSize(), backgroundColor = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            fun confirmText(type: String): String {
                return "Are you sure you want to $type? You can't undo this action!"
            }

            fun goBack() {
                sidebarViewState.value = ""
            }

            when (sidebarViewState.value) {
                "LEAVE" -> ConfirmView(confirmText("leave this fridge"),
                    onConfirm = {},
                    onDecline = { goBack() })
                "CLEAR_HISTORY" -> ConfirmView(confirmText("clear fridge history"),
                    onConfirm = {},
                    onDecline = { goBack() })
                "DELETE_FOOD" -> ConfirmView(confirmText("delete all food from fridge"),
                    onConfirm = {},
                    onDecline = { goBack() })
                "CLEAR_SHOPPING" -> ConfirmView(confirmText("clear all positions on shopping list"),
                    onConfirm = {},
                    onDecline = { goBack() })
                else -> {
                    Text(
                        text = "Hi ${if (userState.value == null) "" else userState.value?.username}!",
                        modifier = Modifier.padding(top = 15.dp),
                        fontSize = 26.sp,
                        color = MaterialTheme.colors.secondary
                    )

                    SettingsView(onLeaveFridge = { sidebarViewState.value = "LEAVE" },
                        onClearHistory = { sidebarViewState.value = "CLEAR_HISTORY" },
                        onDeleteAllFood = { sidebarViewState.value = "DELETE_FOOD" },
                        onClearShoppingList = { sidebarViewState.value = "CLEAR_SHOPPING" })

                    Column(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            onClick.invoke()
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
    onLeaveFridge: () -> Unit,
    onClearHistory: () -> Unit,
    onDeleteAllFood: () -> Unit,
    onClearShoppingList: () -> Unit,
) {


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

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
    }
}

@Composable
fun SettingsOption(title: String, onClick: () -> Unit) {

    val color = MaterialTheme.colors.primaryVariant


    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = 5.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(vertical = 5.dp),
            fontSize = 20.sp,
            color = color
        )
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = color)
    }

}

