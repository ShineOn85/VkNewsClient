package ru.absolutelee.vknewsclient.domain.entities

sealed class AuthState(){

    object Initial: AuthState()
    object Authorized: AuthState()
    object Unauthorized: AuthState()

}