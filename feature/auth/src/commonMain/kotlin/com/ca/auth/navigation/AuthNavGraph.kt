package com.ca.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ca.auth.presentation.signin.SignInRoute
import com.ca.auth.presentation.signup.SignUpRoute
import com.ca.navigation.TopLevelDestination
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthNavGraph : TopLevelDestination {
    @Serializable
    data object SignIn : AuthNavGraph()

    @Serializable
    data object SignUp : AuthNavGraph()

    @Serializable
    data object ForgotPassword : AuthNavGraph()
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