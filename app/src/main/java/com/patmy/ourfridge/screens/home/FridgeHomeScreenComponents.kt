package com.patmy.ourfridge.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.components.*
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MFHistory
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
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FoodInfoButtons(text = "Close") { onClose() }
            FoodInfoButtons(text = "Change") { onChange() }
        }
    }
}


@Composable
fun FoodInfoButtons(
    text: String,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp),
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() }, modifier = Modifier
            .width(125.dp)
            .height(50.dp), shape = shape
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

@Composable
fun FoodChangeView(
    foodInfo: MFood?,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onQuantityChange: (String, String) -> Unit,
) {

    val quantityState = remember {
        mutableStateOf("")
    }

    val inputValueToBig = remember {
        mutableStateOf(false)
    }

    val invalidInput = remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You are changing ${foodInfo?.title}.",
            fontSize = 18.sp,
            color = MaterialTheme.colors.primaryVariant
        )


        InputField(
            modifier = Modifier.padding(top = 10.dp),
            valueState = quantityState,
            label = "Quantity",
            enabled = true,
            keyboardType = KeyboardType.Number
        )
        if (inputValueToBig.value) ErrorMessage(text = "You can't take out more than you have!")

        if (invalidInput.value) ErrorMessage(text = "Field is empty!")

        Row(modifier = Modifier.padding(top = 5.dp, bottom = 30.dp)) {
            FoodInfoButtons(text = "Take out", shape = RoundedCornerShape(15.dp)) {
                if (quantityState.value.isNotEmpty()) {
                    if (quantityState.value.toInt() > foodInfo?.quantity?.toInt()!!) {
                        inputValueToBig.value = true
                    } else if (quantityState.value.toInt() == foodInfo.quantity?.toInt()!!) {
                        inputValueToBig.value = false
                        onDelete()
                    } else {
                        inputValueToBig.value = false
                        onQuantityChange("-", quantityState.value)
                    }
                } else {
                    invalidInput.value = true
                }

            }
            Spacer(modifier = Modifier.width(20.dp))
            FoodInfoButtons(text = "Put in", shape = RoundedCornerShape(15.dp)) {
                inputValueToBig.value = false
                if (quantityState.value.isNotEmpty()) {
                    onQuantityChange("+", quantityState.value)
                } else {
                    invalidInput.value = true
                }

            }
        }
        Row {
            FoodInfoButtons(text = "Close") {
                onClose()
            }
            Spacer(modifier = Modifier.width(60.dp))
            FoodInfoButtons(text = "Delete") {
                onDelete()
            }
        }
    }
}

@Composable
fun ShowFoodInfo(
    foodInfo: MFood?,
    onClose: () -> Unit,
    onDelete: (MFood?) -> Unit,
    onDecreaseFoodQuantity: (MFood?, String) -> Unit,
    onIncreaseFoodQuantity: (MFood?, String) -> Unit,
) {

    val showInfo = remember {
        mutableStateOf(true)
    }

    if (showInfo.value) {
        PopUpTemplate(onClose = { onClose() }) {
            FoodInfoView(foodInfo, onClose) {
                showInfo.value = false
            }
        }
    } else {
        PopUpWithTextField(onClose = onClose) {
            FoodChangeView(foodInfo, onClose, onDelete = {
                onDelete(foodInfo)
            }, onQuantityChange = { type, value ->
                if (type == "-") {
                    onDecreaseFoodQuantity(foodInfo, value)
                } else {
                    onIncreaseFoodQuantity(foodInfo, value)
                }
            })
        }
    }
}

@Composable
fun FoodLabel(food: MFood?, onClick: () -> Unit, onDelete: () -> Unit) {
    Column(modifier = Modifier.padding(4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke() },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(text = food?.title.toString(), modifier = Modifier.padding(end = 18.dp))
                Text(
                    text = "${food?.quantity.toString()}${food?.unit.toString()}",
                    modifier = Modifier.padding(end = 2.dp)
                )
            }
            Icon(imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.clickable { onDelete() })
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black, thickness = 1.dp)
    }
}


@Composable
fun ShowHistory(onClose: () -> Unit) {

    val myList = remember {
        mutableStateOf(UserAndFridgeData.fridge?.fridgeHistory)
    }

    PopUpTemplate(onClose = onClose) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Card(modifier = Modifier, shape = RoundedCornerShape(2)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .verticalScroll(rememberScrollState())
                ) {
                    val iterator = myList.value!!.listIterator(myList.value!!.size)
                    while (iterator.hasPrevious()) {
                        val element = iterator.previous()
                        EventLabel(event = element)
                    }
                }
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onClose() }) {
                Text(text = "Close", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun EventLabel(event: MFHistory?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "${event?.event}", textAlign = TextAlign.Center)
    }
    Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black, thickness = 1.dp)
}

