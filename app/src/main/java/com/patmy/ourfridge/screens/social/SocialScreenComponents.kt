package com.patmy.ourfridge.screens.social

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patmy.ourfridge.R
import com.patmy.ourfridge.components.ErrorMessage
import com.patmy.ourfridge.data.UserAndFridgeData
import com.patmy.ourfridge.model.MUser



@Composable
fun SocialMainView(roleChangeLoading: Boolean, changeUserRole: (MUser?) -> Unit) {

    val fridgeId = UserAndFridgeData.fridge?.id.toString()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your fridge share code",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 15.dp),
                color = MaterialTheme.colors.primaryVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(value = fridgeId,
                    onValueChange = {},
                    modifier = Modifier,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.secondary,
                        backgroundColor = MaterialTheme.colors.primary,
                        unfocusedIndicatorColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = MaterialTheme.colors.primary
                    ),
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = {
                        val interactionSource = MutableInteractionSource()

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, fridgeId)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        val context = LocalContext.current

                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                context.startActivity(shareIntent)
                            },
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    })
            }
            Text(
                text = "Send this 6-digit code to people you want invite to fridge.",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                color = MaterialTheme.colors.primaryVariant,
                textAlign = TextAlign.Center
            )

            val usersList = UserAndFridgeData.fridge?.fridgeUsers!!.toMutableList()

            // Adding new user to fridge for layout purpose
            if (!usersList.contains(UserAndFridgeData.user)) {
                usersList += UserAndFridgeData.user
            }

            if (UserAndFridgeData.user?.fridge !== null) {
                UsersList(usersList, roleChangeLoading) {
                    changeUserRole(it)
                }
            }
        }
    }
}



@Composable
fun UsersList(
    usersList: List<MUser?>,
    roleChangeLoading: Boolean,
    changeUserRole: (MUser?) -> Unit,
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 65.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (user in usersList) {
            if (UserAndFridgeData.user?.role == "admin") {
                if (user?.role == "admin") {
                    UserLabel(user)
                } else {
                    UserLabelAdmin(user, roleChangeLoading) {
                        changeUserRole(it)
                    }
                }
            } else {
                UserLabel(user)
            }
        }

    }
}

@Composable
fun UserLabelAdmin(
    user: MUser?,
    roleChangeLoading: Boolean = false,
    changeUserRole: (MUser?) -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val expanded = remember {
        mutableStateOf(false)
    }

    val userRoleToChange: MutableState<MUser?> = remember {
        mutableStateOf(null)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {

                userRoleToChange.value = null
                Icon(
                    painter = painterResource(id = (if (user?.role == "user") R.drawable.user_icon else if (user?.role == "admin") R.drawable.admin_icon else R.drawable.children_icon)),
                    contentDescription = "User_role",
                    modifier = Modifier.clickable(
                        interactionSource,
                        indication = null,
                        onClick = {
                            expanded.value = true
                        }), tint = MaterialTheme.colors.primaryVariant
                )

                DropdownMenu(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .width(80.dp),
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    Column(verticalArrangement = Arrangement.Center) {
                        DropdownMenuItem(onClick = {
                            userRoleToChange.value = user
                            changeUserRole(user)
                            expanded.value = false
                        }) {
                            Icon(
                                painter = painterResource(id = (if (user?.role == "user") R.drawable.user_icon else if (user?.role == "admin") R.drawable.admin_icon else R.drawable.children_icon)),
                                contentDescription = "User_role",
                                modifier = Modifier, tint = MaterialTheme.colors.primaryVariant
                            )
                        }

                        DropdownMenuItem(onClick = {
                            changeUserRole(user)
                            expanded.value = false
                        }) {
                            Icon(
                                painter = painterResource(id = (if (user?.role == "user") R.drawable.children_icon else R.drawable.user_icon)),
                                contentDescription = "User_role",
                                modifier = Modifier, tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                }
            }
            UsersLabelUsername(user = user)
        }
    }
}

@Composable
fun UserLabel(user: MUser?) {

    val userRoleIcon =
        if (user?.role == "user") R.drawable.user_icon else if (user?.role == "admin") R.drawable.admin_icon else R.drawable.children_icon

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = userRoleIcon),
                contentDescription = "User",
                modifier = Modifier,
                tint = MaterialTheme.colors.primaryVariant
            )
            UsersLabelUsername(user = user)
        }
    }
}

@Composable
fun UsersLabelUsername(user: MUser?) {
    Text(
        text = user?.username.toString(),
        modifier = Modifier.padding(end = 20.dp),
        style = TextStyle(color = MaterialTheme.colors.primaryVariant, fontSize = 24.sp)
    )
}

@Composable
fun JoinToFridgeComponent(fridgeNotFound: Boolean, onJoinToFridge: (String) -> Unit) {

    val text = remember {
        mutableStateOf("")
    }
    val maxChars = 6

    val isTextValid = remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Pass here 6-digit code which you will get from users you want to join",
            modifier = Modifier.padding(top = 15.dp, bottom = 30.dp, start = 30.dp, end = 30.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primaryVariant,
            overflow = TextOverflow.Ellipsis
        )

        TextField(
            value = text.value,
            onValueChange = {
                val newString = it.trim()
                text.value = newString.take(maxChars)
                isTextValid.value = text.value.isNotEmpty()
            },
            placeholder = {
                Text(
                    text = "6-digit",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.primaryVariant,
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier.width(150.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp),
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.secondary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary
            )
        )

        if (fridgeNotFound) {
            ErrorMessage(text = "Unable to find fridge with that id!")
        }

        Button(
            onClick = { onJoinToFridge(text.value) },
            modifier = Modifier.padding(top = 20.dp),
            enabled = isTextValid.value,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                disabledBackgroundColor = MaterialTheme.colors.primary,
                disabledContentColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primaryVariant
            )
        ) {
            Text(
                text = "Confirm",
                modifier = Modifier.padding(5.dp),
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)
            )
        }
    }
}