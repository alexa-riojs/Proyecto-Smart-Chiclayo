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
            .background(CyberDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Command Center Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberSurfaceCard)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(1.dp, NeonCyan, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Smart Chiclayo",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NeonCyan
                        )
                        Text(
                            text = "Command Center",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // System Online Status (Page 9)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(CyberSurface, RoundedCornerShape(20.dp))
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(20.dp))
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
                            text = "SYSTEM ONLINE 11:14:50 GMT-5",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

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

            // Quick Nav Pills / Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberSurface)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminNavChip("Dashboard", Icons.Default.Dashboard, selectedSidebarItem == "Dashboard") { selectedSidebarItem = "Dashboard" }
                AdminNavChip("Incidents (12)", Icons.Default.Warning, selectedSidebarItem == "Incidents") { selectedSidebarItem = "Incidents" }
                AdminNavChip("Cameras", Icons.Default.Videocam, selectedSidebarItem == "Cameras") { selectedSidebarItem = "Cameras" }
                AdminNavChip("Traffic Lights", Icons.Default.Traffic, selectedSidebarItem == "Traffic Lights") { selectedSidebarItem = "Traffic Lights" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Content Area
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Header (Matches Page 9)
                Text(
                    text = "Panel Administrativo",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "Real-time city metrics and infrastructure status.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // KPI Metrics Row (Matches Page 9)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Active Users Card
                    KpiMetricCard(
                        title = "Active Users",
                        value = activeUsersCount,
                        subtitle = "+5.2% vs last hour",
                        icon = Icons.Default.People,
                        iconTint = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )

                    // Active Drivers Card
                    KpiMetricCard(
                        title = "Active Drivers",
                        value = activeDriversCount,
                        subtitle = "92% utilization rate",
                        icon = Icons.Default.DirectionsCar,
                        iconTint = RoleTaxi,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Incident Status (24h) Card (Matches Page 9)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
                    border = BorderStroke(1.dp, CyberCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Incident Status (24h)",
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
                            IncidentMetricColumn(label = "REPORTED", value = "$reportedCount.", color = TextPrimary)
                            IncidentMetricColumn(label = "CONFIRMED", value = "$confirmedCount.", color = TrafficYellow)
                            IncidentMetricColumn(label = "RESOLVED", value = "$resolvedCount", color = TrafficGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Live Traffic Map Simulation Container (Matches Page 9)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
                    border = BorderStroke(1.dp, CyberCardBorder)
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
                            color = CyberDark.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, CyberCardBorder)
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
                                    text = "LIVE TRAFFIC MAP",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Camera Feeds Section (Matches Page 9)
                Text(
                    text = "Camera Feeds",
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

                // Traffic Light Network Status (Matches Page 9)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Traffic Light Network Status",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Text(
                        text = "View All Network",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4 Traffic Light Nodes Grid (Matches Page 9)
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

                Spacer(modifier = Modifier.height(24.dp))

                // Admin User info footer
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = CyberSurfaceCard,
                    border = BorderStroke(1.dp, CyberCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = AdminPurple.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, AdminPurple)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = AdminPurple, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Admin User", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Text("General Administrator • LAT: -6.7714 LON: -79.8409", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
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
        color = if (isSelected) NeonCyan.copy(alpha = 0.15f) else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) NeonCyan else CyberCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) NeonCyan else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) NeonCyanLight else TextSecondary
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
        colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
        border = BorderStroke(1.dp, CyberCardBorder)
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
        colors = CardDefaults.cardColors(containerColor = CyberSurface),
        border = BorderStroke(1.dp, CyberCardBorder)
    ) {
        Column {
            // Video Feed Simulation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .background(Color(0xFF07111A))
            ) {
                // Scanlines & Video overlay text
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
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "CHICLAYO CCTV // LIVE",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = NeonCyan.copy(alpha = 0.7f)
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
        color = CyberSurfaceCard,
        border = BorderStroke(1.dp, CyberCardBorder)
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

            // Indicator light dot
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
