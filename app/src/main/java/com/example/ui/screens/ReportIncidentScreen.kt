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
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
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
                    color = MinimalPrimary
                )

                IconButton(onClick = { /* Notifications */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Header
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Reportar\nIncidencia",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 34.sp
                    ),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Form Container Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                border = BorderStroke(1.dp, MinimalBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // UBICACIÓN ACTUAL Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MinimalSurfaceVariant,
                        border = BorderStroke(1.dp, MinimalBorderSubtle)
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
                                    tint = MinimalPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "UBICACIÓN ACTUAL",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
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
                                onClick = { locationText = "Av. Bolognesi / Balta, Chiclayo" },
                                modifier = Modifier.testTag("adjust_location_button")
                            ) {
                                Text(
                                    text = "Ajustar",
                                    color = MinimalPrimary,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // TIPO DE INCIDENCIA
                    Text(
                        text = "TIPO DE INCIDENCIA",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IncidentTypeButton(
                            title = "Accidente",
                            icon = Icons.Default.CarCrash,
                            isSelected = selectedType == IncidentType.ACCIDENT,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.ACCIDENT }
                        )

                        IncidentTypeButton(
                            title = "Obras / Vía",
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
                            title = "Bache / Hueco",
                            icon = Icons.Default.WarningAmber,
                            isSelected = selectedType == IncidentType.POTHOLE,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.POTHOLE }
                        )

                        IncidentTypeButton(
                            title = "Congestión",
                            icon = Icons.Default.Traffic,
                            isSelected = selectedType == IncidentType.CONGESTION,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = IncidentType.CONGESTION }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    IncidentTypeButton(
                        title = "Emergencia",
                        icon = Icons.Default.Emergency,
                        isSelected = selectedType == IncidentType.EMERGENCY,
                        modifier = Modifier.fillMaxWidth(0.485f),
                        onClick = { selectedType = IncidentType.EMERGENCY }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // NIVEL DE PRIORIDAD
                    Text(
                        text = "NIVEL DE PRIORIDAD",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
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
                            color = MinimalSurfaceVariant,
                            border = BorderStroke(1.dp, MinimalBorderSubtle)
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
                            modifier = Modifier.background(MinimalSurface)
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

                    Spacer(modifier = Modifier.height(18.dp))

                    // DESCRIPCIÓN
                    Text(
                        text = "DESCRIPCIÓN",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
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
                            focusedBorderColor = MinimalPrimary,
                            unfocusedBorderColor = MinimalBorder,
                            focusedContainerColor = MinimalSurfaceVariant,
                            unfocusedContainerColor = MinimalSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // EVIDENCIA FOTOGRÁFICA
                    Text(
                        text = "EVIDENCIA FOTOGRÁFICA",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { photoAttached = !photoAttached }
                            .testTag("attach_photo_button"),
                        color = if (photoAttached) MinimalPrimaryContainer else MinimalSurfaceVariant,
                        border = BorderStroke(1.dp, if (photoAttached) MinimalPrimary else MinimalBorderSubtle)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (photoAttached) Icons.Default.CheckCircle else Icons.Default.PhotoCamera,
                                contentDescription = "Foto",
                                tint = if (photoAttached) MinimalPrimary else TextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (photoAttached) "Foto adjuntada (1 imagen)" else "Adjuntar foto (Opcional)",
                                color = if (photoAttached) MinimalOnPrimaryContainer else TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    onSubmitReport(selectedType, selectedPriority, locationText, descriptionText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp)
                    .testTag("submit_report_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MinimalPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enviar Reporte",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
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
            .height(68.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isSelected) MinimalPrimaryContainer else MinimalSurfaceVariant,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) MinimalPrimary else MinimalBorderSubtle
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
                tint = if (isSelected) MinimalPrimary else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                color = if (isSelected) MinimalOnPrimaryContainer else TextSecondary
            )
        }
    }
}
