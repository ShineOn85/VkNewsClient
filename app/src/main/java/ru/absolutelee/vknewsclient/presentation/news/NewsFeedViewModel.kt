package ru.absolutelee.vknewsclient.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.NewsFeedResult
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import ru.absolutelee.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import ru.absolutelee.vknewsclient.domain.usecases.DeletePostUseCase
import ru.absolutelee.vknewsclient.domain.usecases.GetRecommendedUseCase
import ru.absolutelee.vknewsclient.domain.usecases.LoadNextRecommendedUseCase
import ru.absolutelee.vknewsclient.presentation.mergeWith
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(repository: NewsFeedRepository) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->

    }


    private val getRecommended = GetRecommendedUseCase(repository)
    private val deletePost = DeletePostUseCase(repository)
    private val loadNextRecommendedPosts = LoadNextRecommendedUseCase(repository)
    private val changePostLikeStatus = ChangeLikeStatusUseCase(repository)

    private val recommendedFlow = getRecommended()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()


    val state = recommendedFlow
        .map {
            when (it) {
                is NewsFeedResult.Success -> {
                    NewsFeedScreenState.Posts(it.posts) as NewsFeedScreenState
                }

                is NewsFeedResult.Error -> {
                    NewsFeedScreenState.Error(it.message)
                }
            }
        }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)


    fun loadNextRecommended() {
        viewModelScope.launch {
            when (val response = recommendedFlow.value) {
                is NewsFeedResult.Success -> {
                    loadNextDataFlow.emit(
                        NewsFeedScreenState.Posts(
                            posts = response.posts,
                            isLoading = true
                        )
                    )
                }

                is NewsFeedResult.Error -> {
                    loadNextDataFlow.emit(
                        NewsFeedScreenState.Error(
                            errorMessage = response.message
                        )
                    )
                }
            }

            loadNextRecommendedPosts()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(context = exceptionHandler) {
            changePostLikeStatus(feedPost)
        }
    }

    fun remove(feedPost: FeedPost) {
        viewModelScope.launch(context = exceptionHandler) {
            deletePost(feedPost)
        }
    }
}