package com.patmy.ourfridge.screens.shopping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun AddArticlesButton(onClick: () -> Unit) {

    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(bottomStartPercent = 100, bottomEndPercent = 100),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Text(
            text = "Finish shopping",
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = 5.dp, horizontal = 25.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight(500),
            color = MaterialTheme.colors.primaryVariant
        )
    }
}