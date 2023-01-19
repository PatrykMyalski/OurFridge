package com.patmy.ourfridge.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.FoodLabel
import com.patmy.ourfridge.components.OurFridgeAppBottomBar
import com.patmy.ourfridge.components.OurFridgeAppTopBar
import com.patmy.ourfridge.model.MFoodInside
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch

@Composable
fun FridgeHomeScreen(navController: NavController) {

    //TODO Opis tabeli z border na dole pod opisem,
    // Dummy data z posiłkami w liście,
    // Composable odpowiedzialny za tworzenie pozycji na liście
    // przyciski add i delete posiłki z listy
    // przycisk nad cardem pozwalający sprawdzić historie lodówki


    val DUMMY_LIST = listOf(
        MFoodInside(id = "2asd11", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd12", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd13", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd14", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd15", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd16", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd17", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd18", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd19", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd10", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd111", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd112", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd113", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd114", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd115", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd116", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd117", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd118", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
        MFoodInside(id = "2asd119", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"),
    )


    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
        OurFridgeAppTopBar(onProfileClicked = {
            scope.launch { scaffoldState.drawerState.open() }
        })
    },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
                      ProfileSideBar(){
                          Firebase.auth.signOut()
                          navController.navigate(OurFridgeScreens.LoginScreen.name)
                      }
        },
        backgroundColor = MaterialTheme.colors.background,
    bottomBar = {
        OurFridgeAppBottomBar(navController)
    }){



        HomeScreenView(data = DUMMY_LIST, onAddFood = {/*TODO adding food to fridge*/})



    }
}

@Composable
fun ProfileSideBar(onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(80.dp)){

        Text(text = "Profile Sidebar")
        Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", modifier = Modifier.clickable {onClick.invoke()})
    }
}

@Composable
fun ShowFoodInfo(foodInfo: MutableState<MFoodInside>, onClose: () -> Unit) {
    val textModifier = Modifier.padding(bottom = 3.dp)
        Card(modifier = Modifier.fillMaxSize(), backgroundColor = Color(0x67000000)){
            Card(modifier = Modifier
                .fillMaxSize()
                .padding(top = 270.dp, start = 30.dp, end = 30.dp, bottom = 180.dp),
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(10)) {
                Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally ) {
                    Column(verticalArrangement = Arrangement.Top) {
                        Text(text = "Food Name: ${foodInfo.value.title}", modifier = textModifier)
                        Text(text = "In fridge is: ${foodInfo.value.quantity}${foodInfo.value.unit}", modifier = textModifier)
                        Text(text = "Added: ${foodInfo.value.date}", modifier = textModifier)
                        Text(text = "Added by: ${foodInfo.value.idOfCreator}")
                    }
                    Card(modifier = Modifier
                        .padding(6.dp)
                        .width(80.dp)
                        .height(40.dp)
                        .clickable { onClose.invoke() },
                        backgroundColor = MaterialTheme.colors.primary) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Close", modifier = Modifier, color = MaterialTheme.colors.secondary)
                        }
                    }
                }
            }
        }
    }


@Composable
fun HomeScreenView(data: List<MFoodInside> = emptyList(), onAddFood: () -> Unit){

    val showFoodInfo = remember { mutableStateOf(false) }
    val foodInfo = remember {
        mutableStateOf(MFoodInside(id = "2asd1", title = "Food", quantity = "200", unit = "g", date = "16/01/2023", idOfCreator = "Patryk Myalski"))
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
                    if (data.isNullOrEmpty()){
                        Text(text = "Add what you have in your fridge...", modifier = Modifier.padding(2.dp), color = Color.Gray)
                    } else {
                        for (food in data){
                            FoodLabel(food){
                                showFoodInfo.value = true
                                foodInfo.value = food
                            }
                        }
                    }
                }
            }
            Card(modifier = Modifier
                .width(340.dp)
                .height(50.dp)
                .clickable { onAddFood.invoke() },
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                backgroundColor = MaterialTheme.colors.primary) {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "+Add food",
                        modifier = Modifier,
                        color = MaterialTheme.colors.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
    if (showFoodInfo.value){
        ShowFoodInfo(foodInfo){
            showFoodInfo.value = false
        }
    }
    }










