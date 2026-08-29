package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RouteOption
import com.example.ui.components.ChiclayoMapCanvas
import com.example.ui.theme.*

@Composable
fun RouteOptionsScreen(
    origin: String,
    destination: String,
    routes: List<RouteOption>,
    selectedRoute: RouteOption?,
    onSelectRoute: (RouteOption) -> Unit,
    onStartNavigation: () -> Unit,
    onBack: () -> Unit
) {
    var activeRoute by remember { mutableStateOf(selectedRoute ?: routes.firstOrNull()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MinimalBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
        ) {
            // Top Half: Map View with Route Lines
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.46f)
            ) {
                ChiclayoMapCanvas(
                    modifier = Modifier.fillMaxSize(),
                    showActiveRoute = true
                )

                // Top Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MinimalSurface.copy(alpha = 0.95f),
                        border = BorderStroke(1.dp, MinimalBorder),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.testTag("route_options_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Atrás",
                                    tint = TextPrimary
                                )
                            }

                            Text(
                                text = "Opciones de Ruta",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )

                            IconButton(onClick = { /* Filter */ }) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Filtros",
                                    tint = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Origin & Destination Floating Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MinimalSurface.copy(alpha = 0.96f)),
                        border = BorderStroke(1.dp, MinimalBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Origin
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(MinimalPrimary)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "ORIGEN",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextMuted
                                    )
                                    Text(
                                        text = origin,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = TextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = MinimalBorderSubtle)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Destination
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(TrafficRed)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "DESTINO",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextMuted
                                    )
                                    Text(
                                        text = destination,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Half: Routes Found Sheet
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.54f)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                border = BorderStroke(1.dp, MinimalBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "3 Rutas Encontradas",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Route Cards
                    routes.forEach { route ->
                        val isSelected = activeRoute?.id == route.id
                        RouteItemCard(
                            route = route,
                            isSelected = isSelected,
                            onSelect = {
                                activeRoute = route
                                onSelectRoute(route)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Button: Iniciar Ruta
                    Button(
                        onClick = onStartNavigation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_navigation_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MinimalPrimary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = "Iniciar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "▲ Iniciar Ruta",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteItemCard(
    route: RouteOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("route_card_${route.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MinimalPrimaryContainer else MinimalSurfaceVariant
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) MinimalPrimary else MinimalBorderSubtle
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) MinimalOnPrimaryContainer else TextPrimary
                )

                if (route.tag.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (route.isFastest) MinimalPrimary else MinimalSurface,
                        border = BorderStroke(1.dp, if (route.isFastest) MinimalPrimary else MinimalBorderSubtle)
                    ) {
                        Text(
                            text = route.tag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (route.isFastest) Color.White else MinimalPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${route.durationMinutes} min",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "  •  ${route.distanceKm} km",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (route.trafficCondition.contains("FLUID") || route.trafficCondition.contains("FLUIDO")) TrafficGreen else TrafficYellow)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = route.trafficCondition,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Detalles",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
