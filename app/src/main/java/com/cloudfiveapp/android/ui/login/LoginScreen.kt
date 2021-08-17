package com.cloudfiveapp.android.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun LoginScreen(
    // TODO: Remove this default parameter value with @PreviewParameter somehow?
    navController: NavController = rememberNavController(),
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text("Hello Login")
                Button(
                    onClick = {
                        viewModel.signIn(email = "alex@10fw.net", password = "password") {
                            navController.navigate("showcase")
                        }
                    }
                ) {
                    Text("Login")
                }
            }
        }
    }
}
