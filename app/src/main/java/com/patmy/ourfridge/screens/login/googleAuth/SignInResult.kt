package com.patmy.ourfridge.screens.login.googleAuth

data class SignInResult(
    val data: UserData?,
    val errorMassage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)