package com.rojasdev.apprecconproject.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rojasdev.apprecconproject.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GENERAR INFORMES", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OnPastelGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(CoffeeBackground),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Selecciona el tipo de reporte que deseas generar en formato PDF:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            item {
                ReportTypeCard(
                    title = "Reporte Mensual",
                    description = "Resumen completo de recolección y jornales del mes actual.",
                    icon = Icons.Default.CalendarMonth,
                    color = OnPastelGreen
                )
            }

            item {
                ReportTypeCard(
                    title = "Reporte Semanal",
                    description = "Detalle de actividades de los últimos 7 días.",
                    icon = Icons.Default.ViewWeek,
                    color = OnPastelBlue
                )
            }

            item {
                ReportTypeCard(
                    title = "Reporte Anual",
                    description = "Consolidado histórico de producción y costos del año.",
                    icon = Icons.Default.History,
                    color = OnPastelOrange
                )
            }
        }
    }
}

@Composable
fun ReportTypeCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = { /* Lógica de generación PDF */ }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = color.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    RecconTheme {
        ReportsScreen(onBack = {})
    }
}
