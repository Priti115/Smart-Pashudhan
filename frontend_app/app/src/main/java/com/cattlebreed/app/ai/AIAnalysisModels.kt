package com.cattlebreed.app.ai

import java.io.File

/**
 * Interface for AI analysis providers
 * This allows plugging in different AI models (local TensorFlow Lite, remote API, etc.)
 */
interface AIAnalysisProvider {
    
    /**
     * Analyze cattle image and return detailed analysis
     */
    suspend fun analyzeCattleImage(imageFile: File): AIAnalysisResult
    
    /**
     * Check if the provider is available and ready
     */
    suspend fun isAvailable(): Boolean
    
    /**
     * Get provider information
     */
    fun getProviderInfo(): AIProviderInfo
    
    /**
     * Release resources
     */
    fun cleanup()
}

/**
 * Result of AI analysis containing all extracted information
 */
data class AIAnalysisResult(
    val success: Boolean,
    val message: String? = null,
    val confidence: Float = 0.0f,
    val processingTime: Long = 0L, // milliseconds
    
    // Basic measurements (if detected)
    val measurements: CattleMeasurements? = null,
    
    // Breed classification
    val breedClassification: BreedClassification? = null,
    
    // Body condition scoring
    val bodyCondition: BodyConditionScore? = null,
    
    // Health indicators
    val healthIndicators: HealthAnalysis? = null,
    
    // Additional traits
    val physicalTraits: PhysicalTraits? = null,
    
    // Quality assessment
    val qualityMetrics: QualityMetrics? = null,
    
    // Raw AI model output (for debugging/advanced users)
    val rawOutput: Map<String, Any>? = null
)

/**
 * Cattle measurements extracted from image analysis
 */
data class CattleMeasurements(
    val bodyLength: Float? = null,
    val height: Float? = null,
    val chestWidth: Float? = null,
    val rumpAngle: Float? = null,
    val neckLength: Float? = null,
    val legLength: Float? = null,
    val confidence: Float = 0.0f,
    val unit: MeasurementUnit = MeasurementUnit.CM
)

/**
 * Breed classification results
 */
data class BreedClassification(
    val primaryBreed: String,
    val primaryBreedConfidence: Float,
    val secondaryBreed: String? = null,
    val secondaryBreedConfidence: Float? = null,
    val possibleBreeds: List<BreedPrediction> = emptyList(),
    val isCrossbreed: Boolean = false,
    val crossbreedComponents: List<String> = emptyList()
)

data class BreedPrediction(
    val breedName: String,
    val confidence: Float,
    val characteristics: List<String> = emptyList()
)

/**
 * Body condition scoring (1-9 scale typically used for cattle)
 */
data class BodyConditionScore(
    val score: Float, // 1.0 to 9.0 scale
    val category: BodyConditionCategory,
    val confidence: Float,
    val indicators: List<String> = emptyList(),
    val recommendations: List<String> = emptyList()
)

enum class BodyConditionCategory {
    EMACIATED, // 1-2
    THIN, // 2-3
    MODERATE, // 4-6
    GOOD, // 6-7
    OBESE // 8-9
}

/**
 * Health indicators from visual analysis
 */
data class HealthAnalysis(
    val overallHealth: HealthStatus,
    val eyeCondition: String? = null,
    val coatCondition: String? = null,
    val postureAnalysis: String? = null,
    val visibleIssues: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(),
    val confidence: Float = 0.0f
)

enum class HealthStatus {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR,
    CONCERNING
}

/**
 * Physical traits analysis
 */
data class PhysicalTraits(
    val coatColor: String? = null,
    val coatPattern: String? = null,
    val bodyType: String? = null, // Dairy, Beef, Dual-purpose
    val musculature: String? = null,
    val frameSize: FrameSize? = null,
    val specialFeatures: List<String> = emptyList()
)

enum class FrameSize {
    SMALL,
    MEDIUM,
    LARGE
}

/**
 * Quality metrics and scoring
 */
data class QualityMetrics(
    val atcScore: Int, // Animal Type Classification score
    val conformationScore: Float? = null,
    val productionPotential: ProductionPotential? = null,
    val overallGrade: String? = null,
    val strengths: List<String> = emptyList(),
    val improvements: List<String> = emptyList()
)

data class ProductionPotential(
    val milkProduction: ProductionEstimate? = null,
    val meatProduction: ProductionEstimate? = null,
    val breedingValue: Float? = null
)

data class ProductionEstimate(
    val potential: String, // HIGH, MEDIUM, LOW
    val estimatedValue: Float? = null,
    val unit: String? = null
)

enum class MeasurementUnit {
    CM,
    INCHES,
    PIXELS
}

/**
 * AI Provider information
 */
data class AIProviderInfo(
    val name: String,
    val version: String,
    val type: AIProviderType,
    val capabilities: List<AICapability>,
    val supportedImageFormats: List<String>,
    val maxImageSize: Long? = null,
    val requiresInternet: Boolean = false,
    val modelInfo: String? = null
)

enum class AIProviderType {
    LOCAL_TENSORFLOW_LITE,
    LOCAL_ONNX,
    REMOTE_API,
    CLOUD_VISION,
    CUSTOM
}

enum class AICapability {
    BREED_CLASSIFICATION,
    BODY_MEASUREMENTS,
    HEALTH_ANALYSIS,
    BODY_CONDITION_SCORING,
    TRAIT_ANALYSIS,
    QUALITY_ASSESSMENT,
    DEFECT_DETECTION
}

/**
 * Configuration for AI analysis
 */
data class AIAnalysisConfig(
    val enableBreedClassification: Boolean = true,
    val enableMeasurements: Boolean = true,
    val enableHealthAnalysis: Boolean = true,
    val enableQualityAssessment: Boolean = true,
    val confidenceThreshold: Float = 0.5f,
    val maxProcessingTime: Long = 30000L, // 30 seconds
    val cacheResults: Boolean = true,
    val preferredProvider: String? = null
)