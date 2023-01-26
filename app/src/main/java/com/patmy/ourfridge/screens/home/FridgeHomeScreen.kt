package com.patmy.ourfridge.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.*
import com.patmy.ourfridge.model.MFoodInside
import com.patmy.ourfridge.model.MUser
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch

@Composable
fun FridgeHomeScreen(
    navController: NavController,
    //start: Boolean = true,
    //viewModel: FridgeHomeScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {


    val DUMMY_LIST = listOf(
        MFoodInside(id = "2asd11",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd12",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd13",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd14",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd15",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd16",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd17",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd18",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd19",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd10",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd111",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd112",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd113",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd114",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd115",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd116",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd117",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd118",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd119",
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"),
    )

/*
    val fridgeId = remember {
        mutableStateOf("")
    }
*/

/*    val currentUser = remember {
        mutableStateOf(MUser("", "", ""))
    }*/
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
/*    val loadingData = remember {
        mutableStateOf(true)
    }*/
/*    val loadingFridge = remember {
        mutableStateOf(false)
    }*/

/*    if (start) {
        viewModel.GetData {
            currentUser.value = it
            fridgeId.value = currentUser.value.fridge
            loadingData.value = false
        }
    }*/


    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            OurFridgeAppTopBar(onProfileClicked = {
                scope.launch { scaffoldState.drawerState.open() }
            })
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            ProfileSideBar() {
                Firebase.auth.signOut()
                navController.navigate(OurFridgeScreens.LoginScreen.name)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            OurFridgeAppBottomBar(navController)
        }) {

/*        if (loadingData.value) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator(color = MaterialTheme.colors.secondary)
            }

        } else {*/
            HomeScreenView(/*fridgeId = fridgeId.value,
                loadingFridge = loadingFridge.value,
                onJoinFridge = { *//*TODO*//* },
                onCreateFridge = {
                    loadingFridge.value = true
                    viewModel.createFridge(currentUser.value)
                }*/)
        //}


    }
}


@Composable
fun HomeScreenView(
    data: List<MFoodInside> = emptyList(),
/*    fridgeId: String,
    loadingFridge: Boolean,
    onJoinFridge: () -> Unit,
    onCreateFridge: () -> Unit,*/
) {

    val showFoodInfo = remember { mutableStateOf(false) }
    val foodInfo = remember {
        mutableStateOf(MFoodInside(id = "2asd1",    //Todo
            title = "Food",
            quantity = "200",
            unit = "g",
            date = "16/01/2023",
            idOfCreator = "Patryk Myalski"))
    }
    val showAddFoodMenu = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "What is in your fridge?",
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 20.sp, color = MaterialTheme.colors.secondary)
        Card(modifier = Modifier
            .width(340.dp)
            .height(500.dp)
            .padding(top = 20.dp),
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            backgroundColor = Color.White) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                //if (fridgeId == "null" || fridgeId == "") {
                    Text(text = "Add what you have in your fridge...",
                        modifier = Modifier.padding(2.dp),
                        color = Color.Gray)
/*                } else {
                    for (food in data) {
                        FoodLabel(food) {
                            showFoodInfo.value = true
                            foodInfo.value = food
                        }
                    }
                }*/
            }
        }
/*        if (fridgeId == "null" || fridgeId == "") {
            Row(modifier = Modifier.width(340.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                JoinOrCreateFridgeButtons(title = "Join fridge") {
                    //onJoinFridge()
                }
                JoinOrCreateFridgeButtons(title = "Create fridge") {
                    //onCreateFridge()
                }
            }
        } else {*/
            Card(modifier = Modifier
                .width(340.dp)
                .height(50.dp)
                .clickable { showAddFoodMenu.value = true },
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                backgroundColor = MaterialTheme.colors.primary) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "+Add food",
                        modifier = Modifier,
                        color = MaterialTheme.colors.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        //}

    }
    if (showFoodInfo.value) {
        ShowFoodInfo(foodInfo) {
            showFoodInfo.value = false
        }
    }
    if (showAddFoodMenu.value) {
        AddFoodMenu() {
            showAddFoodMenu.value = false
        }
    }
}

@Composable
fun JoinOrCreateFridgeButtons(title: String, onClick: () -> Unit) {
    Card(modifier = Modifier
        .height(50.dp)
        .clickable { onClick() },
        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
        backgroundColor = MaterialTheme.colors.primary) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title,
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colors.secondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun AddFoodMenu(onClose: () -> Unit) {
    val foodTitleState = remember {
        mutableStateOf("")
    }
    val foodQuantityState = remember {
        mutableStateOf("")
    }
    val radioOptions = listOf("piece", "g", "kg", "l")
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[0])
    }
    val addingLoadingState = remember {
        mutableStateOf(false)
    }
    val interactionSource = MutableInteractionSource()

    Card(modifier = Modifier
        .fillMaxSize()
        .clickable(interactionSource = interactionSource, indication = null) {
            onClose()
        }, backgroundColor = Color(0x67000000)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .padding(top = 100.dp, start = 30.dp, end = 30.dp)
                    .clickable(interactionSource = interactionSource, indication = null) {

                    },
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(10),
                elevation = 5.dp

            ) {
                Column(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
                    InputField(valueState = foodTitleState,
                        label = "Name",
                        enabled = true,
                        keyboardType = KeyboardType.Text)
                    InputField(valueState = foodQuantityState,
                        label = "Quantity",
                        enabled = true,
                        keyboardType = KeyboardType.Number)
                    Row {
                        radioOptions.forEach { text ->
                            Row(modifier = Modifier
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                    }
                                )
                                .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) })
                                Text(text = text)
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {

                        AddFoodMenuButtons(title = "Close",
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 3.dp,
                                disabledElevation = 0.dp,
                                hoveredElevation = 8.dp,
                                focusedElevation = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.secondary),
                            enabled = true) {
                            onClose()
                        }

                        AddFoodMenuButtons(title = "+Add",
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 3.dp,
                                disabledElevation = 5.dp,
                                hoveredElevation = 8.dp,
                                focusedElevation = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.secondary,
                                disabledBackgroundColor = MaterialTheme.colors.primary,
                                disabledContentColor = MaterialTheme.colors.background),
                            enabled = foodTitleState.value.trim()
                                .isNotEmpty() && foodQuantityState.value.trim().isNotEmpty()) {
                            //TODO adding to firebase database
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ShowFoodInfo(foodInfo: MutableState<MFoodInside>, onClose: () -> Unit) {
    val textModifier = Modifier.padding(bottom = 3.dp)
    Card(modifier = Modifier.fillMaxSize(), backgroundColor = Color(0x67000000)) {
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(top = 270.dp, start = 30.dp, end = 30.dp, bottom = 180.dp),
            backgroundColor = MaterialTheme.colors.background,
            shape = RoundedCornerShape(10)) {
            Column(modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Column(verticalArrangement = Arrangement.Top) {
                    Text(text = "Food Name: ${foodInfo.value.title}", modifier = textModifier)
                    Text(text = "In fridge is: ${foodInfo.value.quantity}${foodInfo.value.unit}",
                        modifier = textModifier)
                    Text(text = "Added: ${foodInfo.value.date}", modifier = textModifier)
                    Text(text = "Added by: ${foodInfo.value.idOfCreator}")
                }
                Card(modifier = Modifier
                    .padding(6.dp)
                    .width(80.dp)
                    .height(40.dp)
                    .clickable { onClose.invoke() },
                    backgroundColor = MaterialTheme.colors.primary) {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Close",
                            modifier = Modifier,
                            color = MaterialTheme.colors.secondary)
                    }
                }
            }
        }
    }
}










