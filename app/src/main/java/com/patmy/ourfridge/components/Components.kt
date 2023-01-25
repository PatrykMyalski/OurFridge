package com.patmy.ourfridge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.model.MFoodInside


@Composable
fun ErrorMessage(text: String) {
    Text(text = text,
        modifier = Modifier.padding(20.dp),
        fontSize = 16.sp,
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Center)
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    label: String,
    enabled: Boolean,
    singleLine: Boolean = true,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = label) },
        enabled = enabled,
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primaryVariant,
            focusedLabelColor = MaterialTheme.colors.primaryVariant,
            unfocusedBorderColor = MaterialTheme.colors.primary,
            unfocusedLabelColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primaryVariant
        ),
        textStyle = TextStyle(fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
    )
}

@Composable
fun FoodLabel(food: MFoodInside, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(4.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }) {
            Text(text = food.title.toString(), modifier = Modifier.padding(end = 18.dp))
            Text(text = food.quantity.toString(), modifier = Modifier.padding(end = 2.dp))
            Text(text = food.unit.toString())
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black, thickness = 1.dp)
    }
}

@Composable
fun AddFoodMenuButtons(
    title: String,
    elevation: ButtonElevation?,
    colors: ButtonColors,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(onClick = { onClick.invoke() },
        modifier = Modifier
            .width(130.dp)
            .height(50.dp)
            .padding(horizontal = 10.dp),
        elevation = elevation,
        shape = RoundedCornerShape(25),
        colors = colors,
        enabled = enabled) {
        Text(text = title)
    }
}
