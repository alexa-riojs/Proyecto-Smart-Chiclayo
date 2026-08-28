package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.UserProfile
import com.example.data.UserRole
import com.example.ui.theme.*

enum class AuthMode {
    LOGIN,
    REGISTER
}

@Composable
fun AuthScreen(
    isLoading: Boolean,
    isError: Boolean,
    errorMessage: String,
    successMessage: String?,
    registeredUsers: List<UserProfile>,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String, UserRole, String, String) -> Unit,
    onClearError: () -> Unit
) {
    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }

    // Login Fields
    var loginIdentifier by remember { mutableStateOf("operador.chiclayo@smartcity.pe") }
    var loginPasscode by remember { mutableStateOf("admin123") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    // Register Fields
    var regFullName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regOperatorId by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regRole by remember { mutableStateOf(UserRole.CIUDADANO) }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }
    var regConfirmPasswordVisible by remember { mutableStateOf(false) }

    var showRecoverDialog by remember { mutableStateOf(false) }

    if (showRecoverDialog) {
        AlertDialog(
            onDismissRequest = { showRecoverDialog = false },
            containerColor = MinimalSurface,
            title = {
                Text(
                    text = "Recuperar Contraseña",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Para recuperar tu acceso en Smart Chiclayo, ingresa tu correo registrado. Te enviaremos instrucciones de restablecimiento de credenciales.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { showRecoverDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MinimalPrimary, contentColor = Color.White)
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MinimalBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Brand Emblem
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MinimalPrimaryContainer)
                    .border(1.5.dp, MinimalPrimary.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBus,
                    contentDescription = "Smart Chiclayo Logo",
                    tint = MinimalPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            Text(
                text = "Smart Chiclayo",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Sistema Integrado de Movilidad Urbana",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Mode Selector (Iniciar Sesión / Crear Cuenta)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = MinimalSurfaceVariant,
                border = BorderStroke(1.dp, MinimalBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Login Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (authMode == AuthMode.LOGIN) MinimalPrimary else Color.Transparent
                            )
                            .clickable {
                                authMode = AuthMode.LOGIN
                                onClearError()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (authMode == AuthMode.LOGIN) FontWeight.Bold else FontWeight.Medium,
                                color = if (authMode == AuthMode.LOGIN) Color.White else TextSecondary
                            )
                        )
                    }

                    // Register Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (authMode == AuthMode.REGISTER) MinimalPrimary else Color.Transparent
                            )
                            .clickable {
                                authMode = AuthMode.REGISTER
                                onClearError()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Crear Cuenta",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (authMode == AuthMode.REGISTER) FontWeight.Bold else FontWeight.Medium,
                                color = if (authMode == AuthMode.REGISTER) Color.White else TextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Error / Success Feedback Banner
            if (isError && errorMessage.isNotBlank()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = TrafficRed.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, TrafficRed.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Error",
                            tint = TrafficRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage,
                            color = TrafficRed,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            if (successMessage != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = TrafficGreen.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, TrafficGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Éxito",
                            tint = TrafficGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = successMessage,
                            color = TrafficGreen,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            // FORM CONTENT
            AnimatedContent(
                targetState = authMode,
                label = "auth_form_transition"
            ) { mode ->
                if (mode == AuthMode.LOGIN) {
                    // LOGIN FORM
                    Column {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                            border = BorderStroke(1.dp, MinimalBorder)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Credenciales de Acceso",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Ingresa con tu correo o ID de operador registrado",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Identifier Field
                                OutlinedTextField(
                                    value = loginIdentifier,
                                    onValueChange = {
                                        loginIdentifier = it
                                        onClearError()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("operator_id_input"),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Usuario",
                                            tint = MinimalPrimary
                                        )
                                    },
                                    label = { Text("Correo o ID de Operador") },
                                    placeholder = { Text("ej. vilcabanaale@gmail.com") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MinimalPrimary,
                                        unfocusedBorderColor = MinimalBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Password Field
                                OutlinedTextField(
                                    value = loginPasscode,
                                    onValueChange = {
                                        loginPasscode = it
                                        onClearError()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("passcode_input"),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Contraseña",
                                            tint = MinimalPrimary
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                                            Icon(
                                                imageVector = if (loginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Alternar contraseña",
                                                tint = TextSecondary
                                            )
                                        }
                                    },
                                    label = { Text("Contraseña") },
                                    visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MinimalPrimary,
                                        unfocusedBorderColor = MinimalBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Forgot password link
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Text(
                                        text = "¿Olvidaste tu contraseña?",
                                        color = MinimalPrimary,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        modifier = Modifier
                                            .clickable { showRecoverDialog = true }
                                            .padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Quick Test Accounts Pill Selector
                        Text(
                            text = "Cuentas rápidas de prueba (Base de Datos):",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                DemoAccountChip("👤 Ciudadano", "vilcabanaale@gmail.com", "chiclayo2026") { id, pass ->
                                    loginIdentifier = id
                                    loginPasscode = pass
                                }
                            }
                            item {
                                DemoAccountChip("🛡️ Administrador", "operador.chiclayo@smartcity.pe", "admin123") { id, pass ->
                                    loginIdentifier = id
                                    loginPasscode = pass
                                }
                            }
                            item {
                                DemoAccountChip("🚌 Conductor Bus", "chofer.linea12@chiclayo.pe", "bus123") { id, pass ->
                                    loginIdentifier = id
                                    loginPasscode = pass
                                }
                            }
                            item {
                                DemoAccountChip("🚖 Chofer Taxi", "carlos.taxi@chiclayo.pe", "taxi123") { id, pass ->
                                    loginIdentifier = id
                                    loginPasscode = pass
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Submit Login Button
                        Button(
                            onClick = { onLogin(loginIdentifier, loginPasscode) },
                            enabled = !isLoading,
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
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "INICIAR SESIÓN",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.2.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Entrar",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // REGISTER FORM
                    Column {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                            border = BorderStroke(1.dp, MinimalBorder)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Registro de Nueva Cuenta",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tus datos se guardarán de forma real en la base de datos",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Full Name
                                OutlinedTextField(
                                    value = regFullName,
                                    onValueChange = {
                                        regFullName = it
                                        onClearError()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("reg_fullname_input"),
                                    leadingIcon = {
                                        Icon(Icons.Default.Badge, contentDescription = null, tint = MinimalPrimary)
                                    },
                                    label = { Text("Nombre Completo") },
                                    placeholder = { Text("ej. María Elena Torres") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MinimalPrimary,
                                        unfocusedBorderColor = MinimalBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Email
                                OutlinedTextField(
                                    value = regEmail,
                                    onValueChange = {
                                        regEmail = it
                                        if (regOperatorId.isBlank() && it.contains("@")) {
                                            regOperatorId = "OP-" + it.substringBefore("@").take(6).uppercase()
                                        }
                                        onClearError()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("reg_email_input"),
                                    leadingIcon = {
                                        Icon(Icons.Default.Email, contentDescription = null, tint = MinimalPrimary)
                                    },
                                    label = { Text("Correo Electrónico") },
                                    placeholder = { Text("ej. maria.torres@gmail.com") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MinimalPrimary,
                                        unfocusedBorderColor = MinimalBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    // Operator / User ID
                                    OutlinedTextField(
                                        value = regOperatorId,
                                        onValueChange = {
                                            regOperatorId = it
                                            onClearError()
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("reg_operator_id_input"),
                                        leadingIcon = {
                                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MinimalPrimary)
                                        },
                                        label = { Text("ID Usuario / Operador") },
                                        placeholder = { Text("OP-MARIA") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MinimalPrimary,
                                            unfocusedBorderColor = MinimalBorder
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    // Phone
                                    OutlinedTextField(
                                        value = regPhone,
                                        onValueChange = {
                                            regPhone = it
                                            onClearError()
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("reg_phone_input"),
                                        leadingIcon = {
                                            Icon(Icons.Default.Phone, contentDescription = null, tint = MinimalPrimary)
                                        },
                                        label = { Text("Teléfono") },
                                        placeholder = { Text("+51 987...") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MinimalPrimary,
                                            unfocusedBorderColor = MinimalBorder
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Role Picker
                                Text(
                                    text = "Tipo de Cuenta / Rol:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    UserRole.entries.forEach { role ->
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .clickable { regRole = role },
                                            color = if (regRole == role) MinimalPrimaryContainer else MinimalSurfaceVariant,
                                            border = BorderStroke(
                                                1.dp,
                                                if (regRole == role) MinimalPrimary else MinimalBorderSubtle
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = regRole == role,
                                                    onClick = { regRole = role },
                                                    colors = RadioButtonDefaults.colors(selectedColor = MinimalPrimary)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Column {
                                                    Text(
                                                        text = role.title,
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = if (regRole == role) FontWeight.Bold else FontWeight.Medium,
                                                            color = if (regRole == role) MinimalOnPrimaryContainer else TextPrimary
                                                        )
                                                    )
                                                    Text(
                                                        text = role.description,
                                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                        color = TextSecondary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Password
                                OutlinedTextField(
                                    value = regPassword,
                                    onValueChange = {
                                        regPassword = it
                                        onClearError()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("reg_password_input"),
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = MinimalPrimary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { regPasswordVisible = !regPasswordVisible }) {
                                            Icon(
                                                imageVector = if (regPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = null,
                                                tint = TextSecondary
                                            )
                                        }
                                    },
                                    label = { Text("Contraseña (mínimo 4 caracteres)") },
                                    visualTransformation = if (regPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MinimalPrimary,
                                        unfocusedBorderColor = MinimalBorder
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Confirm Password
                                OutlinedTextField(
                                    value = regConfirmPassword,
                                    onValueChange = {
                                        regConfirmPassword = it
                                        onClearError()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("reg_confirm_password_input"),
                                    leadingIcon = {
                                        Icon(Icons.Default.LockClock, contentDescription = null, tint = MinimalPrimary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { regConfirmPasswordVisible = !regConfirmPasswordVisible }) {
                                            Icon(
                                                imageVector = if (regConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = null,
                                                tint = TextSecondary
                                            )
                                        }
                                    },
                                    label = { Text("Confirmar Contraseña") },
                                    visualTransformation = if (regConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MinimalPrimary,
                                        unfocusedBorderColor = MinimalBorder
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Submit Register Button
                        Button(
                            onClick = {
                                onRegister(
                                    regFullName,
                                    regEmail,
                                    regOperatorId,
                                    regPhone,
                                    regRole,
                                    regPassword,
                                    regConfirmPassword
                                )
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("register_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MinimalPrimary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "REGISTRAR CUENTA & ACCEDER",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Guardar",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Information
            Text(
                text = "Base de Datos Local SQLite Room integrada // Listo para sincronización Cloud",
                color = TextMuted,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DemoAccountChip(
    label: String,
    identifier: String,
    pass: String,
    onSelect: (String, String) -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect(identifier, pass) },
        color = MinimalSurfaceVariant,
        border = BorderStroke(1.dp, MinimalBorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MinimalPrimary
                )
            )
        }
    }
}
