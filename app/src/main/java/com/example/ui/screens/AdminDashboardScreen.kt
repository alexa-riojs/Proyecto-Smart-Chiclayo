package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.ChiclayoMapCanvas
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    userProfile: UserProfile,
    trafficLightNodes: List<TrafficLightNode>,
    cameraFeeds: List<CameraFeed>,
    incidents: List<IncidentReport>,
    activeUsersCount: String,
    activeDriversCount: String,
    reportedCount: Int,
    confirmedCount: Int,
    resolvedCount: Int,
    onToggleTrafficLight: (String) -> Unit,
    onSwitchToCitizenView: () -> Unit
) {
    var selectedSidebarItem by remember { mutableStateOf("Dashboard") }

    // Live blinking recording dot
    val infiniteTransition = rememberInfiniteTransition(label = "rec")
    val recAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "recAlpha"
    )

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
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Command Center Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MinimalSurface,
                border = BorderStroke(1.dp, MinimalBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MinimalPrimaryContainer)
                                .border(1.dp, MinimalPrimary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = MinimalPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Smart Chiclayo",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MinimalPrimary
                            )
                            Text(
                                text = "Centro de Control",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // System Online Status
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MinimalSurfaceVariant, RoundedCornerShape(20.dp))
                                .border(1.dp, MinimalBorderSubtle, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(TrafficGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ONLINE",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onSwitchToCitizenView,
                            modifier = Modifier.testTag("exit_admin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Salir de Admin",
                                tint = TextSecondary
                            )
                        }
                    }
                }
            }

            // Quick Nav Pills / Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MinimalSurfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminNavChip("Dashboard", Icons.Default.Dashboard, selectedSidebarItem == "Dashboard") { selectedSidebarItem = "Dashboard" }
                AdminNavChip("Incidencias", Icons.Default.Warning, selectedSidebarItem == "Incidents") { selectedSidebarItem = "Incidents" }
                AdminNavChip("Cámaras", Icons.Default.Videocam, selectedSidebarItem == "Cameras") { selectedSidebarItem = "Cameras" }
                AdminNavChip("Semáforos", Icons.Default.Traffic, selectedSidebarItem == "Traffic Lights") { selectedSidebarItem = "Traffic Lights" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Content Area
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Panel Administrativo",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "Métricas urbanas en tiempo real y estado de infraestructura.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // KPI Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiMetricCard(
                        title = "Usuarios Activos",
                        value = activeUsersCount,
                        subtitle = "+5.2% vs última hora",
                        icon = Icons.Default.People,
                        iconTint = MinimalPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    KpiMetricCard(
                        title = "Conductores",
                        value = activeDriversCount,
                        subtitle = "92% en ruta",
                        icon = Icons.Default.DirectionsCar,
                        iconTint = MinimalSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Incident Status (24h) Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                    border = BorderStroke(1.dp, MinimalBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado de Incidencias (24h)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextSecondary
                            )
                            Icon(Icons.Default.Warning, contentDescription = null, tint = TrafficYellow, modifier = Modifier.size(16.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IncidentMetricColumn(label = "REPORTADAS", value = "$reportedCount", color = TextPrimary)
                            IncidentMetricColumn(label = "CONFIRMADAS", value = "$confirmedCount", color = TrafficYellow)
                            IncidentMetricColumn(label = "RESUELTAS", value = "$resolvedCount", color = TrafficGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Live Traffic Map Simulation Container
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                    border = BorderStroke(1.dp, MinimalBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ChiclayoMapCanvas(
                            modifier = Modifier.fillMaxSize(),
                            layerMode = MapLayerMode.TRAFFIC,
                            showActiveRoute = true,
                            incidents = incidents
                        )

                        // Top Overlay Pill
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MinimalSurface.copy(alpha = 0.92f),
                            border = BorderStroke(1.dp, MinimalBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(TrafficGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MAPA EN VIVO CHICLAYO",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Camera Feeds Section
                Text(
                    text = "Cámaras de Vigilancia",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    cameraFeeds.take(2).forEach { cam ->
                        CctvCameraPreviewCard(
                            camera = cam,
                            recAlpha = recAlpha,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Traffic Light Network Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Red de Semáforos",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Text(
                        text = "Control Central",
                        style = MaterialTheme.typography.labelSmall,
                        color = MinimalPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 4 Traffic Light Nodes Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    trafficLightNodes.forEach { node ->
                        TrafficLightNodeCard(
                            node = node,
                            modifier = Modifier.weight(1f),
                            onClick = { onToggleTrafficLight(node.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Admin User info footer
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MinimalSurface,
                    border = BorderStroke(1.dp, MinimalBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = MinimalPrimaryContainer,
                            border = BorderStroke(1.dp, MinimalPrimary)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MinimalPrimary, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(userProfile.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Text("Administrador General • Chiclayo (-6.7714, -79.8409)", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun AdminNavChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) MinimalPrimaryContainer else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) MinimalPrimary else MinimalBorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) MinimalPrimary else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MinimalOnPrimaryContainer else TextSecondary
                )
            )
        }
    }
}

@Composable
private fun KpiMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MinimalSurface),
        border = BorderStroke(1.dp, MinimalBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = if (subtitle.startsWith("+")) TrafficGreen else TextMuted
            )
        }
    }
}

@Composable
private fun IncidentMetricColumn(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TextMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = color)
    }
}

@Composable
private fun CctvCameraPreviewCard(
    camera: CameraFeed,
    recAlpha: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MinimalSurface),
        border = BorderStroke(1.dp, MinimalBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Video Feed Simulation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .background(Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(TrafficRed.copy(alpha = recAlpha))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "REC | ${camera.id}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = Color.White
                        )
                    }

                    Text(
                        text = "CHICLAYO CCTV // EN VIVO",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = camera.locationName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = camera.trafficDensity,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun TrafficLightNodeCard(
    node: TrafficLightNode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("node_${node.id.lowercase().replace(" ", "_").replace("-", "_")}"),
        color = MinimalSurfaceVariant,
        border = BorderStroke(1.dp, MinimalBorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = node.id,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = node.locationName,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            val lightColor = when (node.state) {
                TrafficLightState.GREEN -> TrafficGreen
                TrafficLightState.YELLOW -> TrafficYellow
                TrafficLightState.RED -> TrafficRed
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(lightColor)
                    .border(1.dp, lightColor.copy(alpha = 0.5f), CircleShape)
            )
        }
    }
}
