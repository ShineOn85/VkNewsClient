package ru.absolutelee.vknewsclient.data.mapper

import ru.absolutelee.vknewsclient.data.model.CommentsResponseDto
import ru.absolutelee.vknewsclient.data.model.NewsFeedResponseDto
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.PostComment
import ru.absolutelee.vknewsclient.domain.entities.StatisticItem
import ru.absolutelee.vknewsclient.domain.entities.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue


fun CommentsResponseDto.mapToComments(): List<PostComment> {
    val result = mutableListOf<PostComment>()

    val comments = this.response.comments
    val profiles = this.response.profiles

    for (comment in comments) {
        val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
        val postComment = PostComment(
            id = comment.id,
            authorName = "${author.firstName} ${author.lastName}",
            publicationDate = comment.date.mapToDate(),
            authorAvatarUrl = author.avatarUrl,
            commentText = comment.text
        )
        result.add(postComment)
    }
    return result
}

fun NewsFeedResponseDto.mapToListFeedPostEntity(): List<FeedPost> {
    val result = mutableListOf<FeedPost>()

    val posts = this.newsFeedContent.posts
    val groups = this.newsFeedContent.groups

    for (post in posts) {
        val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
        val feedPost = FeedPost(
            id = post.id,
            ownerId = post.communityId,
            communityName = group.name,
            publicationData = (post.date).mapToDate(),
            communityThumbnailUrl = group.imageUrl,
            contentText = post.text,
            contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
            isLiked = post.likes.userLikes > 0,
            statistics = listOf(
                StatisticItem(
                    type = StatisticType.LIKES,
                    count = post.likes.count
                ),
                StatisticItem(
                    type = StatisticType.SHARES,
                    count = post.reposts.count
                ),
                StatisticItem(
                    type = StatisticType.COMMENTS,
                    count = post.comments.count
                ),
                StatisticItem(
                    type = StatisticType.VIEWS,
                    count = post.views.count
                )
            )
        )

        result.add(feedPost)
    }
    return result
}

private fun Long.mapToDate(): String {
    val date = Date(this * 1000)
    return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
}