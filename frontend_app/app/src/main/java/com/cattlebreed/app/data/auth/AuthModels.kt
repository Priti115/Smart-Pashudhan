package com.cattlebreed.app.data.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * User authentication data model
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val phoneNumber: String,
    val isVerified: Boolean = false,
    val registrationDate: Date = Date(),
    val lastLoginDate: Date? = null,
    val preferredLanguage: String = "en", // Default to English
    val farmName: String? = null,
    val farmerName: String? = null,
    val location: String? = null,
    val syncEnabled: Boolean = true
)

/**
 * OTP session data model for temporary storage
 */
data class OTPSession(
    val phoneNumber: String,
    val otp: String,
    val expiryTime: Long, // Timestamp when OTP expires
    val attempts: Int = 0,
    val isUsed: Boolean = false
)

/**
 * Authentication state
 */
sealed class AuthState {
    object Unauthenticated : AuthState()
    object PhoneNumberEntry : AuthState()
    data class OTPPending(val phoneNumber: String, val maskedNumber: String) : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Guest : AuthState()  // For users who skip login
    data class Error(val message: String) : AuthState()
}

/**
 * API request/response models for web integration
 */
data class SendOTPRequest(
    val phoneNumber: String,
    val countryCode: String = "+91"
)

data class VerifyOTPRequest(
    val phoneNumber: String,
    val otp: String,
    val deviceId: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserProfile? = null
)

data class UserProfile(
    val phoneNumber: String,
    val farmName: String?,
    val farmerName: String?,
    val location: String?,
    val preferredLanguage: String,
    val isVerified: Boolean
)