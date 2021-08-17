package com.cloudfiveapp.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cloudfiveapp.android.ui.login.LoginScreen
import com.cloudfiveapp.android.ui.stubs.showcase.ThemeShowcase

@Preview(showSystemUi = true)
@Composable
fun CloudFiveApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController) }
        composable("showcase") { ThemeShowcase() }
    }
}
