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
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.IncidentType
import com.example.data.PriorityLevel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIncidentScreen(
    currentLocationName: String = "Av. José Balta 123",
    onSubmitReport: (IncidentType, PriorityLevel, String, String) -> Unit,
    onBack: () -> Unit
) {
    var selectedType by remember { mutableStateOf(IncidentType.ROAD_WORK) }
    var selectedPriority by remember { mutableStateOf(PriorityLevel.MEDIUM) }
    var descriptionText by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf(currentLocationName) }
    var photoAttached by remember { mutableStateOf(false) }
    var priorityDropdownExpanded by remember { mutableStateOf(false) }

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
            // Top Bar (Matches Page 7)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("report_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Smart Chiclayo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    color = NeonCyan
                )

                IconButton(onClick = { /* Notifications */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Header (Matches Page 7)
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Reportar\nIncidencia",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 32.sp
                    ),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Form Container Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
                border = BorderStroke(1.dp, CyberCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // UBICACIÓN ACTUAL Card (Matches Page 7)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CyberSurface,
                        border = BorderStroke(1.dp, CyberCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Ubicación",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "UBICACIÓN ACTUAL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = locationText,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary,
                                        maxLines = 1
                                    )
                                }
                            }

                            TextButton(
                                onClick = { locationText = "Av. Bolognesi / Balta" },
                                modifier = Modifier.testTag("adjust_location_button")
                            ) {
                                Text(
                                    text = "Ajustar",
                                    color = NeonCyan,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // TIPO DE INCIDENCIA (Matches Page 7 2-column Grid)
                    Text(
                        text = "TIPO DE INCIDENCIA",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IncidentTypeButton(
                            title = "Accident",
                            icon = Icons.Default.CarCrash,
                            isSelected = selectedType == IncidentType.ACCIDENT,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.ACCIDENT }
                        )

                        IncidentTypeButton(
                            title = "Road Work",
                            icon = Icons.Default.Handyman,
                            isSelected = selectedType == IncidentType.ROAD_WORK,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.ROAD_WORK }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IncidentTypeButton(
                            title = "Pothole",
                            icon = Icons.Default.WarningAmber,
                            isSelected = selectedType == IncidentType.POTHOLE,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.POTHOLE }
                        )

                        IncidentTypeButton(
                            title = "Congestion",
                            icon = Icons.Default.Traffic,
                            isSelected = selectedType == IncidentType.CONGESTION,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.CONGESTION }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    IncidentTypeButton(
                        title = "Emergency",
                        icon = Icons.Default.Emergency,
                        isSelected = selectedType == IncidentType.EMERGENCY,
                        modifier = Modifier.fillMaxWidth(0.485f),
                        onClick = { selectedType = IncidentType.EMERGENCY }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // NIVEL DE PRIORIDAD (Dropdown selector)
                    Text(
                        text = "NIVEL DE PRIORIDAD",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = priorityDropdownExpanded,
                        onExpandedChange = { priorityDropdownExpanded = !priorityDropdownExpanded }
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .clickable { priorityDropdownExpanded = true },
                            shape = RoundedCornerShape(10.dp),
                            color = CyberSurface,
                            border = BorderStroke(1.dp, CyberCardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedPriority.displayName,
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expandir",
                                    tint = TextSecondary
                                )
                            }
                        }

                        ExposedDropdownMenu(
                            expanded = priorityDropdownExpanded,
                            onDismissRequest = { priorityDropdownExpanded = false },
                            modifier = Modifier.background(CyberSurfaceElevated)
                        ) {
                            PriorityLevel.entries.forEach { priority ->
                                DropdownMenuItem(
                                    text = { Text(priority.displayName, color = TextPrimary) },
                                    onClick = {
                                        selectedPriority = priority
                                        priorityDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // DESCRIPCIÓN
                    Text(
                        text = "DESCRIPCIÓN",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = descriptionText,
                        onValueChange = { descriptionText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("incident_description_field"),
                        placeholder = {
                            Text("Detalles adicionales sobre la incidencia...", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // EVIDENCIA FOTOGRÁFICA (Camera upload box)
                    Text(
                        text = "EVIDENCIA FOTOGRÁFICA",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { photoAttached = !photoAttached }
                            .testTag("attach_photo_button"),
                        color = if (photoAttached) NeonCyan.copy(alpha = 0.1f) else CyberSurface,
                        border = BorderStroke(1.dp, if (photoAttached) NeonCyan else CyberCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (photoAttached) Icons.Default.CheckCircle else Icons.Default.PhotoCamera,
                                contentDescription = "Foto",
                                tint = if (photoAttached) NeonCyan else TextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (photoAttached) "Foto adjuntada (1 imagen)" else "Adjuntar foto (Opcional)",
                                color = if (photoAttached) NeonCyan else TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button: Enviar Reporte (Matches Page 7)
            Button(
                onClick = {
                    onSubmitReport(selectedType, selectedPriority, locationText, descriptionText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 20.dp)
                    .testTag("submit_report_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MinimalPrimary,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "▷ Enviar Reporte",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun IncidentTypeButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isSelected) CyberSurfaceElevated else CyberSurface,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) NeonCyan else CyberCardBorder
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) NeonCyan else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                color = if (isSelected) NeonCyanLight else TextSecondary
            )
        }
    }
}
