package com.example.composeapp.presentation.feed.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.presentation.Screen
import com.example.composeapp.presentation.feed.PostFeedViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<PostFeedViewModel>()
    // change system bar colors
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.White,
        darkIcons = true
    )

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreenRoute
    ){
        composable(
            route = Screen.HomeScreenRoute
        ){
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.CommentsScreenRoute
        ){
            CommentsScreen(navController = navController, viewModel = viewModel)
        }
    }
}