package com.aliernfrog.ensicord.util.extension

import androidx.navigation.NavHostController

fun NavHostController.popBackStackSafe() {
    if (previousBackStackEntry != null) popBackStack()
}