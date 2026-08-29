package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TransitLine
import com.example.ui.theme.*

@Composable
fun TransitScreen(
    transitLines: List<TransitLine>,
    onSelectLine: (TransitLine) -> Unit,
    onOpenNotifications: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedLineId by remember { mutableStateOf<String?>(null) }

    val filteredLines = remember(transitLines, searchQuery) {
        if (searchQuery.isBlank()) transitLines
        else transitLines.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.routeName.contains(searchQuery, ignoreCase = true) ||
            it.number.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MinimalBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .statusBarsPadding()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Search shortcut */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Smart Chiclayo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MinimalPrimary
                )

                IconButton(
                    onClick = onOpenNotifications,
                    modifier = Modifier.testTag("transit_notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Screen Header Title
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Transporte\nPúblico",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 34.sp
                    ),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TrafficGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Monitoreo en tiempo real - Chiclayo Centro",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transit_search_field"),
                    placeholder = { Text("Buscar línea o destino...", color = TextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = TextSecondary)
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MinimalPrimary,
                        unfocusedBorderColor = MinimalBorder,
                        focusedContainerColor = MinimalSurface,
                        unfocusedContainerColor = MinimalSurface,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Transit Line Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredLines, key = { it.id }) { line ->
                    val isExpanded = expandedLineId == line.id
                    TransitLineCard(
                        line = line,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedLineId = if (isExpanded) null else line.id
                            onSelectLine(line)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransitLineCard(
    line: TransitLine,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .testTag("transit_card_${line.number.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MinimalSurface),
        border = BorderStroke(1.dp, if (line.number == "12") MinimalPrimary.copy(alpha = 0.6f) else MinimalBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row: Badge, Name, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Line Number Badge
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(line.badgeColor),
                    border = BorderStroke(1.dp, MinimalBorderSubtle)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = line.number,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = if (line.badgeColor == 0xFF00E5FF.toLong()) Color(0xFF004D40) else if (line.badgeColor == 0xFFECEFF1.toLong()) Color.Black else Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = line.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = line.routeName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                // Status Badge (En camino / Retraso / En ruta)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (line.hasDelay) TrafficRed.copy(alpha = 0.12f) else MinimalSurfaceVariant,
                    border = BorderStroke(1.dp, if (line.hasDelay) TrafficRed.copy(alpha = 0.4f) else MinimalBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (line.hasDelay) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Alerta",
                                tint = TrafficRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (line.number == "12") MinimalPrimary else TrafficGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        Text(
                            text = line.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (line.hasDelay) TrafficRed else TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MinimalBorderSubtle)
            Spacer(modifier = Modifier.height(12.dp))

            // Metrics: DISTANCIA and TIEMPO EST.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "DISTANCIA",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = line.distanceMeters,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "TIEMPO EST.",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = line.estimatedTime,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (line.number == "12") MinimalPrimary else TextPrimary
                        )
                    )
                }
            }

            // Expanded details
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MinimalSurfaceVariant,
                        border = BorderStroke(1.dp, MinimalBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Próxima unidad:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(line.nextBusPlate, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Ubicación actual:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(line.currentStreet, style = MaterialTheme.typography.bodySmall, color = MinimalPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Frecuencia:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(line.frequencyMinutes, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
