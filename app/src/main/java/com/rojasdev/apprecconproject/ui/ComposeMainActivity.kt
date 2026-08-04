package com.rojasdev.apprecconproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rojasdev.apprecconproject.ui.analytics.AnalyticsScreen
import com.rojasdev.apprecconproject.ui.configuration.ConfigurationScreen
import com.rojasdev.apprecconproject.ui.labor.LaborScreen
import com.rojasdev.apprecconproject.ui.main.MainScreen
import com.rojasdev.apprecconproject.ui.recollection.RecollectionScreen
import com.rojasdev.apprecconproject.ui.reports.ReportsScreen
import com.rojasdev.apprecconproject.ui.theme.RecconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecconTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(onNavigate = { navController.navigate(it) })
        }
        composable("recoleccion") {
            RecollectionScreen(onBack = { navController.popBackStack() })
        }
        composable("labor") {
            LaborScreen(onBack = { navController.popBackStack() })
        }
        composable("analytics") {
            AnalyticsScreen(onBack = { navController.popBackStack() })
        }
        composable("informes") {
            ReportsScreen(onBack = { navController.popBackStack() })
        }
        composable("configuracion") {
            ConfigurationScreen(onBack = { navController.popBackStack() })
        }
    }
}
