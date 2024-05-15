package ru.absolutelee.vknewsclient.domain.entities

data class PostComment (
    val id: Long,
    val authorName: String,
    val publicationDate: String,
    val authorAvatarUrl: String,
    val commentText: String
)