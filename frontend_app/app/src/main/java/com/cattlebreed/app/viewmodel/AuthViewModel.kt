package com.cattlebreed.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cattlebreed.app.data.auth.AuthRepository
import com.cattlebreed.app.data.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        // Check if user is already authenticated on app start
        checkAuthenticationStatus()
    }
    
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                val isAuthenticated = authRepository.isAuthenticated()
                if (isAuthenticated) {
                    val userInfo = authRepository.getCurrentUser()
                    if (userInfo != null) {
                        _authState.value = AuthState.Authenticated(userInfo)
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error checking auth status", e)
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    
    fun sendOTP(phoneNumber: String) {
        if (!isValidPhoneNumber(phoneNumber)) {
            _authState.value = AuthState.Error("Invalid phone number format")
            return
        }
        
        viewModelScope.launch {
            try {
                _authState.value = AuthState.PhoneNumberEntry
                
                // Call auth repository to send OTP
                val result = authRepository.sendOTP(phoneNumber)
                
                if (result.isSuccess) {
                    // AuthRepository handles setting OTPPending state
                } else {
                    _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Failed to send OTP. Please try again.")
                }
                
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error sending OTP", e)
                _authState.value = AuthState.Error("Network error. Please check your connection.")
            }
        }
    }
    
    fun verifyOTP(phoneNumber: String, otpCode: String) {
        if (otpCode.length != 6 || !otpCode.all { it.isDigit() }) {
            _authState.value = AuthState.Error("Invalid OTP format")
            return
        }
        
        viewModelScope.launch {
            try {
                // Call auth repository to verify OTP
                val result = authRepository.verifyOTP(phoneNumber, otpCode)
                
                if (result.isSuccess) {
                    // AuthRepository handles setting Authenticated state
                } else {
                    _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Invalid OTP. Please try again.")
                }
                
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error verifying OTP", e)
                _authState.value = AuthState.Error("Verification failed. Please try again.")
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _authState.value = AuthState.Unauthenticated
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error signing out", e)
                // Still sign out locally even if server call fails
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    
    fun skipLogin() {
        authRepository.setGuestMode()
    }
    
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.length == 10 && 
               phone.all { it.isDigit() } && 
               phone.first() in '6'..'9'
    }
}