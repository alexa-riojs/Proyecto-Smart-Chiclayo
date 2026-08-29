package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TaxiOption
import com.example.data.TaxiRide
import com.example.data.TaxiRideStatus
import com.example.ui.components.ChiclayoMapCanvas
import com.example.ui.theme.*

@Composable
fun TaxiScreen(
    taxiOptions: List<TaxiOption>,
    selectedOption: TaxiOption?,
    taxiRide: TaxiRide,
    onSelectOption: (TaxiOption) -> Unit,
    onRequestTaxi: () -> Unit,
    onCancelTaxi: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    var activeOption by remember { mutableStateOf(selectedOption ?: taxiOptions.firstOrNull()) }
    var pickupAddress by remember { mutableStateOf("Av. Balta 123, Chiclayo") }
    var destinationAddress by remember { mutableStateOf("Real Plaza Chiclayo") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MinimalBackground)
    ) {
        // Map Canvas in background
        ChiclayoMapCanvas(
            modifier = Modifier.fillMaxSize(),
            showActiveRoute = true
        )

        // Main responsive container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
        ) {
            // Top App Bar
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
                        IconButton(onClick = { /* Search */ }) {
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
                            modifier = Modifier.testTag("taxi_notifications_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = TextPrimary
                            )
                        }
                    }
                }
            }

            // Bottom Taxi Booking Card
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 86.dp)
                    .testTag("taxi_bottom_panel"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MinimalSurface.copy(alpha = 0.98f)),
                border = BorderStroke(1.dp, MinimalBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header: "Solicitar Taxi" + "● DISPONIBLE"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Solicitar Taxi",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MinimalPrimaryContainer,
                            border = BorderStroke(1.dp, MinimalPrimary.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
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
                                    text = "EN VIVO",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MinimalPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Active Ride States (Searching / Assigned / Arriving)
                    if (taxiRide.status != TaxiRideStatus.IDLE) {
                        ActiveTaxiStatusBanner(
                            ride = taxiRide,
                            onCancel = onCancelTaxi
                        )
                    } else {
                        // Pickup & Destination Fields
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MinimalSurfaceVariant,
                            border = BorderStroke(1.dp, MinimalBorderSubtle)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // PUNTO DE RECOGIDA
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(TextMuted)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "PUNTO DE RECOGIDA",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TextMuted
                                        )
                                        Text(
                                            text = pickupAddress,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                            color = TextPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = MinimalBorderSubtle)
                                Spacer(modifier = Modifier.height(8.dp))

                                // DESTINO
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
                                            text = "DESTINO",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TextMuted
                                        )
                                        Text(
                                            text = destinationAddress,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // OPCIONES DISPONIBLES
                        Text(
                            text = "OPCIONES DISPONIBLES",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        taxiOptions.forEach { option ->
                            val isSelected = activeOption?.id == option.id
                            TaxiOptionItem(
                                option = option,
                                isSelected = isSelected,
                                onSelect = {
                                    activeOption = option
                                    onSelectOption(option)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Solicitar Taxi Button
                        Button(
                            onClick = onRequestTaxi,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("request_taxi_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MinimalPrimary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Solicitar Taxi",
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
private fun TaxiOptionItem(
    option: TaxiOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("taxi_option_${option.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MinimalPrimaryContainer else MinimalSurfaceVariant
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) MinimalPrimary else MinimalBorderSubtle
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MinimalPrimary else MinimalSurface,
                    border = BorderStroke(1.dp, if (isSelected) MinimalPrimary else MinimalBorderSubtle)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (option.iconType == "mototaxi") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                            contentDescription = option.name,
                            tint = if (isSelected) Color.White else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = option.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isSelected) MinimalOnPrimaryContainer else TextPrimary
                    )
                    Text(
                        text = "⏱ ${option.etaMinutes} min  •  ${option.subtitle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Text(
                text = "S/ ${"%.2f".format(option.priceSoles)}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) MinimalPrimary else TextPrimary
                )
            )
        }
    }
}

@Composable
private fun ActiveTaxiStatusBanner(
    ride: TaxiRide,
    onCancel: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MinimalSurfaceVariant,
        border = BorderStroke(1.dp, MinimalPrimary.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when (ride.status) {
                TaxiRideStatus.SEARCHING -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MinimalPrimary,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Buscando conductor cercano...",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "Conectando con red Smart Chiclayo",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
                TaxiRideStatus.DRIVER_ASSIGNED, TaxiRideStatus.ARRIVING -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(42.dp),
                                shape = CircleShape,
                                color = MinimalPrimaryContainer,
                                border = BorderStroke(1.dp, MinimalPrimary)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = MinimalPrimary)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = ride.driverName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${ride.carModel} • ${ride.carPlate}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MinimalSurface,
                            border = BorderStroke(1.dp, MinimalBorderSubtle)
                        ) {
                            Text(
                                text = "★ ${ride.driverRating}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFD97706),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MinimalBorderSubtle)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (ride.status == TaxiRideStatus.ARRIVING) "Conductor llegando en 1 min" else "Conductor en camino (${ride.etaMinutes} min)",
                            color = MinimalPrimary,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "S/ ${"%.2f".format(ride.priceSoles)}",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                        )
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TrafficRed.copy(alpha = 0.15f),
                    contentColor = TrafficRed
                ),
                border = BorderStroke(1.dp, TrafficRed.copy(alpha = 0.4f))
            ) {
                Text("Cancelar Solicitud", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}
