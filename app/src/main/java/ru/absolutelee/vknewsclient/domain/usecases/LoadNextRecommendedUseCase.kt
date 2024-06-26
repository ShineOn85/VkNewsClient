package ru.absolutelee.vknewsclient.domain.usecases

import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class LoadNextRecommendedUseCase @Inject constructor(private val repository: NewsFeedRepository) {
    suspend operator fun invoke() {
        return repository.loadNextRecommended()
    }
}