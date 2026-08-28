package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IncidentReport
import com.example.data.MapLayerMode
import com.example.data.UserProfile
import com.example.ui.components.ChiclayoMapCanvas
import com.example.ui.theme.*

@Composable
fun LiveMapScreen(
    userProfile: UserProfile,
    incidents: List<IncidentReport>,
    layerMode: MapLayerMode,
    onOpenDrawer: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenProfile: () -> Unit,
    onSearchClick: () -> Unit,
    onDestinationSelect: (String) -> Unit,
    onReportIncidentClick: () -> Unit,
    onToggleLayerMode: () -> Unit
) {
    var showQuickSearchDialog by remember { mutableStateOf(false) }

    if (showQuickSearchDialog) {
        AlertDialog(
            onDismissRequest = { showQuickSearchDialog = false },
            containerColor = CyberSurfaceElevated,
            title = {
                Text("¿A dónde deseas ir en Chiclayo?", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    QuickSearchItem("Centro Comercial Real Plaza", "Av. Bolognesi / Panamericana") {
                        showQuickSearchDialog = false
                        onDestinationSelect("Centro Comercial Real Plaza")
                    }
                    QuickSearchItem("Mercado Modelo de Chiclayo", "Av. Balta y Av. Arica") {
                        showQuickSearchDialog = false
                        onDestinationSelect("Mercado Modelo de Chiclayo")
                    }
                    QuickSearchItem("Parque Principal / Plaza de Armas", "Centro Histórico") {
                        showQuickSearchDialog = false
                        onDestinationSelect("Plaza de Armas")
                    }
                    QuickSearchItem("Universidad Señor de Sipán", "Carretera a Pimentel") {
                        showQuickSearchDialog = false
                        onDestinationSelect("Universidad Señor de Sipán")
                    }
                    QuickSearchItem("Aeropuerto Cap. FAP José Quiñones", "Av. Bolognesi s/n") {
                        showQuickSearchDialog = false
                        onDestinationSelect("Aeropuerto Chiclayo")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showQuickSearchDialog = false }) {
                    Text("CERRAR", color = NeonCyan)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Live Interactive Chiclayo Map Canvas
        ChiclayoMapCanvas(
            modifier = Modifier.fillMaxSize(),
            layerMode = layerMode,
            showActiveRoute = false,
            incidents = incidents
        )

        // 2. Top Header & Floating UI Elements
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // App Top Bar (Matches Page 3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Button
                IconButton(
                    onClick = onOpenDrawer,
                    modifier = Modifier.testTag("menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = TextPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // App Title
                Text(
                    text = "Smart Chiclayo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = NeonCyan
                )

                // Actions: Notification & Profile
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenNotifications,
                        modifier = Modifier.testTag("notification_button")
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = TrafficRed,
                                    modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = TextPrimary
                            )
                        }
                    }

                    IconButton(
                        onClick = onOpenProfile,
                        modifier = Modifier.testTag("profile_top_button")
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = CyberSurfaceElevated,
                            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = NeonCyan,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar ("¿A dónde vas?")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showQuickSearchDialog = true }
                    .testTag("search_bar"),
                color = CyberSurfaceCard.copy(alpha = 0.92f),
                border = BorderStroke(1.dp, CyberCardBorder),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "¿A dónde vas?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Traffic Status Pill (Matches Page 3)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CyberDark.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, CyberCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TrafficGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tráfico en tiempo real activo",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 3. Right-side Floating Map Controls (GPS, Layer toggle)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Recenter */ },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("recenter_button"),
                shape = RoundedCornerShape(10.dp),
                containerColor = CyberSurfaceCard.copy(alpha = 0.9f),
                contentColor = TextPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Mi Ubicación",
                    modifier = Modifier.size(20.dp)
                )
            }

            FloatingActionButton(
                onClick = onToggleLayerMode,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("layer_toggle_button"),
                shape = RoundedCornerShape(10.dp),
                containerColor = CyberSurfaceCard.copy(alpha = 0.9f),
                contentColor = if (layerMode != MapLayerMode.STANDARD) NeonCyan else TextPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Capas del mapa",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 4. Center-Right Floating "Reportar Incidencia" FAB (Matches Page 3)
        Button(
            onClick = onReportIncidentClick,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 80.dp)
                .height(46.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .testTag("report_incident_fab"),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonCyan,
                contentColor = CyberDark
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AddLocationAlt,
                    contentDescription = "Reportar",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Reportar Incidencia",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // 5. Bottom Sheet Card (Tránsito fluido + Casa / Trabajo / Centro Comercial)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("traffic_status_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard.copy(alpha = 0.96f)),
            border = BorderStroke(1.dp, CyberCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Status Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Status OK",
                        tint = TrafficGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Tránsito fluido en tu zona actual",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Shortcuts (Casa, Trabajo, Centro Comercial)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickDestinationButton(
                        title = "Casa",
                        icon = Icons.Default.Home,
                        onClick = { onDestinationSelect("Casa") }
                    )

                    QuickDestinationButton(
                        title = "Trabajo",
                        icon = Icons.Default.Work,
                        onClick = { onDestinationSelect("Trabajo") }
                    )

                    QuickDestinationButton(
                        title = "Centro\nComercial",
                        icon = Icons.Default.ShoppingBag,
                        onClick = { onDestinationSelect("Centro Comercial Real Plaza") }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickDestinationButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(92.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = CyberSurface,
        border = BorderStroke(1.dp, CyberCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NeonCyan,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = TextSecondary,
                lineHeight = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun QuickSearchItem(title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(text = subtitle, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
