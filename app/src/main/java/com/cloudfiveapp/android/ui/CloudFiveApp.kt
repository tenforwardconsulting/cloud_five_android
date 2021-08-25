package com.cloudfiveapp.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cloudfiveapp.android.ui.login.LoginScreen
import com.cloudfiveapp.android.ui.login.LoginViewModel
import com.cloudfiveapp.android.ui.stubs.showcase.ThemeShowcase

@Preview(showSystemUi = true)
@Composable
fun CloudFiveApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(onLoginClick = { email, password ->
                viewModel.signIn(email = email, password = password) {
                    navController.navigate("showcase")
                }
            })
        }
        composable("showcase") { ThemeShowcase() }
    }
}
