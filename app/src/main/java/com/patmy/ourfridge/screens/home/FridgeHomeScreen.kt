package com.patmy.ourfridge.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.NoFood
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patmy.ourfridge.R
import java.util.logging.XMLFormatter

@Composable
fun FridgeHomeScreen(navController: NavController) {
    Scaffold(topBar = {}, backgroundColor = MaterialTheme.colors.background) {
        OurFridgeAppTopBar(title = "OurFridge", navController = navController)
    }
}

@Composable
fun OurFridgeAppTopBar(
    title: String,
    icon: ImageVector? = null,
    showLogo: Boolean = true,
    showProfile: Boolean = true,
    navController: NavController,
    onHomeClicked: () -> Unit = { TODO("Nav to HomeScreen")},
    onProfileClicked: () -> Unit = { TODO("Click on profile icon, maybe side bar or profile screen")}) {

    TopAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary, title = {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            if (showLogo) {
                Icon(painter = painterResource(id = R.drawable.applogo),
                    contentDescription = "app logo",
                    modifier = Modifier.clip(shape = CircleShape).clickable { },
                    tint = MaterialTheme.colors.secondary)
            }
            Text(text = title, modifier = Modifier,
                color = MaterialTheme.colors.secondary,
                fontSize = 18.sp, fontWeight = FontWeight.Bold)
            if (showProfile){
                Icon(painter = painterResource(id = R.drawable.profile),
                    contentDescription = "profile icon",
                    modifier = Modifier.clip(shape = CircleShape).clickable { },
                    tint = MaterialTheme.colors.secondary)
            }

        }
    })
}
