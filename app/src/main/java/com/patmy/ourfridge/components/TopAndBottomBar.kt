package com.patmy.ourfridge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patmy.ourfridge.R

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
        },
    )
}

@Composable
fun OurFridgeAppBottomBar(navController: NavController) {
    BottomAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            BottomAppBarIcon(title = "People", icon = Icons.Default.People) {
                //TODO navController to people screen
            }
            BottomAppBarIcon(title = "Home", icon = Icons.Default.Home) {
                ////TODO navController to home screen
            }
        }
    }
}

@Composable
fun BottomAppBarIcon(title: String, icon: ImageVector, onClick: () -> Unit) {
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