package ru.absolutelee.vknewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import ru.absolutelee.vknewsclient.domain.usecases.GetCommentsUseCase

@HiltViewModel (assistedFactory = CommentsViewModel.CommentsViewModelFactory::class)
class CommentsViewModel @AssistedInject constructor (
    @Assisted private val feedPost: FeedPost,
    private val repository: NewsFeedRepository
) : ViewModel() {

    @AssistedFactory
    interface CommentsViewModelFactory {
        fun create(feedPost: FeedPost): CommentsViewModel
    }


    private val getComments = GetCommentsUseCase(repository)

    val state = getComments(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
}