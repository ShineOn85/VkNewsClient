package ru.absolutelee.vknewsclient.domain.entities

sealed class NewsFeedResult {
    
    data class Error(val message: String): NewsFeedResult()

    data class Success(val posts: List<FeedPost>): NewsFeedResult()

}