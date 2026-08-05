package com.rojasdev.apprecconproject.ui.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.ui.components.LoadingState
import com.rojasdev.apprecconproject.ui.theme.*
import kotlinx.coroutines.delay

data class HomeModule(
    val title: String,
    val icon: Int,
    val description: String,
    val color: Color,
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
        HomeModule("Recolección", R.drawable.ic_bolsa_de_cafe, "Gestiona la producción del día", ThunderbirdLegacy) { onNavigate("recoleccion") },
        HomeModule("Jornales", R.drawable.ic_recolector, "Control de días y trabajos", OrangeLegacy) { onNavigate("labor") },
        HomeModule("Contabilidad", R.drawable.ic_playlist24, "Tus finanzas y reportes", HippieGreenLegacy) { onNavigate("analytics") }
    )

    // Animación de entrada para los módulos
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Scaffold(
        containerColor = Color(0xFFFAF9F6) // Tonos arena suaves
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Expansivo Glassmorphism
            PremiumWelcomeHeader(onNavigate)

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-30).dp) // Superposición elegante
            ) {
                // Dashboard de Precios (Bento Box style)
                if (uiState.isLoading) {
                    LoadingState()
                } else {
                    PriceDashboard(uiState, onNavigate)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Operaciones",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Comfortaa,
                    fontWeight = FontWeight.Black,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Módulos en Cascada con más aire (Márgenes y Espaciado)
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp) // Espaciado generoso entre módulos
                ) {
                    modules.forEachIndexed { index, module ->
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(600, delayMillis = index * 200)) +
                                    slideInVertically(animationSpec = tween(600, delayMillis = index * 200)) { it / 3 }
                        ) {
                            PremiumModuleCard(module)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PremiumWelcomeHeader(onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ThunderbirdLegacy, ThunderbirdLegacy.copy(alpha = 0.8f), Color.Transparent)
                )
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            Text(
                "¡Hola!",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                "Hoy es una buena cosecha",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Comfortaa,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        IconButton(
            onClick = { onNavigate("configuracion") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(Icons.Default.Settings, null, tint = Color.White)
        }

        Icon(
            painter = painterResource(id = R.drawable.reecon_curvas),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .graphicsLayer(alpha = 0.1f),
            tint = Color.White
        )
    }
}

@Composable
fun PriceDashboard(uiState: MainUiState, onNavigate: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Precios de Recolección",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = Comfortaa,
                    fontWeight = FontWeight.Black,
                    color = Color.DarkGray
                )
                
                Surface(
                    onClick = { onNavigate("configuracion") },
                    color = ThunderbirdLegacy.copy(alpha = 0.1f),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp), tint = ThunderbirdLegacy)
                        Spacer(Modifier.width(4.dp))
                        Text("EDITAR", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = ThunderbirdLegacy)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PriceItem(
                    label = "Con Alim.",
                    price = uiState.priceYesAliment,
                    icon = R.drawable.ic_alimentacion,
                    modifier = Modifier.weight(1f)
                )
                PriceItem(
                    label = "Sin Alim.",
                    price = uiState.priceNoAliment,
                    icon = R.drawable.ic_no_alimentacion,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PriceItem(label: String, price: Int, icon: Int, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFF9F9F9), RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ThunderbirdLegacy.copy(alpha = 0.7f)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "$$price",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Black,
            color = ThunderbirdLegacy
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = Comfortaa,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PremiumModuleCard(module: HomeModule) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Altura aumentada para mayor comodidad visual
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = module.onClick
            ),
        shape = RoundedCornerShape(32.dp), // Esquinas mucho más redondeadas y modernas
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Sombra más profunda para destacar
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            module.color.copy(alpha = 0.15f),
                            module.color.copy(alpha = 0.02f),
                            Color.Transparent
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con contenedor circular vibrante
            Box(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape), // Contraste sobre el degradado
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = module.icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = module.color
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    fontFamily = Comfortaa,
                    color = Color.DarkGray
                )
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = Comfortaa,
                    color = Color.Gray
                )
            }

            // Indicador de acción
            Box(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    null,
                    tint = module.color,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPremiumPreview() {
    RecconTheme {
        MainContent(
            onNavigate = {},
            uiState = MainUiState(
                priceYesAliment = 500,
                priceNoAliment = 800,
                priceWork = 45000,
                isLoading = false
            )
        )
    }
}
