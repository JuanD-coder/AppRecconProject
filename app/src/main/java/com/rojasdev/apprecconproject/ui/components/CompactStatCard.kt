package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rojasdev.apprecconproject.ui.theme.RecconTheme
import com.rojasdev.apprecconproject.ui.theme.ThunderbirdLegacy

@Composable
fun CompactStatCard(
    label: String,
    value: String,
    money: Int,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = accent)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("$${money}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black, color = accent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompactStatCardPreview() {
    RecconTheme {
        CompactStatCard(
            label = "Total Recolectado",
            value = "1,240 Kg",
            money = 850000,
            accent = ThunderbirdLegacy,
            modifier = Modifier.padding(16.dp)
        )
    }
}
