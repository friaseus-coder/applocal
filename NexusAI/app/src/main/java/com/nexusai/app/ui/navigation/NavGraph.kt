package com.nexusai.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexusai.app.ui.screen.chat.ChatScreen
import com.nexusai.app.ui.screen.memory.MemoryScreen
import com.nexusai.app.ui.screen.persona.PersonaSelectionScreen

object Routes {
    const val PERSONA_SELECTION = "persona_selection"
    const val CHAT = "chat/{perfilId}"
    const val MEMORY = "memory"

    fun chatRoute(perfilId: Long) = "chat/$perfilId"
}

@Composable
fun NexusNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.PERSONA_SELECTION,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(200))
        }
    ) {
        composable(Routes.PERSONA_SELECTION) {
            PersonaSelectionScreen(
                onPersonaSelected = { perfilId ->
                    navController.navigate(Routes.chatRoute(perfilId)) {
                        popUpTo(Routes.PERSONA_SELECTION) { inclusive = false }
                    }
                }
            )
        }

        composable(Routes.CHAT) { backStackEntry ->
            val perfilId = backStackEntry.arguments?.getString("perfilId")?.toLongOrNull() ?: return@composable
            ChatScreen(
                perfilId = perfilId,
                onNavigateToMemory = {
                    navController.navigate(Routes.MEMORY)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MEMORY) {
            MemoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
