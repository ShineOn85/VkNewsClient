package ru.absolutelee.vknewsclient.domain.usecases

import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository

class CheckAuthStateUseCase(private val repository: NewsFeedRepository) {
    suspend operator fun invoke() {
        return repository.checkAuthState()
    }
}