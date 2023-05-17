package com.patmy.ourfridge.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patmy.ourfridge.R
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.navigation.OurFridgeScreens

@Composable
fun OurFridgeAppTopBar(
    screen: String = "",
    onProfileClicked: () -> Unit = {},
    onShowHistory: () -> Unit = {},
) {
    TopAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary, title = {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "OurFridge",
                modifier = Modifier,
                color = MaterialTheme.colors.secondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (screen == "home") {
                val interactionSource = remember {
                    MutableInteractionSource()
                }
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "View History ",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            if (UserAndFridgeData.user?.fridge != null && UserAndFridgeData.user?.fridge != "null") {
                                onShowHistory()
                            }
                        },
                    tint = MaterialTheme.colors.primaryVariant,
                )
            }
        }
    }, navigationIcon = {
        Icon(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile icon",
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
                .clickable { onProfileClicked.invoke() },
            tint = MaterialTheme.colors.secondary
        )
    })
}

@Composable
fun OurFridgeAppBottomBar(
    navController: NavController,
    currentScreen: String? = null,
) {

    BottomAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary) {
        Row(modifier = Modifier.fillMaxWidth()) {
            BottomAppBarIcon(
                modifier = Modifier.fillMaxWidth(0.333f),
                title = "People",
                icon = Icons.Default.People,
                highlight = currentScreen == "social"
            ) {
                navController.navigate(OurFridgeScreens.SocialScreen.name)
            }
            BottomAppBarIcon(
                modifier = Modifier.fillMaxWidth(0.5f),
                title = "Home",
                icon = Icons.Default.Home,
                highlight = currentScreen == "home"
            ) {
                navController.navigate(OurFridgeScreens.FridgeHomeScreen.name)
            }

            // defining toast when user try to access shopping screen without fridge
            val context = LocalContext.current
            val toastDuration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(
                context, "Join or create fridge to access shopping list", toastDuration
            )

            BottomAppBarIcon(
                modifier = Modifier.fillMaxWidth(),
                title = "Shopping",
                icon = Icons.Default.AddShoppingCart,
                highlight = currentScreen == "shopping"
            ) {
                if (UserAndFridgeData.fridge != null) {
                    navController.navigate(OurFridgeScreens.ShoppingScreen.name)
                } else {
                    toast.show()
                }
            }
        }
    }
}


@Composable
fun BottomAppBarIcon(
    modifier: Modifier,
    title: String,
    icon: ImageVector,
    highlight: Boolean,
    onClick: () -> Unit,
) {

    val backgroundColor = if (highlight) {
        if (isSystemInDarkTheme()) Color(0x26000000) else Color(0x26FF0000)
    } else {
        MaterialTheme.colors.primary
    }

    Card(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        Column(
            modifier = Modifier.clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colors.secondary
            )
            Text(
                text = title,
                modifier = Modifier,
                fontSize = 14.sp,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}