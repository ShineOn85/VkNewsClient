package ru.absolutelee.vknewsclient.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

fun Int.formatStatisticCount(): String {
    return if (this >= 100_000) {
        String.format("%sK", (this / 1000))
    } else if (this >= 1000) {
        String.format("%.1fK", (this / 1000f))
    } else {
        this.toString()
    }
}

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}