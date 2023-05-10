package com.patmy.ourfridge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.patmy.ourfridge.model.MFood
import com.patmy.ourfridge.model.radioUnits
import com.patmy.ourfridge.utilities.MyUtils.Companion.checkIfMoreThanThreeDecimals


@Composable
fun ErrorMessage(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 15.dp),
        fontSize = 16.sp,
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Center
    )
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
fun PopUpTemplate(onClose: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x67000000))
    ) {
        Popup(alignment = Alignment.Center,
            properties = PopupProperties(),
            onDismissRequest = { onClose() }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(MaterialTheme.colors.background, RoundedCornerShape(20)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
fun PopUpWithTextField(onClose: () -> Unit, content: @Composable () -> Unit) {

    val interactionSource = MutableInteractionSource()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = interactionSource, indication = null) {
                onClose()
            }, backgroundColor = Color(0x67000000)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, top = 100.dp)
                    .clickable(interactionSource = interactionSource, indication = null) {},
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(10),
                elevation = 5.dp
            ) {
                content()
            }
        }
    }
}

@Composable
fun ConfirmPopUp(text: String, onConfirm: () -> Unit, onClose: () -> Unit) {
    PopUpTemplate(onClose) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp),
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primaryVariant
        )
        Button(onClick = { onConfirm() }) {
            Text(text = "Confirm", color = MaterialTheme.colors.primaryVariant)
        }
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
    Button(
        onClick = { onClick.invoke() },
        modifier = Modifier
            .width(130.dp)
            .height(50.dp)
            .padding(horizontal = 10.dp),
        elevation = elevation,
        shape = RoundedCornerShape(25),
        colors = colors,
        enabled = enabled
    ) {
        Text(text = title)
    }
}

@Composable
fun AddFoodMenu(
    loading: Boolean,
    onClose: () -> Unit,
    onAdd: (MFood) -> Unit,
) {
    val foodTitleState = remember {
        mutableStateOf("")
    }
    val foodQuantityState = remember {
        mutableStateOf("")
    }

    val (selectedUnit, onUnitSelected) = remember {
        mutableStateOf(radioUnits[0].displayValue)
    }

    val invalidQuantity = remember {
        mutableStateOf(false)
    }

    val moreThanThreeDecimals = remember {
        mutableStateOf(false)
    }

    PopUpWithTextField(onClose = onClose) {
        Column(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
            InputField(
                valueState = foodTitleState,
                label = "Name",
                enabled = true,
                keyboardType = KeyboardType.Text
            )
            InputField(
                valueState = foodQuantityState,
                label = "Quantity",
                enabled = true,
                keyboardType = KeyboardType.Number
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                radioUnits.forEach { text ->
                    val unit = text.displayValue
                    Row(
                        modifier = Modifier
                            .selectable(selected = (unit == selectedUnit), onClick = {
                                onUnitSelected(unit)
                            })
                            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (unit == selectedUnit),
                            onClick = { onUnitSelected(unit) })
                        Text(text = unit)
                    }
                }
            }
            if (invalidQuantity.value || moreThanThreeDecimals.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ErrorMessage(text = if (invalidQuantity.value) "Pass valid quantity value!" else "Quantity can have max of three decimals!")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {

                AddFoodMenuButtons(
                    title = "Close", elevation = ButtonDefaults.elevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 3.dp,
                        disabledElevation = 0.dp,
                        hoveredElevation = 8.dp,
                        focusedElevation = 8.dp
                    ), colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.primaryVariant
                    ), enabled = true
                ) {
                    onClose()
                }

                AddFoodMenuButtons(
                    title = if (loading) "Loading..." else "+Add",
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 3.dp,
                        disabledElevation = 5.dp,
                        hoveredElevation = 8.dp,
                        focusedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.primaryVariant,
                        disabledBackgroundColor = MaterialTheme.colors.primary,
                        disabledContentColor = MaterialTheme.colors.background
                    ),
                    enabled = foodTitleState.value.trim()
                        .isNotEmpty() && foodQuantityState.value.trim().isNotEmpty()
                ) {

                    if (foodQuantityState.value.contains(',')) foodQuantityState.value = foodQuantityState.value.replace(',', '.')
                    if (foodQuantityState.value.toFloatOrNull() == null || foodQuantityState.value.last() == '.' || foodQuantityState.value.first() == '.') {
                        invalidQuantity.value = true
                    } else {
                        if (invalidQuantity.value) invalidQuantity.value = false
                        if (checkIfMoreThanThreeDecimals(foodQuantityState.value)) {
                            moreThanThreeDecimals.value = true
                        } else {
                            if (moreThanThreeDecimals.value) moreThanThreeDecimals.value = false
                            onAdd(
                                MFood(
                                    title = foodTitleState.value,
                                    quantity = foodQuantityState.value,
                                    unit = selectedUnit
                                )
                            )
                        }
                    }
                    if (!loading && !invalidQuantity.value && !moreThanThreeDecimals.value) {
                        onClose()
                    }
                }
            }
        }
    }
}
