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
import com.example.data.UserProfile
import com.example.data.UserRole
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onChangeRoleClick: () -> Unit,
    onAdminDashboardClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Perfil de Operador",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // User Identity Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
                border = BorderStroke(1.dp, CyberCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            color = NeonCyan.copy(alpha = 0.2f),
                            border = BorderStroke(1.5.dp, NeonCyan)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = userProfile.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = userProfile.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(userProfile.role.badgeColor).copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, Color(userProfile.role.badgeColor).copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = userProfile.role.title.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(userProfile.role.badgeColor),
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = CyberCardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ID Operador: ${userProfile.operatorId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = "Ubicación: Chiclayo, Lambayeque (-6.7714, -79.8409)",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Items
            ProfileOptionRow("Cambiar Rol de Usuario", "Ciudadano, Conductor o Administrador", Icons.Default.SwapHoriz, onClick = onChangeRoleClick)
            Spacer(modifier = Modifier.height(12.dp))
            ProfileOptionRow("Panel de Comando General", "Acceso a métricas de infraestructura", Icons.Default.AdminPanelSettings, onClick = onAdminDashboardClick)
            Spacer(modifier = Modifier.height(12.dp))
            ProfileOptionRow("Cerrar Sesión", "Volver a pantalla de autenticación", Icons.Default.ExitToApp, onClick = onLogoutClick, isDestructive = true)

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun ProfileOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
        border = BorderStroke(1.dp, CyberCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDestructive) TrafficRed.copy(alpha = 0.15f) else CyberSurface,
                    border = BorderStroke(1.dp, if (isDestructive) TrafficRed.copy(alpha = 0.4f) else CyberCardBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isDestructive) TrafficRed else NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDestructive) TrafficRed else TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted
            )
        }
    }
}
