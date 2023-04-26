package com.patmy.ourfridge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patmy.ourfridge.data.UserAndFridgeData

@Composable
fun ProfileSideBar(onClick: () -> Unit) {
    val interactionSource = MutableInteractionSource()

    Card(modifier = Modifier.fillMaxSize(), backgroundColor = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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