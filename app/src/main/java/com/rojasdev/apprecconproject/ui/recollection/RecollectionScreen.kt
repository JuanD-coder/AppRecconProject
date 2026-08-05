package com.rojasdev.apprecconproject.ui.recollection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.ui.components.EmptyState
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
    val accentColor = ThunderbirdLegacy

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = accentColor,
                contentColor = Color.White,
                actions = {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Payments, contentDescription = null) },
                        label = { Text("Pagos", fontFamily = Comfortaa, color = Color.White) },
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
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
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
                    FloatingActionButton(
                        onClick = onAddClick,
                        containerColor = Color.White,
                        contentColor = accentColor,
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                        }
                        Text(
                            text = if (selectedTab == 0) "RESUMEN DE PAGOS" else "GESTIÓN DE PERSONAL",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = Comfortaa,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    if (selectedTab == 0) {
                        TotalSummaryHeader(uiState.totalKg, uiState.totalAmount)
                    }
                }
            }

            if (selectedTab == 0) {
                if (uiState.collectionTotals.isEmpty()) {
                    EmptyState("No hay pagos registrados todavía")
                } else {
                    CollectionList(uiState.collectionTotals)
                }
            } else {
                if (uiState.collectors.isEmpty()) {
                    EmptyState("No hay personal registrado")
                } else {
                    CollectorsList(uiState.collectors, onArchiveCollector)
                }
            }
        }
    }
}

@Composable
fun TotalSummaryHeader(kg: Double, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Text(
                text = "${kg} Kg",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Recolectado",
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
fun AddCollectorDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Recolector", fontFamily = Comfortaa, fontWeight = FontWeight.Black) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Completo", fontFamily = Comfortaa) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ThunderbirdLegacy,
                    focusedLabelColor = ThunderbirdLegacy
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name) },
                colors = ButtonDefaults.buttonColors(containerColor = ThunderbirdLegacy)
            ) {
                Text("GUARDAR", fontFamily = Comfortaa, fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", fontFamily = Comfortaa, color = Color.Gray)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = ThunderbirdLegacy.copy(alpha = 0.1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_recolector),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = ThunderbirdLegacy
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name_recolector ?: "Desconocido",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = Comfortaa,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${item.kg_collection} Kg recolectados",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = Comfortaa,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${item.price_total.toInt()}",
                style = MaterialTheme.typography.titleLarge,
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Black,
                color = ThunderbirdLegacy
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
                headlineContent = { Text(collector.name, fontFamily = Comfortaa, fontWeight = FontWeight.Black) },
                supportingContent = { Text("Estado: ${collector.state.uppercase()}", fontFamily = Comfortaa) },
                leadingContent = {
                    Surface(
                        shape = CircleShape,
                        color = ThunderbirdLegacy.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_recolector),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = ThunderbirdLegacy
                        )
                    }
                },
                trailingContent = {
                    TextButton(onClick = { collector.id?.let { onArchive(it) } }) {
                        Text("ARCHIVAR", fontFamily = Comfortaa, color = Color.Gray, fontSize = 10.sp)
                    }
                },
                colors = ListItemDefaults.colors(containerColor = Color.White),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )
        }
    }
}
