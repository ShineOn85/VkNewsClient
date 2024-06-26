package ru.absolutelee.vknewsclient.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.PostComment
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(private val repository: NewsFeedRepository) {
    operator fun invoke(feedPost: FeedPost): StateFlow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}