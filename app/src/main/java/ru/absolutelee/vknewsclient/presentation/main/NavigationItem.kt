package ru.absolutelee.vknewsclient.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import ru.absolutelee.vknewsclient.R
import ru.absolutelee.vknewsclient.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
) {

    data object Home : NavigationItem(
        screen = Screen.HomeScreen,
        R.string.navigation_item_home,
        Icons.Outlined.Home
    )

    data object Favourite : NavigationItem(
        screen = Screen.FavouriteScreen,
        R.string.navigation_item_favourite,
        Icons.Outlined.Favorite
    )

    data object Profile : NavigationItem(
        screen = Screen.ProfileScreen,
        R.string.navigation_item_profile,
        Icons.Outlined.Person
    )
}