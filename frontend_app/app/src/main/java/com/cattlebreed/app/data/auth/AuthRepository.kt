package com.cattlebreed.app.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.SecureRandom
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Repository for handling authentication operations
 * Includes both local (demo/testing) and remote (web API) authentication
 */
class AuthRepository(
    private val context: Context,
    private val preferences: SharedPreferences
) {
    
    companion object {
        private const val PREF_CURRENT_USER = "current_user_phone"
        private const val PREF_AUTH_TOKEN = "auth_token"
        private const val PREF_LANGUAGE = "preferred_language"
        private const val OTP_LENGTH = 6
        private const val OTP_EXPIRY_MINUTES = 5L
        private const val MAX_OTP_ATTEMPTS = 3
    }
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    // In-memory OTP sessions for demo purposes
    // In production, this would be handled by backend
    private val otpSessions = mutableMapOf<String, OTPSession>()
    
    init {
        // Check if user is already authenticated or in guest mode
        val currentPhone = preferences.getString(PREF_CURRENT_USER, null)
        val isGuest = preferences.getBoolean("is_guest_mode", false)
        
        when {
            currentPhone != null -> {
                val user = getUserFromStorage(currentPhone)
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                }
            }
            isGuest -> {
                _authState.value = AuthState.Guest
            }
            else -> {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    
    /**
     * Send OTP to phone number
     * In production, this would call the web API
     */
    suspend fun sendOTP(phoneNumber: String): Result<String> {
        return try {
            // Validate phone number format
            if (!isValidPhoneNumber(phoneNumber)) {
                return Result.failure(Exception("Invalid phone number format"))
            }
            
            // Generate OTP
            val otp = generateOTP()
            val expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(OTP_EXPIRY_MINUTES)
            
            // Store OTP session
            otpSessions[phoneNumber] = OTPSession(
                phoneNumber = phoneNumber,
                otp = otp,
                expiryTime = expiryTime
            )
            
            // In production, send actual SMS here
            // For demo, we'll just log the OTP
            println("Demo OTP for $phoneNumber: $otp")
            
            // Simulate network delay
            delay(1000)
            
            val maskedNumber = maskPhoneNumber(phoneNumber)
            _authState.value = AuthState.OTPPending(phoneNumber, maskedNumber)
            
            Result.success("OTP sent to $maskedNumber")
            
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Failed to send OTP: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Verify OTP and authenticate user
     */
    suspend fun verifyOTP(phoneNumber: String, enteredOTP: String): Result<User> {
        return try {
            val session = otpSessions[phoneNumber]
                ?: return Result.failure(Exception("No OTP session found"))
            
            // Check if OTP is expired
            if (System.currentTimeMillis() > session.expiryTime) {
                otpSessions.remove(phoneNumber)
                return Result.failure(Exception("OTP has expired"))
            }
            
            // Check if OTP is already used
            if (session.isUsed) {
                return Result.failure(Exception("OTP already used"))
            }
            
            // Check attempts
            if (session.attempts >= MAX_OTP_ATTEMPTS) {
                otpSessions.remove(phoneNumber)
                return Result.failure(Exception("Too many failed attempts"))
            }
            
            // Verify OTP
            if (session.otp != enteredOTP) {
                otpSessions[phoneNumber] = session.copy(attempts = session.attempts + 1)
                return Result.failure(Exception("Invalid OTP"))
            }
            
            // OTP verified successfully
            otpSessions[phoneNumber] = session.copy(isUsed = true)
            
            // Create or get user
            val user = createOrGetUser(phoneNumber)
            
            // Save authentication state
            saveUserSession(user)
            
            _authState.value = AuthState.Authenticated(user)
            
            // Clean up OTP session
            otpSessions.remove(phoneNumber)
            
            Result.success(user)
            
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Verification failed: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Set guest mode
     */
    fun setGuestMode() {
        preferences.edit()
            .remove(PREF_CURRENT_USER)
            .remove(PREF_AUTH_TOKEN)
            .putBoolean("is_guest_mode", true)
            .apply()
        
        _authState.value = AuthState.Guest
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        preferences.edit()
            .remove(PREF_CURRENT_USER)
            .remove(PREF_AUTH_TOKEN)
            .remove("is_guest_mode")
            .apply()
        
        _authState.value = AuthState.Unauthenticated
    }
    
    /**
     * Get current authenticated user
     */
    fun getCurrentUser(): User? {
        val currentPhone = preferences.getString(PREF_CURRENT_USER, null)
        return if (currentPhone != null) {
            getUserFromStorage(currentPhone)
        } else null
    }
    
    /**
     * Update user profile
     */
    fun updateUserProfile(user: User): User {
        saveUserToStorage(user)
        _authState.value = AuthState.Authenticated(user)
        return user
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return _authState.value is AuthState.Authenticated
    }
    
    /**
     * Check if user is in guest mode
     */
    fun isGuestMode(): Boolean {
        return _authState.value is AuthState.Guest
    }
    
    // Private helper methods
    
    private fun generateOTP(): String {
        val random = SecureRandom()
        val otp = StringBuilder()
        repeat(OTP_LENGTH) {
            otp.append(random.nextInt(10))
        }
        return otp.toString()
    }
    
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Basic Indian phone number validation
        val cleaned = phoneNumber.replace(Regex("[^\\d]"), "")
        return cleaned.length == 10 && cleaned.matches(Regex("[6-9]\\d{9}"))
    }
    
    private fun maskPhoneNumber(phoneNumber: String): String {
        val cleaned = phoneNumber.replace(Regex("[^\\d]"), "")
        return if (cleaned.length >= 4) {
            "*".repeat(cleaned.length - 4) + cleaned.takeLast(4)
        } else phoneNumber
    }
    
    private fun createOrGetUser(phoneNumber: String): User {
        val existingUser = getUserFromStorage(phoneNumber)
        return existingUser ?: User(
            phoneNumber = phoneNumber,
            isVerified = true,
            lastLoginDate = Date(),
            preferredLanguage = preferences.getString(PREF_LANGUAGE, "en") ?: "en"
        ).also { saveUserToStorage(it) }
    }
    
    private fun saveUserSession(user: User) {
        preferences.edit()
            .putString(PREF_CURRENT_USER, user.phoneNumber)
            .putString(PREF_AUTH_TOKEN, generateSessionToken())
            .remove("is_guest_mode")  // Clear guest mode when user authenticates
            .apply()
    }
    
    private fun generateSessionToken(): String {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return "$deviceId-${System.currentTimeMillis()}"
    }
    
    private fun getUserFromStorage(phoneNumber: String): User? {
        // In a real app, this would query the database
        // For now, create a basic user profile
        return User(
            phoneNumber = phoneNumber,
            isVerified = true,
            lastLoginDate = Date(),
            preferredLanguage = preferences.getString(PREF_LANGUAGE, "en") ?: "en"
        )
    }
    
    private fun saveUserToStorage(user: User) {
        // In a real app, this would save to database
        // For now, just update preferences
        preferences.edit()
            .putString(PREF_LANGUAGE, user.preferredLanguage)
            .apply()
    }
}