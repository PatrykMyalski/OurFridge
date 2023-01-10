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
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import java.util.logging.XMLFormatter

@Composable
fun FridgeHomeScreen(navController: NavController) {
    Scaffold(topBar = {
        OurFridgeAppTopBar(title = "OurFridge", navController = navController)
    }, backgroundColor = MaterialTheme.colors.background) {

    }
}


