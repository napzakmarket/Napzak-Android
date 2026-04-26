package com.napzak.market.navigation.util

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

fun defaultTransitionSpec(): ContentTransform =
    slideInHorizontally(initialOffsetX = { it }) togetherWith
            slideOutHorizontally(targetOffsetX = { -it })

fun defaultPopTransitionSpec(): ContentTransform =
    slideInHorizontally(initialOffsetX = { -it }) togetherWith
            slideOutHorizontally(targetOffsetX = { it })

fun defaultPredictivePopTransitionSpec(): ContentTransform =
    slideInHorizontally(initialOffsetX = { -it }) togetherWith
            slideOutHorizontally(targetOffsetX = { it })
