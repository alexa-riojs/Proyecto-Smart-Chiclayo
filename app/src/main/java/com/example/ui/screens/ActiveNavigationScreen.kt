package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NavigationState
import com.example.ui.components.ChiclayoMapCanvas
import com.example.ui.theme.*

@Composable
fun ActiveNavigationScreen(
    navigationState: NavigationState,
    onStopNavigation: () -> Unit,
    onReportIncident: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MinimalBackground)
    ) {
        // 1. Navigation Map with tracking
        ChiclayoMapCanvas(
            modifier = Modifier.fillMaxSize(),
            showActiveRoute = true,
            routeProgress = navigationState.progressPercent,
            isNavigationMode = true
        )

        // 2. Responsive Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
        ) {
            // Top Navigation Turn HUD Card
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("nav_turn_hud"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MinimalSurface.copy(alpha = 0.98f)),
                border = BorderStroke(1.dp, MinimalBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    LinearProgressIndicator(
                        progress = { navigationState.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MinimalPrimary,
                        trackColor = MinimalBorderSubtle
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = navigationState.distanceToTurn.substringBefore(" "),
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-0.5).sp
                                    ),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "m",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = navigationState.currentStreet,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )

                            Text(
                                text = navigationState.directionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        // Turn direction symbol
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MinimalPrimaryContainer,
                            border = BorderStroke(1.dp, MinimalPrimary)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.TurnRight,
                                    contentDescription = "Giro a la derecha",
                                    tint = MinimalPrimary,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Right Floating Map Controls
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FloatingActionButton(
                    onClick = { /* Zoom in */ },
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MinimalSurface,
                    contentColor = TextPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom", modifier = Modifier.size(20.dp))
                }

                FloatingActionButton(
                    onClick = { /* Layers */ },
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MinimalSurface,
                    contentColor = TextPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                ) {
                    Icon(Icons.Default.Layers, contentDescription = "Capas", modifier = Modifier.size(20.dp))
                }

                FloatingActionButton(
                    onClick = { /* Recenter */ },
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MinimalSurface,
                    contentColor = MinimalPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Re-centrar", modifier = Modifier.size(20.dp), tint = MinimalPrimary)
                }
            }

            // Bottom Navigation HUD Panel
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .testTag("nav_bottom_hud"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MinimalSurface.copy(alpha = 0.98f)),
                border = BorderStroke(1.dp, MinimalBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Trip metrics row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(TrafficGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = navigationState.remainingMinutes,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "14:45",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ETA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "•  ${navigationState.remainingDistance}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Traffic fluid badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MinimalSurfaceVariant,
                            border = BorderStroke(1.dp, MinimalBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = MinimalPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tráfico Fluido",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottom Action Buttons: REPORTAR and DETENER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onReportIncident,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("nav_report_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MinimalSurfaceVariant,
                                contentColor = TextPrimary
                            ),
                            border = BorderStroke(1.dp, MinimalBorder)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Reportar",
                                    tint = TrafficYellow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "REPORTAR",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Button(
                            onClick = onStopNavigation,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("nav_stop_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TrafficRed,
                                contentColor = Color.White
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Detener",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DETENER",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
