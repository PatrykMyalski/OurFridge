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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patmy.ourfridge.navigation.OurFridgeScreens


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    navController: NavController,
    userNotFound: Boolean = false,
    emailAlreadyAtUse: Boolean = false,
    loading: Boolean = false,
    registration: Boolean = false,
    onSubmit: (email: String, password: String, userName: String) -> Unit,
) {

    val modifier = Modifier
        .padding(top = 30.dp)
        .fillMaxWidth()
        .height(200.dp)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val emailState = rememberSaveable { mutableStateOf("") }
        val userNameState = rememberSaveable { mutableStateOf("") }
        val passwordState = rememberSaveable { mutableStateOf("") }
        val passwordAgainState = rememberSaveable { mutableStateOf("") }
        val passwordVisibility = rememberSaveable { mutableStateOf(false) }
        val userNameFocusRequest = remember { FocusRequester() }
        val passwordFocusRequest = remember { FocusRequester() }
        val passwordAgainFocusRequest = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(emailState.value,
            passwordState.value,
            passwordAgainState.value,
            userNameState.value) {
            if (registration) {
                emailState.value.trim().isNotEmpty() && passwordState.value.trim()
                    .isNotEmpty() && passwordAgainState.value.trim()
                    .isNotEmpty() && userNameState.value.isNotEmpty()
            } else {
                emailState.value.trim().isNotEmpty() && passwordState.value.trim().isNotEmpty()
            }
        }
        val validForm = remember { mutableStateOf(true) }
        val samePasswords = remember { mutableStateOf(true) }

        EmailInput(emailState = emailState, onAction = KeyboardActions {
            userNameFocusRequest.requestFocus()
        })
        if (registration) {
            InputField(modifier = Modifier.focusRequester(if (registration) userNameFocusRequest else passwordFocusRequest),
                valueState = userNameState,
                label = "Name",
                enabled = true,
                keyboardType = KeyboardType.Text,
                onAction = KeyboardActions {
                    passwordFocusRequest.requestFocus()
                })
            if (!valid && !validForm.value) {
                if (userNameState.value.length < 2) {
                    ErrorMessage(text = "Username must be at least 2 characters long")
                }
            }
        }
        PasswordInput(modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = passwordState,
            label = "Password",
            enabled = true,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
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


        if (!samePasswords.value) {
            Text(text = "Passwords are not the same!",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colors.error)
        }

        SubmitButton(title = if (registration) "Register" else "Login", loading, valid) {
            if (registration) {
                samePasswords.value = passwordState.value == passwordAgainState.value
                validForm.value = emailState.value.contains('@') && passwordState.value.length >= 6
            } else {
                validForm.value = emailState.value.contains('@') && passwordState.value.length >= 6
            }
            if (registration && samePasswords.value && validForm.value) {
                onSubmit(emailState.value, passwordState.value, userNameState.value)
            }
            if (!registration && validForm.value) {
                onSubmit(emailState.value, passwordState.value, "")
            }

        }
        Row(modifier = Modifier.padding(top = 5.dp)) {
            if (registration) {
                Text(text = "Already have account?")
                Text(text = " Sign In",
                    modifier = Modifier.clickable { navController.navigate(OurFridgeScreens.LoginScreen.name) },
                    color = MaterialTheme.colors.secondary)
            } else {
                Text(text = "Your first time using app?")
                Text(text = " Sign Up",
                    modifier = Modifier.clickable { navController.navigate(OurFridgeScreens.RegistrationScreen.name) },
                    color = MaterialTheme.colors.secondary)
            }

        }
        if (!validForm.value && registration) {
            ErrorMessage(text = "Email must include '@' and password must be at least 6 characters!")
        }
        if ((userNotFound || !validForm.value) && !registration) {
            ErrorMessage(text = "Wrong email or password")
        }
        if (emailAlreadyAtUse) {
            ErrorMessage(text = "Account with that email exist!")
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
        if (loading) {
            CircularProgressIndicator(modifier = Modifier, color = MaterialTheme.colors.secondary)
        } else Text(text = title, modifier = Modifier, fontSize = 20.sp)
    }
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    label: String,
    enabled: Boolean,
    passwordReply: Boolean = false,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = label) },
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
            unfocusedLabelColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primaryVariant
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = { if (!passwordReply) PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
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
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    label: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions,
) {
    InputField(modifier = modifier,
        valueState = emailState,
        label = label,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)
}