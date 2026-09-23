package com.ca.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ca.auth.presentation.signin.SignInRoute
import com.ca.auth.presentation.signup.SignUpRoute
import com.ca.navigation.TopLevelDestination
import kotlinx.serialization.Serializable

@Serializable
data object AuthNavGraph : TopLevelDestination {
    @Serializable
    data object SignIn

    @Serializable
    data object SignUp

    @Serializable
    data object ForgotPassword
}

fun NavGraphBuilder.authNavGraph(
    onSignIn: () -> Unit
) {
    navigation<AuthNavGraph>(
        startDestination = AuthNavGraph.SignIn,
    ) {
        composable<AuthNavGraph.SignIn> {
            SignInRoute(
                onSignIn = onSignIn
            )
        }
        composable<AuthNavGraph.SignUp> {
            SignUpRoute(
                onSignIn = onSignIn
            )
        }
    }
}