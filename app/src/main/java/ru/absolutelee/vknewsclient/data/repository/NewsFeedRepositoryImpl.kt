package ru.absolutelee.vknewsclient.data.repository

import android.app.Application
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import ru.absolutelee.vknewsclient.data.mapper.mapToComments
import ru.absolutelee.vknewsclient.data.mapper.mapToListFeedPostEntity
import ru.absolutelee.vknewsclient.data.network.ApiFactory
import ru.absolutelee.vknewsclient.domain.entities.AuthState
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.StatisticItem
import ru.absolutelee.vknewsclient.domain.entities.StatisticType
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import ru.absolutelee.vknewsclient.presentation.mergeWith

class NewsFeedRepositoryImpl(application: Application) : NewsFeedRepository {

    private val loadNextFeedPostsEvent = MutableSharedFlow<Unit>(replay = 1)
    private val loadRecommendedFlow = MutableSharedFlow<List<FeedPost>>()

    private val _loadingErrorFlow = MutableSharedFlow<String>()
    val loadingErrorFlow = _loadingErrorFlow.asSharedFlow()


    private val feedPostsFlow = flow {
        loadNextRecommended()
        loadNextFeedPostsEvent.collect {
            val startFrom = nextFrom

            if (startFrom == null && newsFeed.isNotEmpty()) {
                emit(newsFeed)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommended(token.accessToken)
            } else {
                apiService.loadRecommended(token.accessToken, startFrom)
            }
            nextFrom = response.newsFeedContent.nextFrom
            val posts = response.mapToListFeedPostEntity()
            _newsFeed.addAll(posts)
            emit(newsFeed)
        }
    }.retry(3) {
        delay(3000L)
        true
    }.catch {
        _loadingErrorFlow.emit(it.message ?: "Что-то пошло не так")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token
        get() = VKAccessToken.restore(storage) ?: throw IllegalStateException("Token is null")

    private val apiService = ApiFactory.apiService

    private val checkAuthStateEvent = MutableSharedFlow<Unit>(replay = 1)

    override fun getAuthState() = flow {
        checkAuthStateEvent.emit(Unit)
        checkAuthStateEvent.collect{
            val loggedIn = token.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.Unauthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    private val _newsFeed = mutableListOf<FeedPost>()
    private val newsFeed: List<FeedPost>
        get() = _newsFeed.toList()

    private var nextFrom: String? = null

    override fun getRecommended(): StateFlow<List<FeedPost>> =
        feedPostsFlow.mergeWith(loadRecommendedFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = newsFeed
        )


    override suspend fun loadNextRecommended() {
        loadNextFeedPostsEvent.emit(Unit)
    }

    override suspend fun checkAuthState(){
        checkAuthStateEvent.emit(Unit)
    }

    override fun getComments(feedPost: FeedPost) = flow {
        val commentsDto = apiService.getComments(
            token = token.accessToken,
            ownerId = feedPost.ownerId,
            itemId = feedPost.id
        )
        emit(commentsDto.mapToComments())
    }.retry(3) {
        delay(3000L)
        true
    }.catch {

    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = token.accessToken,
            ownerId = feedPost.ownerId,
            itemId = feedPost.id
        )
        _newsFeed.remove(feedPost)
        loadRecommendedFlow.emit(newsFeed)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) apiService.deleteLike(
            token = token.accessToken,
            ownerId = feedPost.ownerId,
            itemId = feedPost.id
        ) else {
            apiService.addLike(
                token = token.accessToken,
                ownerId = feedPost.ownerId,
                itemId = feedPost.id
            )
        }
        val newCountLikes = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(StatisticType.LIKES, newCountLikes))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = newsFeed.indexOf(feedPost)

        _newsFeed[postIndex] = newPost
        loadRecommendedFlow.emit(newsFeed)
    }

}