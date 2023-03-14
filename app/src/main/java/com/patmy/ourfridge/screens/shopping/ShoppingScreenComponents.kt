package com.patmy.ourfridge.screens.shopping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FAB(onClick: () -> Unit) {
    Card(modifier = Modifier.padding(bottom = 100.dp), backgroundColor = MaterialTheme.colors.primary, shape = CircleShape) {
        Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(60.dp).clickable(onClick = onClick), tint = MaterialTheme.colors.secondary)
    }
}

@Composable
fun ShoppingInfoText(text: String, padding: Int = 20){
    Text(text = text, modifier = Modifier.padding(vertical = padding.dp), textAlign = TextAlign.Center, fontSize = 24.sp, color = MaterialTheme.colors.primaryVariant)
}