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
            .background(CyberDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Half: Map View with Route Lines
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.48f)
            ) {
                ChiclayoMapCanvas(
                    modifier = Modifier.fillMaxSize(),
                    showActiveRoute = true
                )

                // Top Bar (Matches Page 4)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            text = "Route Options",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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

                    Spacer(modifier = Modifier.height(6.dp))

                    // Origin & Destination Floating Card (Page 4)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard.copy(alpha = 0.95f)),
                        border = BorderStroke(1.dp, CyberCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Origin
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "ORIGIN",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = origin,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = TextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = CyberCardBorder)
                            Spacer(modifier = Modifier.height(10.dp))

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
                                        text = "DESTINATION",
                                        style = MaterialTheme.typography.labelSmall,
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

            // Bottom Half: 3 Routes Found Sheet (Matches Page 4)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.52f)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CyberSurface),
                border = BorderStroke(1.dp, CyberCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "3 Routes Found",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Button: Iniciar Ruta (Page 4)
                    Button(
                        onClick = onStartNavigation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_navigation_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MinimalPrimary,
                            contentColor = Color.White
                        )
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
            containerColor = if (isSelected) CyberSurfaceElevated else CyberSurfaceCard
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) NeonCyan else CyberCardBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                if (route.tag.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (route.isFastest) NeonCyan else CyberSurface,
                        border = BorderStroke(1.dp, if (route.isFastest) NeonCyan else CyberCardBorder)
                    ) {
                        Text(
                            text = route.tag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (route.isFastest) CyberDark else NeonCyan
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

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
                            .background(if (route.trafficCondition.contains("FLUID")) TrafficGreen else TrafficYellow)
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
