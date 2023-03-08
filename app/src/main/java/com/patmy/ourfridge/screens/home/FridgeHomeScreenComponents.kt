package com.patmy.ourfridge.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.model.MFood


@Composable
fun FoodInfoView(foodData: MFood?, onClose: () -> Unit, onChange: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            FoodInfoText(text = "Food: ${foodData?.title}")
            FoodInfoText(text = "Quantity: ${foodData?.quantity}${foodData?.unit}")
            FoodInfoText(text = "Added: ${foodData?.date}")
            FoodInfoText(text = "Added by: ${foodData?.nameOfCreator}")
        }
        Row(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FoodInfoButtons(text = "Close") { onClose() }
            FoodInfoButtons(text = "Change") { onChange() }
        }
    }
}


@Composable
fun FoodInfoButtons(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(125.dp)
            .height(50.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight(450),
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
fun FoodInfoText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(bottom = 3.dp),
        style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.primaryVariant)
    )
}

@Composable
fun JoinOrCreateFridgeButtons(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colors.primaryVariant,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
