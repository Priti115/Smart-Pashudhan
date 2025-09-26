package com.cattlebreed.app.navigation

enum class NavigationDestination(val route: String) {
    // Authentication
    PHONE_INPUT("phone_input"),
    OTP_VERIFICATION("otp_verification/{phoneNumber}"),
    
    // Main app
    HOME("home"),
    CAPTURE("capture"),
    HISTORY("history"),
    EXPORT("export"),
    SETTINGS("settings"),
    ANIMAL_DETAIL("animal_detail/{animalId}")
}

object NavigationArguments {
    const val ANIMAL_ID = "animalId"
    const val PHONE_NUMBER = "phoneNumber"
}

fun NavigationDestination.withArgs(vararg args: Any): String {
    return buildString {
        append(route)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}