package com.patmy.ourfridge.screens.shopping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
        Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier
            .size(60.dp)
            .clickable(onClick = onClick), tint = MaterialTheme.colors.secondary)
    }
}

@Composable
fun ShoppingInfoText(text: String, modifier: Modifier = Modifier, padding: Int = 20){
    Text(text = text, modifier = modifier.padding(vertical = padding.dp), textAlign = TextAlign.Start, fontSize = 24.sp, color = MaterialTheme.colors.primaryVariant)
}

@Composable
fun AddArticlesButton(loading: Boolean, onClick: () -> Unit) {

    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(bottomStartPercent = 100, bottomEndPercent = 100),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        if (!loading){
            Text(
                text = "Finish shopping",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(vertical = 5.dp, horizontal = 25.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colors.primaryVariant
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = MaterialTheme.colors.primaryVariant, strokeWidth = 2.dp)
        }

    }
}