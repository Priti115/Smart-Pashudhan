package com.cattlebreed.app.ui.screens.capture

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cattlebreed.app.utils.CameraUtils
import com.cattlebreed.app.utils.FileUtils
import com.cattlebreed.app.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CaptureScreen(
    viewModel: MainViewModel,
    fileUtils: FileUtils,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var cameraUtils: CameraUtils? by remember { mutableStateOf(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
    
    LaunchedEffect(message) {
        if (message?.contains("successfully") == true) {
            showSuccessDialog = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Capture Animal Photo",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        
        when {
            cameraPermissionState.status.isGranted -> {
                CameraContent(
                    modifier = Modifier.padding(innerPadding),
                    context = context,
                    lifecycleOwner = lifecycleOwner,
                    isLoading = isLoading,
                    onImageCaptureReady = { capture, utils ->
                        imageCapture = capture
                        cameraUtils = utils
                    },
                    onCapturePhoto = {
                        imageCapture?.let { capture ->
                            val outputFile = fileUtils.createImageFile()
                            cameraUtils?.capturePhoto(
                                imageCapture = capture,
                                outputFile = outputFile,
                                onImageSaved = { imagePath ->
                                    viewModel.saveAnimalRecord(imagePath)
                                },
                                onError = { _ ->
                                    // Handle error - could log or show toast
                                }
                            )
                        }
                    }
                )
            }
            
            cameraPermissionState.status.shouldShowRationale -> {
                PermissionRationale(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            
            else -> {
                PermissionDenied(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        
        // Success Dialog
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showSuccessDialog = false
                    viewModel.clearMessage()
                },
                title = {
                    Text("Photo Captured!")
                },
                text = {
                    Text("Animal photo has been captured and record saved successfully.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.clearMessage()
                            onNavigateToHistory()
                        }
                    ) {
                        Text("View Records")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.clearMessage()
                        }
                    ) {
                        Text("Take Another")
                    }
                }
            )
        }
    }
}

@Composable
private fun CameraContent(
    context: android.content.Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    isLoading: Boolean,
    onImageCaptureReady: (ImageCapture, CameraUtils) -> Unit,
    onCapturePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraUtils = remember { CameraUtils(context) }
    
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    cameraUtils.setupCamera(
                        lifecycleOwner = lifecycleOwner,
                        previewView = this,
                        onCameraReady = { imageCapture ->
                            onImageCaptureReady(imageCapture, cameraUtils)
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Camera Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Instruction text with better styling
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Position the animal in the frame and tap to capture",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
            
            // Capture Button with consistent styling
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = if (isLoading) {
                        { }
                    } else {
                        onCapturePhoto
                    },
                    modifier = Modifier.size(72.dp),
                    containerColor = if (isLoading) 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else 
                        MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Capture Photo",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
    
    DisposableEffect(cameraUtils) {
        onDispose {
            cameraUtils.destroy()
        }
    }
}

@Composable
private fun PermissionRationale(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CameraAlt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Camera Permission Required",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "To capture animal photos, we need access to your camera. This permission is essential for the app's core functionality.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onRequestPermission) {
            Text("Grant Camera Permission")
        }
    }
}

@Composable
private fun PermissionDenied(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Block,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Camera Permission Denied",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Camera access has been denied. To use this feature, please enable camera permissions in your device settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}