package com.patmy.ourfridge.screens.shopping

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.*
import com.patmy.ourfridge.model.MArticle
import com.patmy.ourfridge.model.MFood
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.launch


@Composable
fun ShoppingScreen(
    navController: NavController,
    viewModel: ShoppingScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val loggingOut = remember {
        mutableStateOf(false)
    }

    val showArticleAddMenu = remember {
        mutableStateOf(false)
    }

    val shoppingList = remember {
        mutableListOf<MArticle>()
    }

    if (shoppingList.isEmpty()){
        //viewModel.getData()
    }


    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            OurFridgeAppTopBar(screen = "shopping", onProfileClicked = {
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
        floatingActionButton = {
            FAB {
                showArticleAddMenu.value = true
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            OurFridgeAppBottomBar(navController, currentScreen = "shopping")
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ShoppingMainView(shoppingList, viewModel)
        }
        if (showArticleAddMenu.value) {
            AddFoodMenu(loading = false, onClose = { showArticleAddMenu.value = false }, onAdd = {
                //TODO
            })
        }
    }
}


@Composable
fun ShoppingMainView(shoppingList: List<MArticle?>, viewModel: ShoppingScreenViewModel) {



    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ShoppingInfoText(text = "Our shopping list")
        Card(
            modifier = Modifier.fillMaxSize(0.8f),
            shape = RoundedCornerShape(5),
            backgroundColor = MaterialTheme.colors.background,
            border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (shoppingList.isEmpty()) {
                    ShoppingInfoText(text = "Add shopping articles using add button")
                } else {
                    for (article in shoppingList) {
                        ArticleLabel(article, viewModel){
                            //viewModel.deleteArticle(it)
                        }
                    }
                }
            }
        }
        AddArticlesButton {
            //TODO
        }
    }
}



@Composable
fun ArticleLabel(articleInfo: MArticle?, viewModel: ShoppingScreenViewModel, onDeleteArticle: (MArticle?) -> Unit) {

    val checked = remember {
        mutableStateOf(articleInfo?.checked!!)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(5.dp),
            backgroundColor = MaterialTheme.colors.background,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShoppingInfoText(text = articleInfo?.title.toString(), padding = 0)
                Card(
                    modifier = Modifier.size(20.dp),
                    backgroundColor = if (!checked.value) Color(0xFF9CD857) else Color(0xFF559B05),
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        modifier = Modifier.clickable {
                            /* if (!checked.value) {
                                viewModel.checkArticle(articleInfo){
                                    checked.value = true
                                }
                            } else {
                                viewModel.uncheckArticle(articleInfo){
                                    checked.value = false
                                }
                            }*/
                        },
                        tint = Color.Black
                    )
                }
            }
        }
        val interactionSource = MutableInteractionSource()
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onDeleteArticle(articleInfo)
            },
            tint = MaterialTheme.colors.secondary
        )
    }
}




    

