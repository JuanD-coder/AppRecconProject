package com.rojasdev.apprecconproject.ui.analytics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.legacy.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.legacy.data.dataModel.allWorkAndCollector
import com.rojasdev.apprecconproject.ui.components.EmptyState
import com.rojasdev.apprecconproject.ui.components.RecconTopBar
import com.rojasdev.apprecconproject.ui.components.SectionTitle
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
            RecconTopBar(
                title = "CONTABILIDAD",
                accent = HippieGreenLegacy,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { /* Export PDF */ }) {
                        Icon(Icons.Default.PictureAsPdf, "Exportar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE7E7E7)) // gray_light from legacy
        ) {
            // Hero Section (Esencia Legacy)
            item {
                LegacyAnalyticsHeader(
                    uiState = uiState,
                    onNextMonth = onNextMonth,
                    onPreviousMonth = onPreviousMonth,
                    onDateSelected = onDateSelected
                )
            }

            // Summary for Selected Day (Legacy Evolution)
            item {
                DaySummaryHeader(uiState)
            }

            // Recolección Section
            if (uiState.dailyCollectionRecords.isNotEmpty()) {
                item {
                    SectionTitle("RECOLECCIÓN", ThunderbirdLegacy)
                }
                items(uiState.dailyCollectionRecords) { record ->
                    CollectionRecordItem(record)
                }
            }

            // Trabajos Section
            if (uiState.dailyWorkRecords.isNotEmpty()) {
                item {
                    SectionTitle("TRABAJOS / JORNALES", OrangeLegacy)
                }
                items(uiState.dailyWorkRecords) { record ->
                    WorkRecordItem(record)
                }
            }

            if (uiState.dailyCollectionRecords.isEmpty() && uiState.dailyWorkRecords.isEmpty()) {
                item {
                    EmptyState("No hay registros para este día")
                }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun LegacyAnalyticsHeader(
    uiState: AnalyticsUiState,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = HippieGreenLegacy,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(bottom = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                CalendarSection(
                    uiState = uiState,
                    onNextMonth = onNextMonth,
                    onPreviousMonth = onPreviousMonth,
                    onDateSelected = onDateSelected
                )
            }

            Spacer(Modifier.height(16.dp))

            // Totals (Large like tvShowDates/tvShowPay)
            Text(
                text = "Recolección: \n ${uiState.totalKg} Kg",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 35.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                ),
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Pago: \n $${uiState.totalCollectionMoney.toInt()}",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 35.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                ),
                fontWeight = FontWeight.Black,
                color = Color.White
            )
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
    Column(modifier = Modifier.padding(8.dp)) {
        // Title (Calendar Collection)
        Text(
            text = "CALENDARIO DE RECOLECCIÓN",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = Color.Black
        )

        Spacer(Modifier.height(8.dp))

        // Month Navigation (Style Bar from legacy)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE7E7E7)) // bar style
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Color.Black)
            }
            Text(
                text = "${uiState.monthName} ${uiState.currentYear}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
            IconButton(onClick = onNextMonth) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Black)
            }
        }

        Spacer(Modifier.height(8.dp))

        // Days of week header (with domingo in Cinnabar)
        Row(Modifier.fillMaxWidth()) {
            val days = listOf("L", "M", "M", "J", "V", "S", "D")
            days.forEachIndexed { index, day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = if (index == 6) CinnabarLegacy else Color.Black
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color.Black, thickness = 1.dp)

        // Days Grid
        val chunks = uiState.calendarDays.chunked(7)
        chunks.forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEachIndexed { index, day ->
                    CalendarDayItem(
                        day = day,
                        onDateSelected = onDateSelected,
                        isSunday = index == 6,
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
    isSunday: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .background(
                color = if (day.isSelected) HippieGreenLegacy else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onDateSelected(day.date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.dayOfMonth,
            color = when {
                day.isSelected -> Color.White
                isSunday -> CinnabarLegacy
                else -> Color.Black
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Black
        )
        Row {
            if (day.hasCollection) {
                Box(Modifier.size(4.dp).background(ThunderbirdLegacy, CircleShape))
            }
            if (day.hasWork) {
                Spacer(Modifier.width(2.dp))
                Box(Modifier.size(4.dp).background(OrangeLegacy, CircleShape))
            }
        }
    }
}

@Composable
fun DaySummaryHeader(uiState: AnalyticsUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_recolector),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "RESUMEN DIARIO",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = HippieGreenLegacy
            )
        }
    }
}

@Composable
fun CollectionRecordItem(record: allCollecionAndCollector) {
    ListItem(
        headlineContent = { Text(record.name_recolector ?: "N/A", fontFamily = Comfortaa, fontWeight = FontWeight.Black) },
        supportingContent = { Text("${record.Cantidad} Kg recolectados", fontFamily = Comfortaa) },
        trailingContent = { Text("$${record.result.toInt()}", fontFamily = Comfortaa, fontWeight = FontWeight.Black, color = ThunderbirdLegacy) },
        leadingContent = {
            Surface(
                shape = CircleShape,
                color = ThunderbirdLegacy.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_kilogramo),
                    null,
                    modifier = Modifier.padding(10.dp),
                    tint = ThunderbirdLegacy
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.White),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
    )
}

@Composable
fun WorkRecordItem(record: allWorkAndCollector) {
    ListItem(
        headlineContent = { Text(record.name_recolector ?: "N/A", fontFamily = Comfortaa, fontWeight = FontWeight.Black) },
        supportingContent = { Text(record.actividad ?: "Trabajo", fontFamily = Comfortaa) },
        trailingContent = { Text("$${record.result.toInt()}", fontFamily = Comfortaa, fontWeight = FontWeight.Black, color = OrangeLegacy) },
        leadingContent = {
            Surface(
                shape = CircleShape,
                color = OrangeLegacy.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_recolector),
                    null,
                    modifier = Modifier.padding(10.dp),
                    tint = OrangeLegacy
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.White),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
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