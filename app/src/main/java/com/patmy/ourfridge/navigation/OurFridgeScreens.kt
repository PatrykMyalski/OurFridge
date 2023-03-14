package com.patmy.ourfridge.navigation

enum class OurFridgeScreens {
    SplashScreen,
    FridgeHomeScreen,
    LoginScreen,
    RegistrationScreen,
    SocialScreen,
    ShoppingScreen;
    companion object {
        fun fromRoute(route: String?): OurFridgeScreens
        = when(route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            FridgeHomeScreen.name -> FridgeHomeScreen
            LoginScreen.name -> LoginScreen
            RegistrationScreen.name -> RegistrationScreen
            SocialScreen.name -> SocialScreen
            ShoppingScreen.name -> ShoppingScreen
            null -> FridgeHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }

}