package com.rojasdev.apprecconproject.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.rojasdev.apprecconproject.ui.theme.RecconTheme
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import com.rojasdev.apprecconproject.ui.theme.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

data class HomeModule(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val color: Color,
    val onColor: Color,
    val onClick: () -> Unit
)

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MainContent(
        onNavigate = onNavigate,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    onNavigate: (String) -> Unit,
    uiState: MainUiState
) {
    val modules = listOf(
        HomeModule("Recolección", Icons.Default.Agriculture, "Gestión de café diario", PastelRed, OnPastelRed) { onNavigate("recoleccion") },
        HomeModule("Jornales", Icons.Default.Engineering, "Control de días de trabajo", PastelOrange, OnPastelOrange) { onNavigate("labor") },
        HomeModule("Contabilidad", Icons.Default.Assessment, "Reportes y finanzas", PastelGreen, OnPastelGreen) { onNavigate("analytics") }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // El saludo y la taza que te gustaban
            WelcomeHeader(onNavigate)

            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera de Precios en Rojo
            PriceHeader(uiState, onNavigate)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Módulos Principales",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Módulos (Vertical)
            modules.forEach { module ->
                ModuleCard(module)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WelcomeHeader(onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                "¡Buen día!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Todo listo para la jornada de hoy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onNavigate("configuracion") }) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Coffee,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer(alpha = 0.15f),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    RecconTheme {
        MainContent(
            onNavigate = {},
            uiState = MainUiState(
                priceYesAliment = 500,
                priceNoAliment = 600,
                priceWork = 45000,
                isLoading = false
            )
        )
    }
}

@Composable
fun PriceHeader(uiState: MainUiState, onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PriceIndicator("Con Alimentación", uiState.priceYesAliment)
                PriceIndicator("Sin Alimentación", uiState.priceNoAliment)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onNavigate("configuracion") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Update, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ACTUALIZAR", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PriceIndicator(label: String, price: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$${price}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ModuleCard(module: HomeModule) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { module.onClick() },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Side
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp),
                color = module.color,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        module.icon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = module.onColor
                    )
                }
            }

            // Text Side
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
            ) {
                Text(
                    text = module.title.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = module.onColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
