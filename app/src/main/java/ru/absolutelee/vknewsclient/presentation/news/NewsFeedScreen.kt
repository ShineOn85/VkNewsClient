package ru.absolutelee.vknewsclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.ui.theme.DarkBlue

@Composable
fun NewsFeedScreen(onCommentsClickListener: (FeedPost) -> Unit) {

    val viewModel: NewsFeedViewModel = viewModel()

    val state = viewModel.state.collectAsState(NewsFeedScreenState.Initial)

    when (val currentState = state.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                feedPosts = currentState.posts,
                viewModel = viewModel,
                onCommentsClickListener = onCommentsClickListener,
                isLoading = currentState.isLoading
            )
        }


        NewsFeedScreenState.Initial -> {
        }

        NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }

        is NewsFeedScreenState.Error -> {
            Text(text = currentState.errorMessage)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun FeedPosts(
    feedPosts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    onCommentsClickListener: (FeedPost) -> Unit,
    isLoading: Boolean
) {

    LazyColumn(
        contentPadding = PaddingValues(
            top = 2.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = feedPosts,
            key = { it.id }
        ) { feedPost ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                viewModel.remove(feedPost)
            }
            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                directions = setOf(DismissDirection.EndToStart),
                state = dismissState,
                background = { },
                dismissContent = {
                    PostCard(
                        feedPost = feedPost,
                        onSharesItemClick = { statisticItem ->

                        },
                        onCommentsItemClick = {
                            onCommentsClickListener.invoke(feedPost)
                        },
                        onLikesItemClick = {
                            viewModel.changeLikeStatus(feedPost)
                        }
                    )
                }
            )
        }
        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(12.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    viewModel.loadNextRecommended()
                }
            }
        }
    }
}