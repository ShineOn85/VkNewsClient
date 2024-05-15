package ru.absolutelee.vknewsclient.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.absolutelee.vknewsclient.domain.entities.AuthState
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(private val repository: NewsFeedRepository) {
    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthState()
    }
}