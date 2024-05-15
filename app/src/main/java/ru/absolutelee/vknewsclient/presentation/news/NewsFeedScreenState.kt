package ru.absolutelee.vknewsclient.presentation.news

import ru.absolutelee.vknewsclient.domain.entities.FeedPost

sealed class NewsFeedScreenState {

    data object Initial: NewsFeedScreenState()

    data object Loading: NewsFeedScreenState()

    data class Error(
        val errorMessage: String
    ): NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val isLoading: Boolean = false
    ): NewsFeedScreenState()

}