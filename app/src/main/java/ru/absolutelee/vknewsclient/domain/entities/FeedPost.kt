package ru.absolutelee.vknewsclient.domain.entities


data class FeedPost(
    val id: Long,
    val ownerId: Long,
    val communityName: String,
    val publicationData: String,
    val communityThumbnailUrl: String,
    val contentText: String,
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isLiked: Boolean
)
