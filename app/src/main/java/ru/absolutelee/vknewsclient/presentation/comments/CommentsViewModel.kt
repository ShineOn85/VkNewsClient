package ru.absolutelee.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.absolutelee.vknewsclient.data.repository.NewsFeedRepositoryImpl
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.usecases.GetCommentsUseCase

class CommentsViewModel(feedPost: FeedPost, application: Application) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl(application)

    private val getComments = GetCommentsUseCase(repository)


    val state = getComments(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
}