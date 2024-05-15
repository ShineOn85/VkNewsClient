package ru.absolutelee.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.absolutelee.vknewsclient.domain.usecases.CheckAuthStateUseCase
import ru.absolutelee.vknewsclient.domain.usecases.GetAuthStateUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val checkAuthState: CheckAuthStateUseCase,
    private val getAuthState: GetAuthStateUseCase
) : ViewModel() {

    val authState = getAuthState()

    fun performAuthResult() {
        viewModelScope.launch {
            checkAuthState()
        }
    }
}