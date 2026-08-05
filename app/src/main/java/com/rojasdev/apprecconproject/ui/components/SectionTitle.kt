package com.rojasdev.apprecconproject.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.rojasdev.apprecconproject.ui.theme.Spacing

@Composable
fun SectionTitle(
    title: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = accent
    )
}
