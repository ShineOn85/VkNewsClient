package ru.absolutelee.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.AndroidEntryPoint
import ru.absolutelee.vknewsclient.domain.entities.AuthState
import ru.absolutelee.vknewsclient.ui.theme.VkNewsClientTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: MainViewModel = hiltViewModel()
            val authState = viewModel.authState.collectAsState(AuthState.Initial)

            val launcher =
                rememberLauncherForActivityResult(contract = VK.getVKAuthActivityResultContract()) {
                    viewModel.performAuthResult()
                }

            VkNewsClientTheme(dynamicColor = false) {
                when (authState.value) {
                    is AuthState.Authorized -> {
                        Log.d("Bebra", "auto")
                        MainScreen()
                    }

                    is AuthState.Unauthorized -> {
                        Log.d("Bebra", "Noauto")
                        LoginScreen(
                            onLoginClick = {
                                launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                            }
                        )
                    }

                    AuthState.Initial -> {

                    }
                }
            }
        }
    }
}


