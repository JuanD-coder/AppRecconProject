package com.rojasdev.apprecconproject.ui.labor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
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

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = CoffeeSecondary, // Naranja para Trabajos
                contentColor = Color.White,
                actions = {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Engineering, null) },
                        label = { Text("Jornales", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White.copy(alpha = 0.2f))
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Group, null) },
                        label = { Text("Personal", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White.copy(alpha = 0.2f))
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {}, containerColor = Color.White, contentColor = CoffeeSecondary) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(CoffeeBackground)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = CoffeeSecondary)
                }
                Text(
                    if (selectedTab == 0) "RESUMEN DE JORNALES" else "PERSONAL DE TRABAJO",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = CoffeeSecondary
                )
            }

            if (selectedTab == 0) {
                LaborSummaryHeader(uiState.totalDays, uiState.totalAmount)
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.laborTotals) { item -> LaborCard(item) }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.workers) { worker -> WorkerItem(worker) }
                }
            }
        }
    }
}

@Composable
fun LaborSummaryHeader(days: Double, amount: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = CoffeeSecondary,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Días Totales", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                Text("${days.toInt()}", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color.White.copy(alpha = 0.2f))
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total a Pagar", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                Text("$${amount.toInt()}", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun LaborCard(item: workTotalCollector) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(48.dp), shape = MaterialTheme.shapes.medium, color = PastelOrange) {
                Icon(Icons.Default.Work, null, modifier = Modifier.padding(8.dp), tint = OnPastelOrange)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name_recolector ?: "N/A", fontWeight = FontWeight.Bold)
                Text("${item.days_work.toInt()} días registrados", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text("$${item.total.toInt()}", fontWeight = FontWeight.Black, color = OnPastelOrange, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun WorkerItem(worker: RecolectoresEntity) {
    ListItem(
        headlineContent = { Text(worker.name, fontWeight = FontWeight.Bold) },
        supportingContent = { Text("Estado: ${worker.state.uppercase()}") },
        leadingContent = {
            Surface(Modifier.size(40.dp), shape = MaterialTheme.shapes.small, color = PastelOrange) {
                Icon(Icons.Default.Person, null, modifier = Modifier.padding(8.dp), tint = OnPastelOrange)
            }
        },
        modifier = Modifier.background(Color.White, MaterialTheme.shapes.medium)
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
