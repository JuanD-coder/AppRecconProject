package com.rojasdev.apprecconproject.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.legacy.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.legacy.data.dataModel.allWorkAndCollector
import com.rojasdev.apprecconproject.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AnalyticsContent(
        uiState = uiState,
        onBack = onBack,
        onDateSelected = { viewModel.onDateSelected(it) },
        onNextMonth = { viewModel.nextMonth() },
        onPreviousMonth = { viewModel.previousMonth() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsContent(
    uiState: AnalyticsUiState,
    onBack: () -> Unit,
    onDateSelected: (String) -> Unit,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CONTABILIDAD", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export PDF */ }) {
                        Icon(Icons.Default.PictureAsPdf, "Exportar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OnPastelGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(CoffeeBackground)
        ) {
            // Calendar Section (Esencia Legacy)
            item {
                CalendarSection(
                    uiState = uiState,
                    onNextMonth = onNextMonth,
                    onPreviousMonth = onPreviousMonth,
                    onDateSelected = onDateSelected
                )
            }

            item {
                HorizontalDivider(Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray)
            }

            // Summary for Selected Day
            item {
                DaySummaryHeader(uiState)
            }

            // Recolección Section
            if (uiState.dailyCollectionRecords.isNotEmpty()) {
                item {
                    SectionHeader("RECOLECCIÓN", OnPastelRed)
                }
                items(uiState.dailyCollectionRecords) { record ->
                    CollectionRecordItem(record)
                }
            }

            // Trabajos Section
            if (uiState.dailyWorkRecords.isNotEmpty()) {
                item {
                    SectionHeader("TRABAJOS / JORNALES", OnPastelOrange)
                }
                items(uiState.dailyWorkRecords) { record ->
                    WorkRecordItem(record)
                }
            }

            if (uiState.dailyCollectionRecords.isEmpty() && uiState.dailyWorkRecords.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("No hay registros para este día", color = Color.Gray)
                    }
                }
            }
            
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun CalendarSection(
    uiState: AnalyticsUiState,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Month Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = OnPastelGreen)
            }
            Text(
                text = "${uiState.monthName} ${uiState.currentYear}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnPastelGreen
            )
            IconButton(onClick = onNextMonth) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = OnPastelGreen)
            }
        }

        Spacer(Modifier.height(8.dp))

        // Days of week header
        Row(Modifier.fillMaxWidth()) {
            val days = listOf("L", "M", "M", "J", "V", "S", "D")
            days.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Days Grid
        // Note: Simple grid for now, ignoring week start offset for simplicity in preview
        // but it will follow the days generated by ViewModel.
        val chunks = uiState.calendarDays.chunked(7)
        chunks.forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    CalendarDayItem(
                        day = day,
                        onDateSelected = onDateSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty slots if last week is short
                if (week.size < 7) {
                    repeat(7 - week.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    day: CalendarDay,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(
                color = if (day.isSelected) OnPastelGreen else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onDateSelected(day.date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.dayOfMonth,
            color = if (day.isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (day.isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Row {
            if (day.hasCollection) {
                Box(Modifier.size(4.dp).background(OnPastelRed, CircleShape))
            }
            if (day.hasWork) {
                Spacer(Modifier.width(2.dp))
                Box(Modifier.size(4.dp).background(OnPastelOrange, CircleShape))
            }
        }
    }
}

@Composable
fun DaySummaryHeader(uiState: AnalyticsUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryCard(
            label = "Recolección",
            value = "${uiState.totalKg} Kg",
            money = uiState.totalCollectionMoney.toInt(),
            color = OnPastelRed,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            label = "Jornales",
            value = "${uiState.totalWorkDays.toInt()} Días",
            money = uiState.totalWorkMoney.toInt(),
            color = OnPastelOrange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SummaryCard(label: String, value: String, money: Int, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = color)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("$${money}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
fun CollectionRecordItem(record: allCollecionAndCollector) {
    ListItem(
        headlineContent = { Text(record.name_recolector ?: "N/A", fontWeight = FontWeight.Bold) },
        supportingContent = { Text("${record.Cantidad} Kg recolectados") },
        trailingContent = { Text("$${record.result.toInt()}", fontWeight = FontWeight.Black, color = OnPastelRed) },
        leadingContent = {
            val icon = if (record.Alimentacion == "yes") Icons.Default.Restaurant else Icons.Default.NoFood
            Surface(
                shape = CircleShape,
                color = OnPastelRed.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(icon, null, modifier = Modifier.padding(10.dp), tint = OnPastelRed)
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.White),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).background(Color.White, MaterialTheme.shapes.small)
    )
}

@Composable
fun WorkRecordItem(record: allWorkAndCollector) {
    ListItem(
        headlineContent = { Text(record.name_recolector ?: "N/A", fontWeight = FontWeight.Bold) },
        supportingContent = { Text(record.actividad ?: "Trabajo") },
        trailingContent = { Text("$${record.result.toInt()}", fontWeight = FontWeight.Black, color = OnPastelOrange) },
        leadingContent = {
            Surface(
                shape = CircleShape,
                color = OnPastelOrange.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Engineering, null, modifier = Modifier.padding(10.dp), tint = OnPastelOrange)
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.White),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).background(Color.White, MaterialTheme.shapes.small)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalyticsScreenPreview() {
    RecconTheme {
        AnalyticsContent(
            uiState = AnalyticsUiState(
                monthName = "JULIO",
                currentYear = 2026,
                selectedDate = "2026-07-30",
                calendarDays = List(31) { i ->
                    CalendarDay(
                        "2026-07-${i+1}",
                        (i+1).toString(),
                        isSelected = i == 29,
                        hasCollection = i % 3 == 0,
                        hasWork = i % 5 == 0
                    )
                },
                dailyCollectionRecords = listOf(
                    allCollecionAndCollector(1, "Juan Perez", 1, 150.0, 75000.0, 500.0, "active", "yes", "2026-07-30", 1)
                ),
                dailyWorkRecords = listOf(
                    allWorkAndCollector(2, "Maria Garcia", 1, 1.0, 45000.0, 45000.0, "2026-07-30", 1, "Deshierbe")
                ),
                totalKg = 150.0,
                totalCollectionMoney = 75000.0,
                totalWorkDays = 1.0,
                totalWorkMoney = 45000.0
            ),
            onBack = {},
            onDateSelected = {},
            onNextMonth = {},
            onPreviousMonth = {}
        )
    }
}
