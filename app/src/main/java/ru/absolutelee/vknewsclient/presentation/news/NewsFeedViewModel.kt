package ru.absolutelee.vknewsclient.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.absolutelee.vknewsclient.data.repository.NewsFeedRepositoryImpl
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import ru.absolutelee.vknewsclient.domain.usecases.DeletePostUseCase
import ru.absolutelee.vknewsclient.domain.usecases.GetRecommendedUseCase
import ru.absolutelee.vknewsclient.domain.usecases.LoadNextRecommendedUseCase
import ru.absolutelee.vknewsclient.presentation.mergeWith

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->

    }

    private val repository = NewsFeedRepositoryImpl(application)

    private val getRecommended = GetRecommendedUseCase(repository)
    private val deletePost = DeletePostUseCase(repository)
    private val loadNextRecommendedPosts = LoadNextRecommendedUseCase(repository)
    private val changePostLikeStatus = ChangeLikeStatusUseCase(repository)

    private val recommendedFlow = getRecommended()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    private val loadingErrorFlow = repository.loadingErrorFlow.map {
        NewsFeedScreenState.Error(it)
    }

    val state = recommendedFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)
        .mergeWith(loadingErrorFlow)


    fun loadNextRecommended() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = recommendedFlow.value,
                    isLoading = true
                )
            )
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