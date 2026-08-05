package com.rojasdev.apprecconproject.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.ui.components.RecconTopBar
import com.rojasdev.apprecconproject.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RecconTopBar(
                title = "GENERAR INFORMES",
                accent = HippieGreenLegacy,
                onBack = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE7E7E7)), // gray_light
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Selecciona el tipo de reporte que deseas generar en formato PDF:",
                    fontFamily = Comfortaa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            item {
                ReportTypeCard(
                    title = "Reporte Semanal",
                    description = "Detalle de actividades de los últimos 7 días.",
                    color = Color(0xFF1976D2)
                )
            }

            item {
                ReportTypeCard(
                    title = "Reporte Mensual",
                    description = "Resumen completo de recolección y jornales del mes actual.",
                    color = HippieGreenLegacy
                )
            }

            item {
                ReportTypeCard(
                    title = "Reporte Anual",
                    description = "Consolidado histórico de producción y costos del año.",
                    color = OrangeLegacy
                )
            }
        }
    }
}

@Composable
fun ReportTypeCard(
    title: String,
    description: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF434342)), // dark gray from legacy
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Comfortaa,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            // Content
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pdf_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = Comfortaa,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { /* Lógica PDF */ },
                    colors = ButtonDefaults.buttonColors(containerColor = color),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("CREAR", fontSize = 10.sp, fontFamily = Comfortaa, fontWeight = FontWeight.Black)
                }
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