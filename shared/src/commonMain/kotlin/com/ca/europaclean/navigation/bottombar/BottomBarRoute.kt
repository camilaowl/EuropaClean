package com.ca.europaclean.navigation.bottombar

import kotlinx.serialization.Serializable

@Serializable
sealed interface BottomBarRoute {
    @Serializable data object Schedule : BottomBarRoute
    @Serializable data object MyHome : BottomBarRoute
    @Serializable data object Settings : BottomBarRoute
}