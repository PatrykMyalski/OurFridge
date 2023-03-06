package com.patmy.ourfridge.screens.home

import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.*
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MFood
import com.patmy.ourfridge.model.radioUnits
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch

@Composable
fun FridgeHomeScreen(
    navController: NavController,
    viewModel: FridgeHomeScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {


    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val loadingData = remember {
        mutableStateOf(true)
    }
    val loggingOut = remember {
        mutableStateOf(false)
    }

    val loadingInAddFoodForm = remember {
        mutableStateOf(false)
    }

    val loadingFridge = remember {
        mutableStateOf(false)
    }

    if (UserAndFridgeData.user == null && !loggingOut.value) {
        viewModel.getData { updateUser, updateFridge ->
            if (updateUser !== null) {
                UserAndFridgeData.setData(updateUser, updateFridge)
                loadingData.value = false
            } else {
                Log.d("FB", "ERROR: Cannot get user from firestore")
            }
            if (updateFridge !== null) {
                loadingData.value = false
            } else {
                loadingData.value = false
            }
        }
    } else {
        loadingData.value = false
    }


    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            OurFridgeAppTopBar(onProfileClicked = {
                scope.launch { scaffoldState.drawerState.open() }
            })
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            ProfileSideBar {
                loggingOut.value = true
                Firebase.auth.signOut()
                navController.navigate(OurFridgeScreens.LoginScreen.name)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            OurFridgeAppBottomBar(navController, currentScreen = "home")
        }) {
        Box(modifier = Modifier.padding(it)) {
            if (loadingData.value) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                }
            } else {
                HomeScreenView(
                    loadingFridge = loadingFridge.value,
                    loadingInAddFoodForm = loadingInAddFoodForm.value,
                    onJoinFridge = { navController.navigate(OurFridgeScreens.SocialScreen.name) },
                    onCreateFridge = {
                        loadingFridge.value = true
                        viewModel.createFridge(UserAndFridgeData.user) { updateFridge, updateCurrentUser ->
                            UserAndFridgeData.setData(updateCurrentUser, updateFridge)
                            loadingFridge.value = false
                        }
                    },
                    onAddFoodToFridge = { food ->
                        //TODO adding history event to fridge and user
                        loadingInAddFoodForm.value = true
                        viewModel.addFoodToFridge(
                            food,
                            UserAndFridgeData.user,
                            UserAndFridgeData.fridge!!
                        ) { updatedFridge ->
                            UserAndFridgeData.setData(updateFridge = updatedFridge)
                            loadingInAddFoodForm.value = false
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun HomeScreenView(
    loadingFridge: Boolean,
    loadingInAddFoodForm: Boolean,
    onJoinFridge: () -> Unit,
    onCreateFridge: () -> Unit,
    onAddFoodToFridge: (MFood) -> Unit,
) {

    val showFoodInfo = remember { mutableStateOf(false) }
    val foodInfo = remember {
        mutableStateOf<MFood?>(null)
    }
    val showAddFoodMenu = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What is in your fridge?",
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 20.sp, color = MaterialTheme.colors.primaryVariant
        )
        Card(
            modifier = Modifier
                .width(340.dp)
                .height(500.dp)
                .padding(top = 20.dp),
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (loadingFridge) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                } else {
                    if (UserAndFridgeData.user?.fridge == null || UserAndFridgeData.user?.fridge == "null") {
                        Text(
                            text = "Join or create fridge",
                            modifier = Modifier.padding(2.dp),
                            color = Color.Gray
                        )
                    } else {
                        if (UserAndFridgeData.fridge?.foodInside.isNullOrEmpty()) {
                            Text(
                                text = "Add what you have in fridge...",
                                modifier = Modifier.padding(2.dp),
                                color = Color.Gray
                            )
                        } else if (UserAndFridgeData.fridge?.foodInside!![0]?.title == null) {
                            Text(
                                text = "Add what you have in fridge...",
                                modifier = Modifier.padding(2.dp),
                                color = Color.Gray
                            )
                        } else {
                            for (food in UserAndFridgeData.fridge!!.foodInside) {
                                FoodLabel(food) {
                                    showFoodInfo.value = true
                                    foodInfo.value = food
                                }
                            }
                        }
                    }
                }
            }
        }
        if (UserAndFridgeData.user?.fridge == null || UserAndFridgeData.user?.fridge == "null") {
            Row(
                modifier = Modifier.width(340.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                JoinOrCreateFridgeButtons(title = "Join fridge") {
                    onJoinFridge()
                }
                JoinOrCreateFridgeButtons(title = "Create fridge") {
                    onCreateFridge()
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .width(340.dp)
                    .height(50.dp)
                    .clickable { showAddFoodMenu.value = true },
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "+Add food",
                        modifier = Modifier,
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
    if (showFoodInfo.value) {
        ShowFoodInfo(foodInfo) {
            showFoodInfo.value = false
        }
    }
    if (showAddFoodMenu.value) {
        AddFoodMenu(loading = loadingInAddFoodForm, onClose = {
            showAddFoodMenu.value = false
        }, onAdd = { newFoodTitle, newFoodQuantity, newFoodUnit ->
            onAddFoodToFridge(
                MFood(
                    title = newFoodTitle,
                    quantity = newFoodQuantity,
                    unit = newFoodUnit
                )
            )
        })
    }
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
fun AddFoodMenu(
    loading: Boolean,
    onClose: () -> Unit,
    onAdd: (foodTitle: String, foodQuantity: String, foodUnit: String) -> Unit,
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
                    .padding(top = 100.dp, start = 30.dp, end = 30.dp)
                    .clickable(interactionSource = interactionSource, indication = null) {

                    },
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(10),
                elevation = 5.dp

            ) {
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
                            Row(modifier = Modifier
                                .selectable(
                                    selected = (unit == selectedUnit),
                                    onClick = {
                                        onUnitSelected(unit)
                                    }
                                )
                                .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = (unit == selectedUnit),
                                    onClick = { onUnitSelected(unit) })
                                Text(text = unit)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        AddFoodMenuButtons(
                            title = "Close",
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 3.dp,
                                disabledElevation = 0.dp,
                                hoveredElevation = 8.dp,
                                focusedElevation = 8.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.primaryVariant
                            ),
                            enabled = true
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
                            onAdd(foodTitleState.value, foodQuantityState.value, selectedUnit)
                            if (!loading) {
                                onClose()
                            }
                        }
                    }
                }
            }
        }
    }
}

//TODO in the future add possibility of deleting food or decreasing quantity in this composable
// -reduce boilerplate code by extracting Cards into one Compose
// -improve looks of this compose
@Composable
fun ShowFoodInfo(foodInfo: MutableState<MFood?>, onClose: () -> Unit) {

    val foodData = foodInfo.value

    PopUpTemplate(onClose = { onClose() }) {
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
                FoodInfoButtons(text = "Change") {/*TODO Change view to ChangeQuantityOrDeleteFood*/ }
            }
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
