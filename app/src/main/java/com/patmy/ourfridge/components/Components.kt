package com.patmy.ourfridge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patmy.ourfridge.R
import com.patmy.ourfridge.model.MFoodInside

@Composable
fun OurFridgeAppTopBar(
    title: String,
    icon: ImageVector? = null,
    showLogo: Boolean = true,
    showProfile: Boolean = true,
    navController: NavController,
    onHomeClicked: () -> Unit = { TODO("Nav to HomeScreen")},
    onProfileClicked: () -> Unit = { TODO("Click on profile icon, maybe side bar or profile screen")}) {

    TopAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary, title = {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            if (showLogo) {
                Icon(painter = painterResource(id = R.drawable.applogo),
                    contentDescription = "app logo",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .clickable { },
                    tint = MaterialTheme.colors.secondary)
            }
            Text(text = title, modifier = Modifier.padding(end = 15.dp),
                color = MaterialTheme.colors.secondary,
                fontSize = 18.sp, fontWeight = FontWeight.Bold)
            if (showProfile){
                Icon(painter = painterResource(id = R.drawable.profile),
                    contentDescription = "profile icon",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .clickable { },
                    tint = MaterialTheme.colors.secondary)
            }

        }
    })
}

@Composable
fun OurFridgeAppBottomBar(navController: NavController) {
    BottomAppBar(modifier = Modifier, backgroundColor = MaterialTheme.colors.primary) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            BottomAppBarIcon(title = "People", icon = Icons.Default.People){
                //TODO navController to people screen
            }
            BottomAppBarIcon(title = "Home", icon = Icons.Default.Home){
                ////TODO navController to home screen
            }
        }
    }
}

@Composable
fun BottomAppBarIcon(title: String, icon: ImageVector, onClick: () -> Unit){
    Column(modifier = Modifier.clickable{ onClick.invoke() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = title,modifier = Modifier.size(30.dp), tint = MaterialTheme.colors.secondary)
        Text(text = title, modifier = Modifier, fontSize = 14.sp , color = MaterialTheme.colors.secondary)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(loading: Boolean = false, registration: Boolean = false, onSubmit: (email: String, password: String) -> Unit){

    val modifier = Modifier
        .padding(top = 30.dp)
        .fillMaxWidth()
        .height(200.dp)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val emailState = rememberSaveable{ mutableStateOf("") }
        val passwordState = rememberSaveable { mutableStateOf("") }
        val passwordAgainState = rememberSaveable { mutableStateOf("") }
        val passwordVisibility = rememberSaveable { mutableStateOf(false) }
        val passwordFocusRequest = remember { FocusRequester() }
        val passwordAgainFocusRequest = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(emailState.value, passwordState.value) {
            if (registration){
                emailState.value.trim().isNotEmpty() && passwordState.value.trim().isNotEmpty() && passwordAgainState.value.trim().isNotEmpty()
            } else {
                emailState.value.trim().isNotEmpty() && passwordState.value.trim().isNotEmpty()
            }
        }
        val validForm = remember { mutableStateOf(true) }
        val samePasswords = remember { mutableStateOf(true) }

        EmailInput(emailState = emailState, onAction = KeyboardActions{
            passwordFocusRequest.requestFocus()
        })
        PasswordInput(modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = passwordState,
            label = "Password",
            enabled = true,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions{
                passwordAgainFocusRequest.requestFocus()
            })
        if (registration) {
            PasswordInput(modifier = Modifier.focusRequester(passwordAgainFocusRequest),
                passwordState = passwordAgainState,
                label = "Reply Password",
                enabled = true,
                passwordVisibility = passwordVisibility,
                passwordReply = registration)
        }

        if (!samePasswords.value){
            Text(text = "Passwords are not the same!",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colors.error)
        }

        SubmitButton(title = if (registration) "Register" else "Login" , loading, valid){
            if (registration) {
                samePasswords.value =  passwordState.value == passwordAgainState.value
                validForm.value = emailState.value.contains('@') && passwordState.value.length >= 6
            } else {
                validForm.value = emailState.value.contains('@') && passwordState.value.length >= 6
            }
            onSubmit(emailState.value, passwordState.value)
        }
        if (!validForm.value){
            if (registration){
                Text(text = "Email must include '@' and password must be at least 6 characters!",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center)
            } else {
                Text(text = "Email or password is incorrect!",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center)
            }

        }
    }
}

@Composable
fun SubmitButton(title: String, loading: Boolean, validInputs: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 40.dp, end = 40.dp),
        enabled = !loading && validInputs,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.secondary,
            disabledBackgroundColor = MaterialTheme.colors.primary,
            disabledContentColor = MaterialTheme.colors.background)) {
        if (loading) CircularProgressIndicator()
        else Text(text = title, modifier = Modifier, fontSize = 20.sp)
    }
}

@Composable
fun PasswordInput(modifier: Modifier,
                  passwordState: MutableState<String>,
                  label: String,
                  enabled: Boolean,
                  passwordReply: Boolean = false,
                  passwordVisibility: MutableState<Boolean>,
                  imeAction: ImeAction = ImeAction.Done,
                  onAction: KeyboardActions = KeyboardActions.Default){
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value ,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = label)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primaryVariant,
            focusedLabelColor = MaterialTheme.colors.primaryVariant,
            unfocusedBorderColor = MaterialTheme.colors.primary,
            unfocusedLabelColor =  MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primaryVariant
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = { if ( !passwordReply ) PasswordVisibility(passwordVisibility = passwordVisibility) }
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    Icon(imageVector = Icons.Default.Visibility,
        contentDescription = "change visibility",
        modifier = Modifier.clickable { passwordVisibility.value = !visible },
        tint = MaterialTheme.colors.primaryVariant)
}


@Composable
fun EmailInput(modifier: Modifier = Modifier,
               emailState: MutableState<String>,
               label: String = "Email",
               enabled: Boolean = true,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions
){
    InputField(modifier = modifier,
        valueState = emailState,
        label = label,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)
}

@Composable
fun InputField(modifier: Modifier = Modifier,
               valueState: MutableState<String>,
               label: String,
               enabled: Boolean,
               singleLine: Boolean = true,
               keyboardType: KeyboardType,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default
){
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
            unfocusedLabelColor =  MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primaryVariant
        ),
        textStyle = TextStyle (fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
    )
}

@Composable
fun FoodLabel(food: MFoodInside, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(4.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }){
            Text(text = food.title.toString(), modifier = Modifier.padding(end = 18.dp))
            Text(text = food.quantity.toString(), modifier = Modifier.padding(end = 2.dp))
            Text(text = food.unit.toString())
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black, thickness = 1.dp)
    }
}
