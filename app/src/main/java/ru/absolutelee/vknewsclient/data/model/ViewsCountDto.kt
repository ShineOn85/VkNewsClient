package ru.absolutelee.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class ViewsCountDto (
    @SerializedName("count") val count: Int
)