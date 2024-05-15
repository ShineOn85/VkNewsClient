package ru.absolutelee.vknewsclient.domain.usecases

import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository

class LoadNextRecommendedUseCase(private val repository: NewsFeedRepository) {
    suspend operator fun invoke() {
        return repository.loadNextRecommended()
    }
}