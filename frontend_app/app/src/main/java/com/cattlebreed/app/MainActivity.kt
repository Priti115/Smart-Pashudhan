package com.cattlebreed.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cattlebreed.app.data.auth.AuthRepository
import com.cattlebreed.app.data.auth.AuthState
import com.cattlebreed.app.data.database.AppDatabase
import com.cattlebreed.app.data.repository.AnimalRepository
import com.cattlebreed.app.navigation.NavigationArguments
import com.cattlebreed.app.navigation.NavigationDestination
import com.cattlebreed.app.ui.screens.auth.OTPVerificationScreen
import com.cattlebreed.app.ui.screens.auth.PhoneInputScreen
import com.cattlebreed.app.ui.screens.capture.CaptureScreen
import com.cattlebreed.app.ui.screens.export.ExportScreen
import com.cattlebreed.app.ui.screens.history.AnimalDetailScreen
import com.cattlebreed.app.ui.screens.history.HistoryScreen
import com.cattlebreed.app.ui.screens.home.HomeScreen
import com.cattlebreed.app.ui.screens.settings.SettingsScreen
import com.cattlebreed.app.ui.theme.CattleBreedAppTheme
import com.cattlebreed.app.utils.FileUtils
import com.cattlebreed.app.utils.PDFUtils
import com.cattlebreed.app.viewmodel.AuthViewModel
import com.cattlebreed.app.viewmodel.ExportViewModel
import com.cattlebreed.app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var repository: AnimalRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var fileUtils: FileUtils
    private lateinit var pdfUtils: PDFUtils
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize dependencies
        val database = AppDatabase.getDatabase(this)
        repository = AnimalRepository(database.animalRecordDao())
        val preferences = getSharedPreferences("cattle_breed_prefs", MODE_PRIVATE)
        authRepository = AuthRepository(this, preferences)
        fileUtils = FileUtils(this)
        pdfUtils = PDFUtils(this)
        
        setContent {
            CattleBreedAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CattleBreedApp(repository, authRepository, fileUtils, pdfUtils)
                }
            }
        }
    }
}

@Composable
fun CattleBreedApp(
    repository: AnimalRepository,
    authRepository: AuthRepository,
    fileUtils: FileUtils,
    pdfUtils: PDFUtils
) {
    val navController = rememberNavController()
    
    // Create ViewModels
    val mainViewModel: MainViewModel = viewModel { MainViewModel(repository) }
    val exportViewModel: ExportViewModel = viewModel { ExportViewModel(repository, fileUtils, pdfUtils) }
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository) }
    
    // Observe authentication state
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    
    // Determine start destination based on auth state
    val startDestination = when (val state = authState) {
        is AuthState.Authenticated -> NavigationDestination.HOME.route
        is AuthState.Guest -> NavigationDestination.HOME.route
        is AuthState.OTPPending -> "otp_verification/${state.phoneNumber}"
        else -> NavigationDestination.PHONE_INPUT.route
    }
    
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Authentication screens
            composable(NavigationDestination.PHONE_INPUT.route) {
                PhoneInputScreen(
                    authViewModel = authViewModel,
                    onNavigateToOTP = { phoneNumber ->
                        navController.navigate("otp_verification/$phoneNumber")
                    },
                    onSkipLogin = {
                        authViewModel.skipLogin()
                        navController.navigate(NavigationDestination.HOME.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            
            composable("otp_verification/{${NavigationArguments.PHONE_NUMBER}}") { backStackEntry ->
                val phoneNumber = backStackEntry.arguments?.getString(NavigationArguments.PHONE_NUMBER) ?: ""
                OTPVerificationScreen(
                    phoneNumber = phoneNumber,
                    authViewModel = authViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToHome = {
                        navController.navigate(NavigationDestination.HOME.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(NavigationDestination.HOME.route) {
                HomeScreen(
                    onNavigateToCapture = {
                        navController.navigate(NavigationDestination.CAPTURE.route)
                    },
                    onNavigateToHistory = {
                        navController.navigate(NavigationDestination.HISTORY.route)
                    },
                    onNavigateToExport = {
                        navController.navigate(NavigationDestination.EXPORT.route)
                    },
                    onNavigateToSettings = {
                        navController.navigate(NavigationDestination.SETTINGS.route)
                    }
                )
            }
            
            composable(NavigationDestination.CAPTURE.route) {
                CaptureScreen(
                    viewModel = mainViewModel,
                    fileUtils = fileUtils,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToHistory = {
                        navController.navigate(NavigationDestination.HISTORY.route) {
                            popUpTo(NavigationDestination.HOME.route)
                        }
                    }
                )
            }
            
            composable(NavigationDestination.HISTORY.route) {
                HistoryScreen(
                    viewModel = mainViewModel,
                    fileUtils = fileUtils,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToDetail = { animalId ->
                        navController.navigate("animal_detail/$animalId")
                    }
                )
            }
            
            composable(NavigationDestination.EXPORT.route) {
                ExportScreen(
                    viewModel = exportViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(NavigationDestination.SETTINGS.route) {
                SettingsScreen(
                    authViewModel = authViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToProfile = {
                        // TODO: Navigate to profile screen when implemented
                    },
                    onSignOut = {
                        navController.navigate(NavigationDestination.PHONE_INPUT.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(NavigationDestination.PHONE_INPUT.route)
                    }
                )
            }
            
            composable("animal_detail/{${NavigationArguments.ANIMAL_ID}}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString(NavigationArguments.ANIMAL_ID)?.toLongOrNull()
                if (animalId != null) {
                    AnimalDetailScreen(
                        animalId = animalId,
                        viewModel = mainViewModel,
                        fileUtils = fileUtils,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    // Show error screen or navigate back if invalid ID
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}