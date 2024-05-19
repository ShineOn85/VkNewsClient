package ru.absolutelee.vknewsclient.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.absolutelee.vknewsclient.domain.entities.AuthState
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.NewsFeedResult
import ru.absolutelee.vknewsclient.domain.entities.PostComment

interface NewsFeedRepository {

    fun getAuthState(): StateFlow<AuthState>

    fun getRecommended(): StateFlow<NewsFeedResult>

    fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>>

    suspend fun loadNextRecommended()

    suspend fun checkAuthState()

    suspend fun deletePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)

}