package ru.absolutelee.vknewsclient.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.absolutelee.vknewsclient.R


@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.vk_logo), contentDescription = null)
        Button(onClick = {
            onLoginClick()
        }
        ) {
            Text(text = "Войти")
        }
    }
}