package com.patmy.ourfridge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.patmy.ourfridge.screens.home.FridgeHomeScreen
import com.patmy.ourfridge.screens.login.LoginScreen
import com.patmy.ourfridge.screens.registration.RegistrationScreen
import com.patmy.ourfridge.screens.social.SocialScreen
import com.patmy.ourfridge.screens.splash.SplashScreen

@Composable
fun OurFridgeNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = OurFridgeScreens.SplashScreen.name){
        composable(OurFridgeScreens.SocialScreen.name){
            SocialScreen(navController = navController)
        }
        composable(OurFridgeScreens.FridgeHomeScreen.name){
            FridgeHomeScreen(navController = navController)
        }
        composable(OurFridgeScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable(OurFridgeScreens.RegistrationScreen.name){
            RegistrationScreen(navController = navController)
        }
        composable(OurFridgeScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
    }
}