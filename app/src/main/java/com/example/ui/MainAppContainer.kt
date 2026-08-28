package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.IncidentType
import com.example.data.PriorityLevel
import com.example.data.UserRole
import com.example.ui.screens.*
import com.example.ui.theme.*

@Composable
fun MainAppContainer() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val database = remember { com.example.data.local.AppDatabase.getDatabase(context) }
    val repository = remember { com.example.data.SmartChiclayoRepository(database.userDao()) }
    val viewModel: SmartChiclayoViewModel = viewModel(
        factory = SmartChiclayoViewModel.provideFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDrawer by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    // Notifications Dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            containerColor = CyberSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Alertas de Tráfico Chiclayo", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = CyberSurface,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, CyberCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("● Tránsito fluido en Vía Evitamiento", color = TrafficGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text("Velocidad promedio: 55 km/h hacia Real Plaza.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Surface(
                        color = CyberSurface,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, CyberCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("⚠ Obras en José Leonardo Ortiz", color = TrafficYellow, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text("Desvío por Av. Balta Norte disponible.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("CERRAR", color = NeonCyan)
                }
            }
        )
    }

    // Role Switcher Dialog
    if (uiState.showRoleSwitcherDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleRoleSwitcher(false) },
            containerColor = CyberSurfaceElevated,
            title = {
                Text("Seleccionar Rol de Acceso", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    UserRole.entries.forEach { role ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.toggleRoleSwitcher(false)
                                    viewModel.selectRoleAndContinue(role)
                                },
                            color = if (uiState.userProfile.role == role) NeonCyan.copy(alpha = 0.15f) else CyberSurface,
                            border = BorderStroke(1.dp, if (uiState.userProfile.role == role) NeonCyan else CyberCardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = role.title,
                                    color = if (uiState.userProfile.role == role) NeonCyanLight else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.toggleRoleSwitcher(false) }) {
                    Text("CANCELAR", color = TextSecondary)
                }
            }
        )
    }

    // Modal Drawer Sheet
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CyberSurfaceElevated,
                drawerContentColor = TextPrimary,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Smart Chiclayo",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        color = NeonCyan
                    )
                    Text(
                        text = "Centro de Movilidad Urbana",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = CyberCardBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Map, contentDescription = null, tint = NeonCyan) },
                        label = { Text("Mapa en Vivo") },
                        selected = uiState.currentScreen == AppScreen.MAIN_APP && uiState.currentTab == MainTab.MAP,
                        onClick = {
                            viewModel.setTab(MainTab.MAP)
                            drawerState.apply { viewModel.navigateTo(AppScreen.MAIN_APP) }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedTextColor = NeonCyanLight,
                            unselectedTextColor = TextPrimary
                        )
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = RoleTransit) },
                        label = { Text("Transporte Público") },
                        selected = uiState.currentScreen == AppScreen.MAIN_APP && uiState.currentTab == MainTab.TRANSIT,
                        onClick = {
                            viewModel.setTab(MainTab.TRANSIT)
                            drawerState.apply { viewModel.navigateTo(AppScreen.MAIN_APP) }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedTextColor = NeonCyanLight,
                            unselectedTextColor = TextPrimary
                        )
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.LocalTaxi, contentDescription = null, tint = RoleTaxi) },
                        label = { Text("Solicitar Taxi") },
                        selected = uiState.currentScreen == AppScreen.MAIN_APP && uiState.currentTab == MainTab.TAXI,
                        onClick = {
                            viewModel.setTab(MainTab.TAXI)
                            drawerState.apply { viewModel.navigateTo(AppScreen.MAIN_APP) }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedTextColor = NeonCyanLight,
                            unselectedTextColor = TextPrimary
                        )
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = TrafficYellow) },
                        label = { Text("Reportar Incidencia") },
                        selected = uiState.currentScreen == AppScreen.REPORT_INCIDENT,
                        onClick = {
                            viewModel.navigateTo(AppScreen.REPORT_INCIDENT)
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedTextColor = NeonCyanLight,
                            unselectedTextColor = TextPrimary
                        )
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = AdminPurple) },
                        label = { Text("Panel Administrativo") },
                        selected = uiState.currentScreen == AppScreen.ADMIN_DASHBOARD,
                        onClick = {
                            viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedTextColor = NeonCyanLight,
                            unselectedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "SYS.AUTH.v4.41 // Chiclayo - Lambayeque",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = TextMuted
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = CyberDark,
            bottomBar = {
                if (uiState.currentScreen == AppScreen.MAIN_APP) {
                    SmartChiclayoBottomNav(
                        currentTab = uiState.currentTab,
                        onTabSelected = { tab ->
                            if (tab == MainTab.HOME) viewModel.setTab(MainTab.MAP)
                            else viewModel.setTab(tab)
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (uiState.currentScreen) {
                    AppScreen.AUTH -> {
                        AuthScreen(
                            isLoading = uiState.isAuthLoading,
                            isError = uiState.isAuthError,
                            errorMessage = uiState.authErrorMessage,
                            successMessage = uiState.authSuccessMessage,
                            registeredUsers = uiState.registeredUsersList,
                            onLogin = { id, pass -> viewModel.login(id, pass) },
                            onRegister = { name, email, opId, phone, role, pass, confirmPass ->
                                viewModel.register(name, email, opId, phone, role, pass, confirmPass)
                            },
                            onClearError = { viewModel.clearAuthMessages() }
                        )
                    }
                    AppScreen.ROLE_SELECTION -> {
                        RoleSelectionScreen(
                            currentRole = uiState.userProfile.role,
                            onRoleSelected = { role -> viewModel.selectRoleAndContinue(role) },
                            onCancel = { viewModel.logout() }
                        )
                    }
                    AppScreen.MAIN_APP -> {
                        when (uiState.currentTab) {
                            MainTab.HOME, MainTab.MAP -> {
                                LiveMapScreen(
                                    userProfile = uiState.userProfile,
                                    incidents = uiState.incidents,
                                    layerMode = uiState.mapLayerMode,
                                    onOpenDrawer = {
                                        // Open role switcher or drawer
                                        viewModel.toggleRoleSwitcher(true)
                                    },
                                    onOpenNotifications = { showNotificationsDialog = true },
                                    onOpenProfile = { viewModel.setTab(MainTab.PROFILE) },
                                    onSearchClick = { viewModel.navigateTo(AppScreen.ROUTE_OPTIONS) },
                                    onDestinationSelect = { dest -> viewModel.selectDestination(dest) },
                                    onReportIncidentClick = { viewModel.navigateTo(AppScreen.REPORT_INCIDENT) },
                                    onToggleLayerMode = { viewModel.toggleMapLayerMode() }
                                )
                            }
                            MainTab.TRANSIT -> {
                                TransitScreen(
                                    transitLines = uiState.transitLines,
                                    onSelectLine = { line -> /* Track line */ },
                                    onOpenNotifications = { showNotificationsDialog = true }
                                )
                            }
                            MainTab.TAXI -> {
                                TaxiScreen(
                                    taxiOptions = uiState.taxiOptions,
                                    selectedOption = uiState.selectedTaxiOption,
                                    taxiRide = uiState.taxiRide,
                                    onSelectOption = { option -> viewModel.selectTaxiOption(option) },
                                    onRequestTaxi = { viewModel.requestTaxi() },
                                    onCancelTaxi = { viewModel.cancelTaxiRequest() },
                                    onOpenNotifications = { showNotificationsDialog = true }
                                )
                            }
                            MainTab.PROFILE -> {
                                ProfileScreen(
                                    userProfile = uiState.userProfile,
                                    onChangeRoleClick = { viewModel.navigateTo(AppScreen.ROLE_SELECTION) },
                                    onAdminDashboardClick = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) },
                                    onLogoutClick = { viewModel.logout() }
                                )
                            }
                        }
                    }
                    AppScreen.ROUTE_OPTIONS -> {
                        RouteOptionsScreen(
                            origin = uiState.originInput,
                            destination = uiState.destinationInput,
                            routes = uiState.routeOptions,
                            selectedRoute = uiState.selectedRoute,
                            onSelectRoute = { route -> viewModel.selectRouteOption(route) },
                            onStartNavigation = { viewModel.startNavigation() },
                            onBack = { viewModel.navigateTo(AppScreen.MAIN_APP) }
                        )
                    }
                    AppScreen.ACTIVE_NAVIGATION -> {
                        ActiveNavigationScreen(
                            navigationState = uiState.navigationState,
                            onStopNavigation = { viewModel.stopNavigation() },
                            onReportIncident = { viewModel.navigateTo(AppScreen.REPORT_INCIDENT) }
                        )
                    }
                    AppScreen.REPORT_INCIDENT -> {
                        ReportIncidentScreen(
                            currentLocationName = "Av. José Balta 123, Chiclayo",
                            onSubmitReport = { type, priority, loc, desc ->
                                viewModel.reportIncident(type, priority, loc, desc)
                            },
                            onBack = { viewModel.navigateTo(AppScreen.MAIN_APP) }
                        )
                    }
                    AppScreen.ADMIN_DASHBOARD -> {
                        AdminDashboardScreen(
                            userProfile = uiState.userProfile,
                            trafficLightNodes = uiState.trafficLightNodes,
                            cameraFeeds = uiState.cameraFeeds,
                            incidents = uiState.incidents,
                            activeUsersCount = uiState.activeUsersCount,
                            activeDriversCount = uiState.activeDriversCount,
                            reportedCount = uiState.reportedIncidentsCount,
                            confirmedCount = uiState.confirmedIncidentsCount,
                            resolvedCount = uiState.resolvedIncidentsCount,
                            onToggleTrafficLight = { nodeId -> viewModel.toggleTrafficLight(nodeId) },
                            onSwitchToCitizenView = { viewModel.navigateTo(AppScreen.MAIN_APP) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SmartChiclayoBottomNav(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = MinimalSurfaceVariant,
        contentColor = TextPrimary,
        tonalElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val navItems = listOf(
            Triple(MainTab.HOME, "Home", Icons.Default.Home),
            Triple(MainTab.MAP, "Map", Icons.Default.Map),
            Triple(MainTab.TRANSIT, "Transit", Icons.Default.DirectionsBus),
            Triple(MainTab.TAXI, "Taxi", Icons.Default.LocalTaxi),
            Triple(MainTab.PROFILE, "Profile", Icons.Default.Person)
        )

        navItems.forEach { (tab, label, icon) ->
            val isSelected = currentTab == tab || (tab == MainTab.MAP && currentTab == MainTab.HOME)
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MinimalOnPrimaryContainer,
                    selectedTextColor = MinimalPrimary,
                    indicatorColor = MinimalPrimaryContainer,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}
