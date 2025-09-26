package com.cattlebreed.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * Language manager for handling multi-language support
 * Supports major Indian languages
 */
class LanguageManager(
    private val context: Context,
    private val preferences: SharedPreferences
) {
    
    companion object {
        private const val PREF_LANGUAGE_CODE = "selected_language"
        private const val DEFAULT_LANGUAGE = "en"
        
        /**
         * Static helper methods for use without instance
         */
        fun getCurrentLanguage(context: Context): String {
            val prefs = context.getSharedPreferences("cattle_breed_prefs", Context.MODE_PRIVATE)
            return prefs.getString(PREF_LANGUAGE_CODE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
        }
        
        fun setLanguage(context: Context, languageCode: String) {
            val prefs = context.getSharedPreferences("cattle_breed_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString(PREF_LANGUAGE_CODE, languageCode).apply()
            
            // Apply language change
            val localeList = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }
    
    /**
     * Supported languages with their details
     */
    enum class SupportedLanguage(
        val code: String,
        val displayName: String,
        val nativeName: String,
        val region: String
    ) {
        ENGLISH("en", "English", "English", "Global"),
        HINDI("hi", "Hindi", "हिन्दी", "India"),
        TAMIL("ta", "Tamil", "தமிழ்", "Tamil Nadu"),
        TELUGU("te", "Telugu", "తెలుగు", "Andhra Pradesh, Telangana"),
        GUJARATI("gu", "Gujarati", "ગુજરાતી", "Gujarat"),
        MARATHI("mr", "Marathi", "मराठी", "Maharashtra"),
        BENGALI("bn", "Bengali", "বাংলা", "West Bengal"),
        KANNADA("kn", "Kannada", "ಕನ್ನಡ", "Karnataka"),
        MALAYALAM("ml", "Malayalam", "മലയാളം", "Kerala"),
        PUNJABI("pa", "Punjabi", "ਪੰਜਾਬੀ", "Punjab"),
        ODIA("or", "Odia", "ଓଡ଼ିଆ", "Odisha"),
        ASSAMESE("as", "Assamese", "অসমীয়া", "Assam");
        
        companion object {
            fun fromCode(code: String): SupportedLanguage? {
                return values().find { it.code == code }
            }
            
            fun getAllLanguages(): List<SupportedLanguage> {
                return values().toList()
            }
            
            fun getIndianLanguages(): List<SupportedLanguage> {
                return values().filter { it.region.contains("India") || it != ENGLISH }
            }
        }
    }
    
    /**
     * Get current selected language
     */
    fun getCurrentLanguage(): SupportedLanguage {
        val savedLanguage = preferences.getString(PREF_LANGUAGE_CODE, DEFAULT_LANGUAGE)
        return SupportedLanguage.fromCode(savedLanguage ?: DEFAULT_LANGUAGE) ?: SupportedLanguage.ENGLISH
    }
    
    /**
     * Set application language
     */
    fun setLanguage(language: SupportedLanguage) {
        // Save to preferences
        preferences.edit()
            .putString(PREF_LANGUAGE_CODE, language.code)
            .apply()
        
        // Apply to app
        applyLanguage(language)
    }
    
    /**
     * Apply language to the application
     */
    private fun applyLanguage(language: SupportedLanguage) {
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
    
    /**
     * Get current locale
     */
    fun getCurrentLocale(): Locale {
        return Locale(getCurrentLanguage().code)
    }
    
    /**
     * Get language display text for UI
     */
    fun getLanguageDisplayText(language: SupportedLanguage): String {
        return "${language.nativeName} (${language.displayName})"
    }
    
    /**
     * Auto-detect language based on system locale
     */
    fun autoDetectLanguage(): SupportedLanguage {
        val systemLanguage = Locale.getDefault().language
        return SupportedLanguage.fromCode(systemLanguage) ?: SupportedLanguage.ENGLISH
    }
    
    /**
     * Initialize language on app start
     */
    fun initializeLanguage() {
        val currentLanguage = getCurrentLanguage()
        applyLanguage(currentLanguage)
    }
    
    /**
     * Get all supported languages for selection UI
     */
    fun getSupportedLanguages(): List<LanguageOption> {
        return SupportedLanguage.getAllLanguages().map { language ->
            LanguageOption(
                code = language.code,
                displayText = getLanguageDisplayText(language),
                isSelected = language == getCurrentLanguage(),
                region = language.region
            )
        }
    }
    
    /**
     * Check if RTL support is needed
     */
    fun isRtlLanguage(): Boolean {
        // Add RTL languages if needed in future (Arabic, Urdu, etc.)
        return false
    }
    
    /**
     * Get language statistics for analytics
     */
    fun getLanguageStats(): LanguageStats {
        val current = getCurrentLanguage()
        return LanguageStats(
            selectedLanguage = current.code,
            isSystemDefault = current == autoDetectLanguage(),
            totalSupportedLanguages = SupportedLanguage.values().size
        )
    }
}

/**
 * Data class for language selection UI
 */
data class LanguageOption(
    val code: String,
    val displayText: String,
    val isSelected: Boolean,
    val region: String
)

/**
 * Language statistics for analytics
 */
data class LanguageStats(
    val selectedLanguage: String,
    val isSystemDefault: Boolean,
    val totalSupportedLanguages: Int
)