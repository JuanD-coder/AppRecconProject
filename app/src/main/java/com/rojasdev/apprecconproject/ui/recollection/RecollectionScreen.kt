package com.rojasdev.apprecconproject.ui.recollection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecollectionScreen(
    onBack: () -> Unit,
    viewModel: RecollectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddCollectorDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name ->
                viewModel.addCollector(name)
                showDialog = false
            }
        )
    }

    RecollectionContent(
        uiState = uiState,
        onBack = onBack,
        onAddClick = { showDialog = true },
        onArchiveCollector = { viewModel.archiveCollector(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecollectionContent(
    uiState: RecollectionUiState,
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onArchiveCollector: (Int) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = CoffeePrimary,
                contentColor = Color.White,
                actions = {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Payments, contentDescription = null) },
                        label = { Text("Pagos", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoffeePrimary,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Personal", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoffeePrimary,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onAddClick,
                        containerColor = Color.White,
                        contentColor = CoffeePrimary,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(CoffeeBackground)
        ) {
            // Ad Placeholder (Legacy had a banner here)
            AdBannerPlaceholder()

            // Header con título y botón de atrás
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = CoffeePrimary)
                }
                Text(
                    text = if (selectedTab == 0) "RESUMEN DE PAGOS" else "GESTIÓN DE PERSONAL",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = CoffeePrimary
                )
            }

            if (selectedTab == 0) {
                // El banner de totales que estaba en el fragment legacy
                TotalSummaryHeader(uiState.totalKg, uiState.totalAmount)
                CollectionList(uiState.collectionTotals)
            } else {
                CollectorsList(uiState.collectors, onArchiveCollector)
            }
        }
    }
}

@Composable
fun AdBannerPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("PUBLICIDAD", color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
fun TotalSummaryHeader(kg: Double, amount: Double) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = CoffeePrimary,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total Recolectado", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
                Text("${kg} Kg", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            VerticalDivider(color = Color.White.copy(alpha = 0.3f), modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total a Pagar", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
                Text("$${amount.toInt()}", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun AddCollectorDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Recolector", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeePrimary,
                    focusedLabelColor = CoffeePrimary
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name) },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeePrimary)
            ) {
                Text("GUARDAR")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = Color.Gray)
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecollectionScreenPreview() {
    RecconTheme {
        RecollectionContent(
            uiState = RecollectionUiState(
                collectors = listOf(
                    RecolectoresEntity(1, "Juan Perez", "active"),
                    RecolectoresEntity(2, "Maria Garcia", "active"),
                    RecolectoresEntity(3, "Pedro Gomez", "active")
                ),
                collectionTotals = listOf(
                    collecionTotalCollector(1, "Juan Perez", 150.0, 75000.0),
                    collecionTotalCollector(2, "Maria Garcia", 120.5, 60000.0)
                ),
                totalKg = 270.5,
                totalAmount = 135000.0
            ),
            onBack = {},
            onAddClick = {},
            onArchiveCollector = {}
        )
    }
}

@Composable
fun CollectionList(totals: List<collecionTotalCollector>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(totals) { item ->
            CollectionCard(item)
        }
    }
}

@Composable
fun CollectionCard(item: collecionTotalCollector) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = PastelRed
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = OnPastelRed
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name_recolector ?: "Desconocido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${item.kg_collection} Kg recolectados",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${item.price_total.toInt()}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = OnPastelRed
            )
        }
    }
}

@Composable
fun CollectorsList(
    collectors: List<RecolectoresEntity>,
    onArchive: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(collectors) { collector ->
            ListItem(
                headlineContent = { Text(collector.name, fontWeight = FontWeight.Bold) },
                supportingContent = { Text("Estado: ${collector.state.uppercase()}") },
                leadingContent = {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = PastelRed.copy(alpha = 0.5f)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp),
                            tint = OnPastelRed
                        )
                    }
                },
                trailingContent = {
                    TextButton(onClick = { collector.id?.let { onArchive(it) } }) {
                        Text("ARCHIVAR", color = Color.Gray, fontSize = 10.sp)
                    }
                },
                modifier = Modifier.background(Color.White, MaterialTheme.shapes.medium)
            )
        }
    }
}
