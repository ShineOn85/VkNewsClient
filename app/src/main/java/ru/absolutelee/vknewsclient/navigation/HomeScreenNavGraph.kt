package ru.absolutelee.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.navigation.Screen.Companion.KEY_FEED_POST


fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    navigation(
        startDestination = Screen.NewsFeedScreen.route,
        route = Screen.HomeScreen.route
    ) {
        composable(route = Screen.NewsFeedScreen.route) {
            newsFeedScreenContent()
        }
        composable(
            route = Screen.CommentsScreen.route,
            arguments = listOf(
                navArgument(KEY_FEED_POST) {
                    type = NavType.StringType
                },
            )
        ) {
            val feedPostJson = it.arguments?.getString(KEY_FEED_POST) ?: ""
            val feedPost = Gson().fromJson(feedPostJson, FeedPost::class.java)
            commentsScreenContent(feedPost)
        }
    }
}