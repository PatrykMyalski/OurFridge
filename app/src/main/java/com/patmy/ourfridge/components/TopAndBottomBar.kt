package com.patmy.ourfridge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patmy.ourfridge.R
import com.patmy.ourfridge.navigation.OurFridgeScreens

@Composable
fun OurFridgeAppTopBar(onProfileClicked: () -> Unit = {}) {
    TopAppBar(
        modifier = Modifier, backgroundColor = MaterialTheme.colors.primary,
        title = {
            Text(text = "OurFridge", modifier = Modifier.padding(start = 70.dp),
                color = MaterialTheme.colors.secondary,
                fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            Icon(painter = painterResource(id = R.drawable.profile),
                contentDescription = "profile icon",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .clickable { onProfileClicked.invoke() },
                tint = MaterialTheme.colors.secondary)
        }
    )
}

@Composable
fun OurFridgeAppBottomBar(
    navController: NavController,
    currentScreen: String? = null,
) {

    BottomAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary) {
        Row(modifier = Modifier.fillMaxWidth()) {
            BottomAppBarIcon(modifier = Modifier.fillMaxWidth(0.5f),
                title = "People",
                icon = Icons.Default.People,
                highlight = currentScreen == "social") {
                navController.navigate(OurFridgeScreens.SocialScreen.name)
            }
            BottomAppBarIcon(modifier = Modifier.fillMaxWidth(),
                title = "Home",
                icon = Icons.Default.Home,
                highlight = currentScreen == "home") {
                navController.navigate(OurFridgeScreens.FridgeHomeScreen.name)
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

    Card(modifier = modifier,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) {
        Column(modifier = Modifier.clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colors.secondary)
            Text(text = title,
                modifier = Modifier,
                fontSize = 14.sp,
                color = MaterialTheme.colors.secondary)
        }
    }
}