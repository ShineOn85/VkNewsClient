package ru.absolutelee.vknewsclient.navigation

import android.net.Uri
import com.google.gson.Gson
import ru.absolutelee.vknewsclient.domain.entities.FeedPost

sealed class Screen(
    val route: String
) {

    data object HomeScreen : Screen(ROUTE_HOME)
    data object FavouriteScreen : Screen(ROUTE_FAVOURITE)
    data object ProfileScreen : Screen(ROUTE_PROFILE)
    data object NewsFeedScreen : Screen(ROUTE_NEWS_FEED)
    data object CommentsScreen : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_FOR_ID = "comments_screen"

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            return "$ROUTE_FOR_ID/${feedPostJson.encode()}"
        }
    }

    companion object {

        const val KEY_FEED_POST = "feed_post"

        private const val ROUTE_HOME = "home_screen"
        private const val ROUTE_FAVOURITE = "favourite_screen"
        private const val ROUTE_PROFILE = "profile_screen"
        private const val ROUTE_NEWS_FEED = "news_feed_screen"
        private const val ROUTE_COMMENTS = "comments_screen/{$KEY_FEED_POST}"
    }
}

fun String.encode(): String{
    return Uri.encode(this)
}