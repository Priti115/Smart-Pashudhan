# Cattle Breed Assessment App

A comprehensive Android application for capturing and analyzing cattle photos using AI-powered breed assessment capabilities. Built with **Jetpack Compose**, **Room Database**, and **CameraX**.

## Features

### 🏠 Home Screen
- Welcome interface with quick access to all features
- Clean, intuitive navigation to main app functions
- Modern Material Design 3 UI

### 📸 Photo Capture
- **CameraX Integration**: High-quality photo capture with camera preview
- **Permission Management**: Handles camera permissions with user-friendly prompts
- **Automatic Processing**: Captures photos and automatically saves records with dummy AI analysis data
- **Real-time Feedback**: Shows capture progress and success confirmation

### 📊 Animal Records History
- **List View**: Browse all captured animal records with key information
- **Detailed View**: Full record details including:
  - High-resolution animal photos
  - Complete body measurements (length, height, chest width, rump angle)
  - AI-generated ATC (Animal Type Classification) scores
  - Capture timestamps and metadata
  - Sync status indicators
- **Record Management**: Delete functionality with confirmation dialogs

### 📤 Data Export
- **JSON Export**: Structured data format ideal for system integration
- **CSV Export**: Spreadsheet-compatible format for data analysis
- **Local Storage**: Files saved to device internal storage
- **Export History**: Track recently exported files with file management

## Technical Architecture

### 📱 **UI Layer - Jetpack Compose**
- **Material Design 3**: Modern, adaptive UI components
- **Compose Navigation**: Type-safe navigation between screens
- **State Management**: Reactive UI with StateFlow and Compose state

### 🎥 **Camera Integration - CameraX**
- **Preview & Capture**: Real-time camera preview with photo capture
- **Lifecycle Aware**: Proper camera resource management
- **File Management**: Automatic image file creation and storage

### 🗄️ **Data Layer - Room Database**
- **Local Database**: SQLite database with Room abstraction
- **Type Converters**: Automatic Date serialization
- **Repository Pattern**: Clean separation of data access logic

### 🏗️ **Architecture Pattern - MVVM**
- **ViewModels**: Business logic and state management
- **Repository**: Single source of truth for data operations
- **Dependency Injection Ready**: Structure supports DI frameworks

## Database Schema

### AnimalRecord Entity
```kotlin
@Entity(tableName = "animal_records")
data class AnimalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val animalId: String,           // Unique identifier (e.g., "ANIMAL_A1B2C3D4")
    val date: Date,                 // Capture timestamp
    val imagePath: String,          // Local file path to saved image
    val bodyLength: Double,         // Body length measurement (cm)
    val height: Double,             // Height measurement (cm) 
    val chestWidth: Double,         // Chest width measurement (cm)
    val rumpAngle: Double,          // Rump angle measurement (degrees)
    val atcScore: Int,              // AI-generated ATC score (0-100)
    val synced: Boolean = false     // Cloud sync status
)
```

## Key Dependencies

```gradle
// Core Android & Compose
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.material3:material3'
implementation 'androidx.navigation:navigation-compose:2.7.5'

// Database
implementation 'androidx.room:room-runtime:2.6.0'
implementation 'androidx.room:room-ktx:2.6.0'

// Camera
implementation 'androidx.camera:camera-core:1.3.1'
implementation 'androidx.camera:camera-camera2:1.3.1'
implementation 'androidx.camera:camera-lifecycle:1.3.1'
implementation 'androidx.camera:camera-view:1.3.1'

// Image Loading
implementation 'io.coil-kt:coil-compose:2.5.0'

// Permissions
implementation 'com.google.accompanist:accompanist-permissions:0.32.0'
```

## Project Structure

```
app/src/main/java/com/cattlebreed/app/
├── data/
│   ├── entity/          # Room entities
│   ├── dao/             # Database access objects
│   ├── database/        # Database configuration
│   ├── repository/      # Data repository
│   └── converter/       # Type converters
├── ui/
│   ├── screens/         # Compose screens
│   │   ├── home/        # Home screen
│   │   ├── capture/     # Camera capture screen
│   │   ├── history/     # Records history & detail
│   │   └── export/      # Data export screen
│   └── theme/           # Material Design theme
├── utils/               # Utility classes
│   ├── FileUtils.kt     # File operations
│   └── CameraUtils.kt   # Camera operations
├── viewmodel/           # ViewModels
└── navigation/          # Navigation setup
```

## Setup Instructions

### Prerequisites
- **Android Studio**: Arctic Fox or later
- **Android SDK**: API level 24+ (Android 7.0)
- **Kotlin**: 1.9.10 or later

### Installation
1. **Clone/Download** the project to your local machine
2. **Open** the project in Android Studio
3. **Sync** Gradle files (should happen automatically)
4. **Build** the project (`Build > Make Project`)
5. **Run** on device or emulator with API 24+

### Permissions Required
- **Camera**: For photo capture functionality
- **Storage**: For saving images and export files

## Current Implementation Status

### ✅ **Completed Features**
- Complete UI/UX for all screens
- Camera integration with CameraX
- Room database with full CRUD operations
- JSON/CSV export functionality
- Navigation between all screens
- Permission handling
- File storage management

### 🔄 **Ready for AI Integration**
The app is designed with **dummy data generation** for AI-related features. The following areas are ready for ML model integration:

1. **Image Analysis**: Replace dummy measurements with actual computer vision analysis
2. **Breed Classification**: Replace dummy ATC scores with actual breed assessment
3. **Feature Extraction**: Implement real body measurement extraction from photos

### 🎯 **AI Integration Points**

```kotlin
// In MainViewModel.saveAnimalRecord()
// Current: Dummy data generation
val record = AnimalRecord(
    // ... other fields
    bodyLength = 100.0 + (0..50).random(), // Replace with AI analysis
    height = 120.0 + (0..30).random(),     // Replace with AI analysis
    atcScore = 70 + (0..30).random(),      // Replace with AI scoring
)

// Future: AI model integration
val analysisResult = aiModel.analyzeImage(imagePath)
val record = AnimalRecord(
    // ... other fields  
    bodyLength = analysisResult.bodyLength,
    height = analysisResult.height,
    atcScore = analysisResult.atcScore,
)
```

## Export Formats

### JSON Export
```json
[
  {
    "id": 1,
    "animalId": "ANIMAL_A1B2C3D4",
    "date": "2023-12-25 10:30:00",
    "imagePath": "/data/data/.../images/CATTLE_2023-12-25_10-30-00.jpg",
    "bodyLength": 125.0,
    "height": 142.0,
    "chestWidth": 58.0,
    "rumpAngle": 15.0,
    "atcScore": 87,
    "synced": false
  }
]
```

### CSV Export
```csv
ID,Animal ID,Date,Image Path,Body Length,Height,Chest Width,Rump Angle,ATC Score,Synced
1,ANIMAL_A1B2C3D4,Dec 25 2023 10:30,/data/...,125.0,142.0,58.0,15.0,87,false
```

## Future Enhancements

1. **AI Model Integration**
   - Computer vision for automatic measurements
   - Breed classification algorithms
   - Real-time analysis feedback

2. **Cloud Synchronization**
   - Remote data backup
   - Multi-device sync
   - Cloud-based AI processing

3. **Advanced Analytics**
   - Historical trends and comparisons  
   - Batch processing capabilities
   - Report generation

4. **User Experience**
   - Offline/online mode indicators
   - Progress tracking for large exports
   - Advanced filtering and search

This project provides a solid foundation for a production-ready cattle assessment application, with clean architecture and comprehensive functionality ready for AI enhancement.