package ru.absolutelee.vknewsclient.data.model

import com.google.gson.annotations.SerializedName


data class PostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("comments") val comments: CommentsCountDto,
    @SerializedName("views") val views: ViewsCountDto,
    @SerializedName("reposts") val reposts: RepostsCountDto,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?
)