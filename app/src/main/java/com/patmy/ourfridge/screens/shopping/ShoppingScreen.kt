package com.patmy.ourfridge.screens.shopping

import android.widget.Toast
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patmy.ourfridge.components.*
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MArticle
import com.patmy.ourfridge.model.MShoppingList
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

    val loadingInAddArticles = remember {
        mutableStateOf(false)
    }

    val showArticleAddMenu = remember {
        mutableStateOf(false)
    }

    val showAddToFridgeConfirm = remember {
        mutableStateOf(false)
    }

    val shoppingList = remember {
        mutableStateOf(UserAndFridgeData.shoppingList)
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
            if (shoppingList.value?.shoppingList != null) {
                ShoppingMainView(shoppingList.value?.shoppingList,
                    viewModel,
                    loadingInAddArticles.value,
                    onDelete = {
                        loadingInAddArticles.value = !loadingInAddArticles.value
                    },
                    onAddArticles = {
                        showAddToFridgeConfirm.value = true
                    })
            }
        }
        if (showArticleAddMenu.value) {
            AddFoodMenu(loading = false,
                onClose = { showArticleAddMenu.value = false },
                onAdd = { article ->
                    loadingInAddArticles.value = true
                    viewModel.onAddArticle(article) { shoppingListUpdate ->
                        shoppingList.value?.shoppingList = shoppingListUpdate
                        loadingInAddArticles.value = false
                    }
                })
        }
        if (showAddToFridgeConfirm.value) {
            AddToFridgeConfirm(onDoNotDelete = {
                viewModel.finishShopping(false, shoppingList.value?.shoppingList!!) {
                    showAddToFridgeConfirm.value = false
                }
            }, onDelete = {
                viewModel.finishShopping(true, shoppingList.value?.shoppingList!!) {
                    showAddToFridgeConfirm.value = false
                }
            }, onClose = {
                showAddToFridgeConfirm.value = false
            })
        }
    }
}

@Composable
fun AddToFridgeConfirm(onClose: () -> Unit, onDoNotDelete: () -> Unit, onDelete: () -> Unit) {
    PopUpTemplate(onClose = { onClose() }) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddToFridgeConfirmText(text = "You will add all checked articles to the fridge.")
            Spacer(modifier = Modifier.height(5.dp))
            AddToFridgeConfirmText(text = "Do you want to delete all unchecked articles?")
            Spacer(modifier = Modifier.height(12.dp))
            AddToFridgeConfirmText(text = "If you want to go back just click outside this popup.")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AddToFridgeConfirmButton(title = "Delete") {
                    onDelete()
                }
                Spacer(modifier = Modifier.width(50.dp))
                AddToFridgeConfirmButton(title = "Don't delete") {
                    onDoNotDelete()
                }
            }
        }
    }
}

@Composable
fun AddToFridgeConfirmText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 5.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun AddToFridgeConfirmButton(title: String, onClick: () -> Unit) {

    Button(
        onClick = { onClick() },
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.primaryVariant
        )
    ) {
        Text(text = title)
    }
}

@Composable
fun ShoppingMainView(
    shoppingList: List<MArticle?>?,
    viewModel: ShoppingScreenViewModel,
    loading: Boolean,
    onDelete: () -> Unit,
    onAddArticles: () -> Unit,
) {

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
                if (shoppingList!![0]?.id == null || shoppingList[0]?.id == "null") {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ShoppingInfoText(
                            text = "Add shopping articles using add button",
                            textAlign = TextAlign.Center
                        )
                    }

                } else {
                    for (article in shoppingList) {
                        ArticleLabel(article, viewModel) {
                            onDelete()
                            viewModel.deleteArticle(it) {
                                onDelete()
                            }
                        }
                    }
                }
            }
        }
        val context = LocalContext.current
        val toastDuration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(
            context,
            "First you need to add something to shopping list",
            toastDuration
        )
        AddArticlesButton(loading) {
            if ((shoppingList!![0]?.id == null || shoppingList[0]?.id == "null")) {
                toast.show()
            } else {
                onAddArticles()
            }

        }
    }
}


@Composable
fun ArticleLabel(
    articleInfo: MArticle?,
    viewModel: ShoppingScreenViewModel,
    onDeleteArticle: (MArticle?) -> Unit,
) {

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
                ShoppingInfoText(
                    text = "${articleInfo?.title} ${articleInfo?.quantity}${articleInfo?.unit}",
                    modifier = Modifier.fillMaxWidth(0.85f),
                    padding = 0
                )
                Card(
                    modifier = Modifier.size(20.dp),
                    backgroundColor = if (!checked.value) Color.Transparent else Color(0xFF7FE906),
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        modifier = Modifier.clickable {
                            if (!checked.value) {
                                viewModel.changeCheck(true, articleInfo) {
                                    checked.value = true
                                }
                            } else {
                                viewModel.changeCheck(false, articleInfo) {
                                    checked.value = false
                                }
                            }
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
                interactionSource = interactionSource, indication = null
            ) {
                onDeleteArticle(articleInfo)
            },
            tint = MaterialTheme.colors.secondary
        )
    }
}





