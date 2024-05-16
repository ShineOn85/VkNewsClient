package ru.absolutelee.vknewsclient.presentation.main

import android.os.Bundle
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
            VkNewsClientTheme(dynamicColor = false) {
                val viewModel: MainViewModel = hiltViewModel()
                val authState = viewModel.authState.collectAsState(AuthState.Initial)

                val launcher =
                    rememberLauncherForActivityResult(contract = VK.getVKAuthActivityResultContract()) {
                        viewModel.performAuthResult()
                    }

                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
                    }

                    is AuthState.Unauthorized -> {
                        LoginScreen(
                            onLoginClick = {
                                launcher.launch(listOf(VKScope.WALL,VKScope.FRIENDS))
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


