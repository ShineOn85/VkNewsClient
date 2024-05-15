package ru.absolutelee.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentsCountDto (
    @SerializedName("count") val count: Int
)