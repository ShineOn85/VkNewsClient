package ru.absolutelee.vknewsclient.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.absolutelee.vknewsclient.data.repository.NewsFeedRepositoryImpl
import ru.absolutelee.vknewsclient.domain.usecases.CheckAuthStateUseCase
import ru.absolutelee.vknewsclient.domain.usecases.GetAuthStateUseCase

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepositoryImpl(application)

    private val checkAuthState = CheckAuthStateUseCase(repository)
    private val getAuthState = GetAuthStateUseCase(repository)

    val authState = getAuthState()

    fun performAuthResult() {
        viewModelScope.launch {
            checkAuthState()
        }
    }
}