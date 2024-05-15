package ru.absolutelee.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class RepostsCountDto (
    @SerializedName("count") val count: Int
)
