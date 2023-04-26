package com.patmy.ourfridge.screens


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.patmy.ourfridge.R
import com.patmy.ourfridge.navigation.OurFridgeScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val animationScale = remember {
            androidx.compose.animation.core.Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            animationScale.animateTo(
                0.9f, animationSpec = tween(durationMillis = 1000, easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                })
            )
            delay(1800L)
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                navController.navigate(OurFridgeScreens.LoginScreen.name)
            } else {
                navController.navigate(OurFridgeScreens.FridgeHomeScreen.name)
            }
        }

        Surface(
            modifier = Modifier
                .size(330.dp)
                .padding(15.dp)
                .scale(animationScale.value),
            shape = CircleShape,
            color = MaterialTheme.colors.primary,
            border = BorderStroke(
                width = 2.dp, color = MaterialTheme.colors.secondary
            )
        ) {
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ourfridge_icon),
                    contentDescription = "app logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = CircleShape),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "OurFridge",
                    modifier = Modifier,
                    color = MaterialTheme.colors.secondary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "\"Your fridge inside out\"",
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}