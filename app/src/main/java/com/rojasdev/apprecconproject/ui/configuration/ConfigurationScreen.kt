package com.rojasdev.apprecconproject.ui.configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity
import com.rojasdev.apprecconproject.ui.theme.*

@Composable
fun ConfigurationScreen(
    onBack: () -> Unit,
    viewModel: ConfigurationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showUpdateDialog by remember { mutableStateOf<SettingEntity?>(null) }
    var showAddWorkDialog by remember { mutableStateOf(false) }

    if (showUpdateDialog != null) {
        UpdatePriceDialog(
            setting = showUpdateDialog!!,
            onDismiss = { showUpdateDialog = null },
            onConfirm = { newPrice ->
                viewModel.updatePrice(showUpdateDialog!!, newPrice)
                showUpdateDialog = null
            }
        )
    }

    if (showAddWorkDialog) {
        AddWorkPriceDialog(
            onDismiss = { showAddWorkDialog = false },
            onConfirm = { name, price ->
                viewModel.addWorkPrice(name, price)
                showAddWorkDialog = false
            }
        )
    }

    ConfigurationContent(
        uiState = uiState,
        onBack = onBack,
        onUpdatePrice = { showUpdateDialog = it },
        onAddWork = { showAddWorkDialog = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationContent(
    uiState: ConfigurationUiState,
    onBack: () -> Unit,
    onUpdatePrice: (SettingEntity) -> Unit,
    onAddWork: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CONFIGURACIÓN", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
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
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Recolección Section (Rojo)
            SettingsSectionHeader("PRECIOS DE RECOLECCIÓN", OnPastelRed)
            
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.priceYesAliment?.let {
                    ActivePriceCard(it, "Con Alim.", Icons.Default.Restaurant, OnPastelRed, Modifier.weight(1f)) { onUpdatePrice(it) }
                }
                uiState.priceNoAliment?.let {
                    ActivePriceCard(it, "Sin Alim.", Icons.Default.NoFood, OnPastelRed, Modifier.weight(1f)) { onUpdatePrice(it) }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 2. Trabajos Section (Naranja)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsSectionHeader("PRECIOS DE JORNALES", OnPastelOrange, paddingHorizontal = 0.dp)
                if (uiState.canAddWorkPrice) {
                    TextButton(onClick = onAddWork) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = OnPastelOrange)
                        Spacer(Modifier.width(4.dp))
                        Text("AÑADIR", color = OnPastelOrange, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.workPrices.forEach { price ->
                    WorkPriceItem(price) { onUpdatePrice(price) }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 3. Historial Section (Gris)
            SettingsSectionHeader("HISTORIAL DE PRECIOS", Color.Gray)
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(100.dp)
            ) {
                items(uiState.archivedPrices) { price ->
                    HistoryPriceCard(price)
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String, color: Color, paddingHorizontal: androidx.compose.ui.unit.Dp = 16.dp) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = paddingHorizontal, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = color
    )
}

@Composable
fun ActivePriceCard(
    setting: SettingEntity,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier,
    onUpdate: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(icon, null, modifier = Modifier.padding(8.dp), tint = color)
            }
            Spacer(Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text("$${setting.cost}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = color)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onUpdate,
                modifier = Modifier.fillMaxWidth().height(32.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color),
                shape = MaterialTheme.shapes.small
            ) {
                Text("CAMBIAR", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WorkPriceItem(price: SettingEntity, onUpdate: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onUpdate() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Engineering, null, tint = OnPastelOrange, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(price.feeding, fontWeight = FontWeight.Bold)
                Text("Vigente desde: ${price.date}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Text("$${price.cost}", fontWeight = FontWeight.Black, color = OnPastelOrange)
        }
    }
}

@Composable
fun HistoryPriceCard(price: SettingEntity) {
    Card(
        modifier = Modifier.width(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                if (price.feeding == "yes") "Con Alim." else if (price.feeding == "no") "Sin Alim." else price.feeding,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text("$${price.cost}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.Gray)
            Text(price.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 9.sp)
        }
    }
}

@Composable
fun UpdatePriceDialog(setting: SettingEntity, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var price by remember { mutableStateOf(setting.cost.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Precio") },
        text = {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Nuevo Valor ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { price.toIntOrNull()?.let { onConfirm(it) } }) { Text("ACTUALIZAR") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCELAR") } }
    )
}

@Composable
fun AddWorkPriceDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Precio de Trabajo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Actividad (Ej: Poda)") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Valor ($)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            Button(onClick = { 
                val p = price.toIntOrNull()
                if (name.isNotBlank() && p != null) onConfirm(name, p) 
            }) { Text("AGREGAR") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCELAR") } }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfigurationScreenPreview() {
    RecconTheme {
        ConfigurationContent(
            uiState = ConfigurationUiState(
                priceYesAliment = SettingEntity(1, "yes", 500, "active", "2026-01-01"),
                priceNoAliment = SettingEntity(2, "no", 600, "active", "2026-01-01"),
                workPrices = listOf(SettingEntity(3, "Jornal Diario", 45000, "active", "2026-01-01")),
                archivedPrices = listOf(SettingEntity(4, "yes", 450, "archived", "2025-12-01")),
                canAddWorkPrice = true
            ),
            onBack = {},
            onUpdatePrice = {},
            onAddWork = {}
        )
    }
}
