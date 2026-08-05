package com.rojasdev.apprecconproject.ui.labor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.ui.components.EmptyState
import com.rojasdev.apprecconproject.ui.theme.*

@Composable
fun LaborScreen(
    onBack: () -> Unit,
    viewModel: LaborViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaborContent(uiState, onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaborContent(
    uiState: LaborUiState,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val accentColor = OrangeLegacy

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = accentColor,
                contentColor = Color.White,
                actions = {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Engineering, null) },
                        label = { Text("Jornales", fontFamily = Comfortaa, color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = accentColor,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Group, null) },
                        label = { Text("Personal", fontFamily = Comfortaa, color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = accentColor,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {}, containerColor = Color.White, contentColor = accentColor) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE7E7E7)) // gray_light from legacy
        ) {
            // Header Area (Legacy style)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accentColor)
                    .padding(bottom = 16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Text(
                            if (selectedTab == 0) "RESUMEN DE JORNALES" else "PERSONAL DE TRABAJO",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = Comfortaa,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    if (selectedTab == 0) {
                        LaborSummaryHeader(uiState.totalDays, uiState.totalAmount)
                    }
                }
            }

            if (selectedTab == 0) {
                if (uiState.laborTotals.isEmpty()) {
                    EmptyState("No hay jornales registrados")
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.laborTotals) { item -> LaborCard(item) }
                    }
                }
            } else {
                if (uiState.workers.isEmpty()) {
                    EmptyState("No hay personal registrado")
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.workers) { worker -> WorkerItem(worker) }
                    }
                }
            }
        }
    }
}

@Composable
fun LaborSummaryHeader(days: Double, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Text(
                text = "${days.toInt()}",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Días Totales",
                style = MaterialTheme.typography.labelMedium,
                fontFamily = Comfortaa,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
        
        VerticalDivider(modifier = Modifier.height(60.dp), color = Color.White.copy(alpha = 0.3f))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Text(
                text = "$${amount.toInt()}",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Total a Pagar",
                style = MaterialTheme.typography.labelMedium,
                fontFamily = Comfortaa,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun LaborCard(item: workTotalCollector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = OrangeLegacy.copy(alpha = 0.1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_recolector),
                    null,
                    modifier = Modifier.padding(8.dp),
                    tint = OrangeLegacy
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    item.name_recolector ?: "N/A",
                    fontFamily = Comfortaa,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "${item.days_work.toInt()} días registrados",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = Comfortaa,
                    color = Color.Gray
                )
            }
            Text(
                "$${item.total.toInt()}",
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Black,
                color = OrangeLegacy,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun WorkerItem(worker: RecolectoresEntity) {
    ListItem(
        headlineContent = { Text(worker.name, fontFamily = Comfortaa, fontWeight = FontWeight.Black) },
        supportingContent = { Text("Estado: ${worker.state.uppercase()}", fontFamily = Comfortaa) },
        leadingContent = {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = OrangeLegacy.copy(alpha = 0.1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_recolector),
                    null,
                    modifier = Modifier.padding(8.dp),
                    tint = OrangeLegacy
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.White),
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LaborScreenPreview() {
    RecconTheme {
        LaborContent(
            uiState = LaborUiState(
                workers = listOf(RecolectoresEntity(1, "Carlos Campo", "active")),
                laborTotals = listOf(workTotalCollector(1, "Carlos Campo", 5, 225000.0)),
                totalDays = 5.0,
                totalAmount = 225000.0
            ),
            onBack = {}
        )
    }
}
