package com.patmy.ourfridge.navigation

enum class OurFridgeScreens {
    SplashScreen,
    FridgeHomeScreen,
    LoginScreen,
    RegistrationScreen,
    FridgeScreen;
    companion object {
        fun fromRoute(route: String?): OurFridgeScreens
        = when(route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            FridgeHomeScreen.name -> FridgeHomeScreen
            LoginScreen.name -> LoginScreen
            RegistrationScreen.name -> RegistrationScreen
            FridgeScreen.name -> FridgeScreen
            null -> FridgeHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }

}