package com.patmy.ourfridge.screens.shopping

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.data.UserAndFridgeData

@Composable
fun FAB(onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(bottom = 100.dp),
        backgroundColor = MaterialTheme.colors.primary,
        shape = CircleShape
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add",
            modifier = Modifier
                .size(60.dp)
                .clickable { onClick() },
            tint = MaterialTheme.colors.secondary
        )
    }
}

@Composable
fun ShoppingInfoText(
    text: String,
    modifier: Modifier = Modifier,
    padding: Int = 20,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        modifier = modifier.padding(vertical = padding.dp),
        textAlign = textAlign,
        fontSize = 24.sp,
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun AddArticlesButton(loading: Boolean, onClick: () -> Unit) {

    val isChild = UserAndFridgeData.user!!.role == "child"
    val context = LocalContext.current
    val toastDuration = Toast.LENGTH_SHORT
    val toast =
        Toast.makeText(context, "As children you are not able to do that!", toastDuration)

    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(bottomStartPercent = 100, bottomEndPercent = 100),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        if (!loading) {
            Text(text = "Finish shopping",
                modifier = Modifier
                    .clickable { if (!isChild) onClick() else toast.show() }
                    .padding(vertical = 5.dp, horizontal = 25.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight(500),
                color = if (!isChild) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.background)
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colors.primaryVariant,
                strokeWidth = 2.dp
            )
        }
    }
}