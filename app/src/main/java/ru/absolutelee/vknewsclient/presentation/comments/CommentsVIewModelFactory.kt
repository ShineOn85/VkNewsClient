package ru.absolutelee.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.absolutelee.vknewsclient.domain.entities.FeedPost

class CommentsVIewModelFactory(
    val feedPost: FeedPost,
    val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost = feedPost, application = application) as T
    }
}