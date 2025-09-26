package com.cattlebreed.app.network

import com.cattlebreed.app.data.entity.AnimalRecord
import java.util.Date

/**
 * API Request/Response models for web admin panel integration
 */

// Authentication APIs
data class LoginRequest(
    val phoneNumber: String,
    val otp: String,
    val deviceId: String,
    val appVersion: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData?
)

data class AuthData(
    val token: String,
    val refreshToken: String,
    val user: ApiUser,
    val permissions: List<String>
)

data class ApiUser(
    val id: String,
    val phoneNumber: String,
    val name: String?,
    val farmName: String?,
    val location: String?,
    val isVerified: Boolean,
    val role: UserRole,
    val createdAt: String,
    val updatedAt: String
)

enum class UserRole {
    FARMER,
    VETERINARIAN,
    ADMIN,
    FIELD_OFFICER
}

// Animal Record APIs
data class SyncAnimalRecordsRequest(
    val records: List<AnimalRecordDto>,
    val lastSyncTimestamp: Long?
)

data class SyncAnimalRecordsResponse(
    val success: Boolean,
    val message: String,
    val data: SyncData?
)

data class SyncData(
    val syncedRecords: List<String>, // Record IDs that were synced
    val conflicts: List<ConflictRecord>,
    val serverRecords: List<AnimalRecordDto>, // New records from server
    val deletedRecords: List<String>, // Record IDs deleted on server
    val lastSyncTimestamp: Long
)

data class ConflictRecord(
    val localRecord: AnimalRecordDto,
    val serverRecord: AnimalRecordDto,
    val conflictType: ConflictType
)

enum class ConflictType {
    MODIFIED_ON_BOTH,
    DELETED_ON_SERVER,
    NEWER_ON_SERVER
}

// Animal Record DTO for API
data class AnimalRecordDto(
    val id: String?,
    val localId: Long?, // Local database ID
    val animalId: String,
    val userId: String,
    val imagePath: String?, // Local path
    val imageUrl: String?, // Server URL
    val date: String, // ISO format
    val bodyLength: Double,
    val height: Double,
    val chestWidth: Double,
    val rumpAngle: Double,
    val atcScore: Int,
    val synced: Boolean,
    val aiAnalysis: AIAnalysisDto?,
    val location: LocationDto?,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String,
    val version: Int = 1 // For conflict resolution
)

data class AIAnalysisDto(
    val breedClassification: String?,
    val confidence: Float,
    val healthScore: Float?,
    val qualityGrade: String?,
    val recommendations: List<String>,
    val providerId: String,
    val modelVersion: String,
    val processingTime: Long
)

data class LocationDto(
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val farmName: String?
)

// File Upload APIs
data class ImageUploadRequest(
    val recordId: String,
    val fileName: String,
    val contentType: String,
    val fileSize: Long
)

data class ImageUploadResponse(
    val success: Boolean,
    val message: String,
    val data: UploadData?
)

data class UploadData(
    val uploadUrl: String, // Pre-signed URL for direct upload
    val fileId: String,
    val expiresAt: String
)

// Analytics and Reporting APIs
data class FarmAnalyticsRequest(
    val startDate: String,
    val endDate: String,
    val includeComparisons: Boolean = true
)

data class FarmAnalyticsResponse(
    val success: Boolean,
    val data: AnalyticsData?
)

data class AnalyticsData(
    val totalAnimals: Int,
    val averageAtcScore: Float,
    val breedDistribution: Map<String, Int>,
    val healthTrends: List<HealthTrendData>,
    val performanceMetrics: PerformanceMetrics,
    val recommendations: List<String>
)

data class HealthTrendData(
    val date: String,
    val averageHealth: Float,
    val animalCount: Int
)

data class PerformanceMetrics(
    val productivityScore: Float,
    val improvementSuggestions: List<String>,
    val benchmarkComparison: BenchmarkData?
)

data class BenchmarkData(
    val regionalAverage: Float,
    val nationalAverage: Float,
    val ranking: String // TOP_10_PERCENT, ABOVE_AVERAGE, etc.
)

// Admin Panel APIs (for web app integration)
data class AdminDashboardRequest(
    val dateRange: String, // "7d", "30d", "90d", "1y"
    val region: String?
)

data class AdminDashboardResponse(
    val success: Boolean,
    val data: AdminDashboardData?
)

data class AdminDashboardData(
    val totalFarmers: Int,
    val totalAnimals: Int,
    val activeUsers: Int,
    val recentAnalyses: Int,
    val topBreeds: List<BreedStatistic>,
    val systemHealth: SystemHealthData,
    val alerts: List<SystemAlert>
)

data class BreedStatistic(
    val breedName: String,
    val count: Int,
    val percentage: Float,
    val averageScore: Float
)

data class SystemHealthData(
    val apiResponseTime: Float,
    val errorRate: Float,
    val activeConnections: Int,
    val databaseHealth: String
)

data class SystemAlert(
    val id: String,
    val type: AlertType,
    val message: String,
    val severity: AlertSeverity,
    val createdAt: String,
    val resolved: Boolean
)

enum class AlertType {
    SYSTEM_ERROR,
    HIGH_LOAD,
    API_FAILURE,
    DATABASE_ISSUE,
    SECURITY_CONCERN
}

enum class AlertSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

// Configuration and Settings APIs
data class AppConfigResponse(
    val success: Boolean,
    val data: AppConfigData?
)

data class AppConfigData(
    val aiProviderConfig: AIProviderConfig,
    val syncSettings: SyncSettings,
    val uiSettings: UISettings,
    val featureFlags: Map<String, Boolean>
)

data class AIProviderConfig(
    val defaultProvider: String,
    val availableProviders: List<String>,
    val confidenceThreshold: Float,
    val enableLocalProcessing: Boolean
)

data class SyncSettings(
    val autoSyncEnabled: Boolean,
    val syncInterval: Long, // minutes
    val wifiOnlySync: Boolean,
    val maxRetries: Int
)

data class UISettings(
    val theme: String,
    val defaultLanguage: String,
    val availableLanguages: List<String>,
    val showAdvancedFeatures: Boolean
)

// Generic API Response Wrapper
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val error: ApiError?,
    val timestamp: String,
    val version: String = "1.0"
)

data class ApiError(
    val code: String,
    val message: String,
    val details: Map<String, Any>?
)

// Pagination for list APIs
data class PaginatedRequest(
    val page: Int = 1,
    val limit: Int = 20,
    val sortBy: String? = null,
    val sortOrder: String = "desc", // asc or desc
    val filters: Map<String, String>? = null
)

data class PaginatedResponse<T>(
    val data: List<T>,
    val pagination: PaginationInfo
)

data class PaginationInfo(
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val itemsPerPage: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)