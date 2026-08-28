package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    isError: Boolean,
    errorMessage: String,
    onLogin: (String, String) -> Unit,
    onRequestClearance: () -> Unit
) {
    var operatorId by remember { mutableStateOf("OP-CHICLAYO-01") }
    var passcode by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showRecoverDialog by remember { mutableStateOf(false) }

    if (showRecoverDialog) {
        AlertDialog(
            onDismissRequest = { showRecoverDialog = false },
            containerColor = CyberSurfaceElevated,
            title = {
                Text(
                    text = "Recuperar Acceso",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Se enviará un código de verificación temporal a tu correo registrado o terminal de despacho municipal.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { showRecoverDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = CyberDark)
                ) {
                    Text("Entendido", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDark)
    ) {
        // Futuristic background lines & dark gradient
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Command Center Emblem
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CyberSurfaceElevated)
                    .border(1.5.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Command Logo",
                    tint = NeonCyan,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = "Smart Chiclayo",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Command Center Authentication",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Credentials Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Credentials",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Operator ID or Email Field
                    OutlinedTextField(
                        value = operatorId,
                        onValueChange = { operatorId = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("operator_id_input"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Operator ID",
                                tint = TextSecondary
                            )
                        },
                        placeholder = { Text("Operator ID or Email", color = TextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Passcode Field
                    OutlinedTextField(
                        value = passcode,
                        onValueChange = { passcode = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("passcode_input"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Passcode",
                                tint = TextSecondary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = { Text("Passcode", color = TextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface
                        )
                    )

                    // Error Message (Page 1)
                    if (isError) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Error",
                                tint = TrafficRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = errorMessage.ifEmpty { "Authentication failed. Passcode required." },
                                color = TrafficRed,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Recover Access
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = "Recover Access",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .clickable { showRecoverDialog = true }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // INITIALIZE LINK -> Button (Page 1 Cyan Pill)
            Button(
                onClick = { onLogin(operatorId, passcode) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("login_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MinimalPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "INITIALIZE LINK",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Continue",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // OR CONNECT VIA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = CyberCardBorder)
                Text(
                    text = "  OR CONNECT VIA  ",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = CyberCardBorder)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Alternative Connection Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onLogin("OP-GOOGLE", "123456") },
                    color = CyberSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Web Access",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onLogin("OP-PASSKEY", "123456") },
                    color = CyberSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Passkey Access",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Unregistered Operator? Request Clearance
            Row(
                modifier = Modifier.clickable { onRequestClearance() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unregistered Operator? ",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Request Clearance",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Telemetry Coordinates (Page 1)
            Text(
                text = "SYS.AUTH.v4.41 // LAT: -6.7714 LON: -79.8409",
                color = TextMuted.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
