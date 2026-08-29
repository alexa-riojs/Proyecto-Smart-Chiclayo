package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserRole
import com.example.ui.theme.*

@Composable
fun RoleSelectionScreen(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onCancel: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(currentRole) }

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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "Selección de Perfil",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Heading
            Text(
                text = "¿Cómo usarás\nla plataforma?",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 34.sp
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona el perfil que mejor describa tu rol en la ciudad para personalizar tu experiencia y herramientas.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4 Role Cards
            RoleCard(
                role = UserRole.CIUDADANO,
                icon = Icons.Default.Person,
                isSelected = selectedRole == UserRole.CIUDADANO,
                onSelect = { selectedRole = UserRole.CIUDADANO }
            )

            Spacer(modifier = Modifier.height(12.dp))

            RoleCard(
                role = UserRole.CONDUCTOR_TRANSPORTE,
                icon = Icons.Default.DirectionsBus,
                isSelected = selectedRole == UserRole.CONDUCTOR_TRANSPORTE,
                onSelect = { selectedRole = UserRole.CONDUCTOR_TRANSPORTE }
            )

            Spacer(modifier = Modifier.height(12.dp))

            RoleCard(
                role = UserRole.CONDUCTOR_TAXI,
                icon = Icons.Default.LocalTaxi,
                isSelected = selectedRole == UserRole.CONDUCTOR_TAXI,
                onSelect = { selectedRole = UserRole.CONDUCTOR_TAXI }
            )

            Spacer(modifier = Modifier.height(12.dp))

            RoleCard(
                role = UserRole.ADMINISTRADOR,
                icon = Icons.Default.AdminPanelSettings,
                isSelected = selectedRole == UserRole.ADMINISTRADOR,
                onSelect = { selectedRole = UserRole.ADMINISTRADOR }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Bottom Actions: CANCELAR and CONTINUAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.testTag("cancel_role_button")
                ) {
                    Text(
                        text = "CANCELAR",
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = { onRoleSelected(selectedRole) },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("continue_role_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MinimalPrimary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CONTINUAR",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Continuar",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleCard(
    role: UserRole,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("role_card_${role.name.lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MinimalPrimaryContainer else MinimalSurface
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) MinimalPrimary else MinimalBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Icon container
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) MinimalPrimary.copy(alpha = 0.18f) else MinimalSurfaceVariant,
                border = BorderStroke(1.dp, if (isSelected) MinimalPrimary else MinimalBorderSubtle)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = role.title,
                        tint = if (isSelected) MinimalPrimary else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = role.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) MinimalOnPrimaryContainer else TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = role.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}
