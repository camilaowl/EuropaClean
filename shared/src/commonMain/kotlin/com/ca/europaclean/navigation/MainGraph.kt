package com.ca.europaclean.navigation

import com.ca.navigation.TopLevelDestination
import kotlinx.serialization.Serializable

@Serializable
sealed class MainGraph : TopLevelDestination {
    @Serializable
    data object Schedule : MainGraph()

    @Serializable
    data object MyHome : MainGraph()

    @Serializable
    data object Settings : MainGraph()
}
