package ru.absolutelee.vknewsclient.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.NewsFeedResult
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetRecommendedUseCase @Inject constructor(private val repository: NewsFeedRepository) {
    operator fun invoke(): StateFlow<NewsFeedResult> {
        return repository.getRecommended()
    }
}