package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    AUTH,
    ROLE_SELECTION,
    MAIN_APP,
    ROUTE_OPTIONS,
    ACTIVE_NAVIGATION,
    REPORT_INCIDENT,
    ADMIN_DASHBOARD
}

enum class MainTab {
    HOME,
    MAP,
    TRANSIT,
    TAXI,
    PROFILE
}

data class UiState(
    val currentScreen: AppScreen = AppScreen.AUTH,
    val currentTab: MainTab = MainTab.MAP,
    val userProfile: UserProfile = UserProfile(),
    val isAuthLoading: Boolean = false,
    val isAuthError: Boolean = false,
    val authErrorMessage: String = "",
    val authSuccessMessage: String? = null,
    val registeredUsersList: List<UserProfile> = emptyList(),
    val transitLines: List<TransitLine> = emptyList(),
    val routeOptions: List<RouteOption> = emptyList(),
    val selectedRoute: RouteOption? = null,
    val navigationState: NavigationState = NavigationState(),
    val taxiOptions: List<TaxiOption> = emptyList(),
    val selectedTaxiOption: TaxiOption? = null,
    val taxiRide: TaxiRide = TaxiRide(),
    val incidents: List<IncidentReport> = emptyList(),
    val trafficLightNodes: List<TrafficLightNode> = emptyList(),
    val cameraFeeds: List<CameraFeed> = emptyList(),
    val selectedCamera: CameraFeed? = null,
    val searchQuery: String = "",
    val destinationInput: String = "Centro Comercial Real Plaza",
    val originInput: String = "Tu ubicación (Av. Balta 123)",
    val mapLayerMode: MapLayerMode = MapLayerMode.STANDARD,
    val showRoleSwitcherDialog: Boolean = false,
    val showNotificationBadge: Boolean = true,
    val notificationMessage: String? = "Bienvenido al Sistema de Movilidad Smart Chiclayo",
    val systemTimeGmt: String = "11:14:50 GMT-5",
    val activeUsersCount: String = "12,450",
    val activeDriversCount: String = "842",
    val reportedIncidentsCount: Int = 45,
    val confirmedIncidentsCount: Int = 12,
    val resolvedIncidentsCount: Int = 128
)

class SmartChiclayoViewModel(
    private val repository: SmartChiclayoRepository = SmartChiclayoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var navigationJob: Job? = null
    private var taxiSimulationJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                _uiState.update { it.copy(userProfile = profile) }
            }
        }
        viewModelScope.launch {
            repository.registeredUsers?.collect { users ->
                _uiState.update { it.copy(registeredUsersList = users) }
            }
        }
        viewModelScope.launch {
            repository.transitLines.collect { lines ->
                _uiState.update { it.copy(transitLines = lines) }
            }
        }
        viewModelScope.launch {
            repository.routeOptions.collect { routes ->
                _uiState.update {
                    it.copy(
                        routeOptions = routes,
                        selectedRoute = routes.firstOrNull()
                    )
                }
            }
        }
        viewModelScope.launch {
            repository.taxiOptions.collect { taxis ->
                _uiState.update {
                    it.copy(
                        taxiOptions = taxis,
                        selectedTaxiOption = taxis.firstOrNull()
                    )
                }
            }
        }
        viewModelScope.launch {
            repository.incidents.collect { inc ->
                _uiState.update { it.copy(incidents = inc) }
            }
        }
        viewModelScope.launch {
            repository.trafficLightNodes.collect { nodes ->
                _uiState.update { it.copy(trafficLightNodes = nodes) }
            }
        }
        viewModelScope.launch {
            repository.cameraFeeds.collect { cameras ->
                _uiState.update {
                    it.copy(
                        cameraFeeds = cameras,
                        selectedCamera = cameras.firstOrNull()
                    )
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setTab(tab: MainTab) {
        _uiState.update { it.copy(currentTab = tab, currentScreen = AppScreen.MAIN_APP) }
    }

    fun login(identifier: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, isAuthError = false, authErrorMessage = "") }
            
            // Brief simulation delay for responsive feel
            delay(350)
            
            val result = repository.authenticateUser(identifier, pass)
            result.onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        isAuthError = false,
                        authErrorMessage = "",
                        authSuccessMessage = "¡Bienvenido, ${profile.name}!",
                        notificationMessage = "Sesión iniciada como ${profile.role.title}",
                        currentScreen = if (profile.role == UserRole.ADMINISTRADOR) AppScreen.ADMIN_DASHBOARD else AppScreen.MAIN_APP,
                        currentTab = MainTab.MAP
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        isAuthError = true,
                        authErrorMessage = error.message ?: "Error de autenticación. Verifica tus credenciales."
                    )
                }
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        operatorId: String,
        phone: String,
        role: UserRole,
        password: String,
        confirmPassword: String
    ) {
        if (password != confirmPassword) {
            _uiState.update {
                it.copy(
                    isAuthError = true,
                    authErrorMessage = "Las contraseñas no coinciden. Por favor verifícalas."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, isAuthError = false, authErrorMessage = "") }
            delay(400)

            val result = repository.registerUser(
                fullName = fullName,
                email = email,
                operatorId = operatorId,
                phone = phone,
                role = role,
                password = password
            )

            result.onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        isAuthError = false,
                        authErrorMessage = "",
                        authSuccessMessage = "¡Cuenta creada exitosamente para ${profile.name}!",
                        notificationMessage = "Cuenta registrada y verificada en Smart Chiclayo.",
                        currentScreen = if (profile.role == UserRole.ADMINISTRADOR) AppScreen.ADMIN_DASHBOARD else AppScreen.MAIN_APP,
                        currentTab = MainTab.MAP
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        isAuthError = true,
                        authErrorMessage = error.message ?: "No se pudo registrar la cuenta. Intenta nuevamente."
                    )
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.AUTH,
                isAuthError = false,
                authErrorMessage = "",
                authSuccessMessage = null,
                notificationMessage = "Has cerrado sesión."
            )
        }
    }

    fun clearAuthMessages() {
        _uiState.update { it.copy(isAuthError = false, authErrorMessage = "", authSuccessMessage = null) }
    }

    fun selectRoleAndContinue(role: UserRole) {
        repository.setUserRole(role)
        if (role == UserRole.ADMINISTRADOR) {
            _uiState.update { it.copy(currentScreen = AppScreen.ADMIN_DASHBOARD) }
        } else {
            _uiState.update { it.copy(currentScreen = AppScreen.MAIN_APP, currentTab = MainTab.MAP) }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectDestination(destName: String) {
        _uiState.update {
            it.copy(
                destinationInput = destName,
                searchQuery = "",
                currentScreen = AppScreen.ROUTE_OPTIONS
            )
        }
    }

    fun selectRouteOption(route: RouteOption) {
        _uiState.update { it.copy(selectedRoute = route) }
    }

    fun startNavigation() {
        val route = _uiState.value.selectedRoute ?: _uiState.value.routeOptions.firstOrNull()
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.ACTIVE_NAVIGATION,
                navigationState = NavigationState(
                    isActive = true,
                    currentStreet = route?.viaStreet ?: "Av. José Leonardo Ortiz",
                    remainingMinutes = "${route?.durationMinutes ?: 12} min",
                    remainingDistance = "${route?.distanceKm ?: 4.2} km",
                    etaTime = route?.estimatedArrivalTime ?: "14:45 ETA",
                    trafficStatus = "Tráfico Fluido",
                    progressPercent = 0.15f
                )
            )
        }

        // Simulate turn-by-turn progression
        navigationJob?.cancel()
        navigationJob = viewModelScope.launch {
            delay(3000)
            _uiState.update {
                it.copy(
                    navigationState = it.navigationState.copy(
                        distanceToTurn = "250 m",
                        progressPercent = 0.45f
                    )
                )
            }
            delay(3500)
            _uiState.update {
                it.copy(
                    navigationState = it.navigationState.copy(
                        distanceToTurn = "80 m",
                        currentStreet = "Av. Salaverry",
                        directionText = "Gira a la izquierda",
                        remainingMinutes = "8 min",
                        remainingDistance = "2.1 km",
                        progressPercent = 0.70f
                    )
                )
            }
        }
    }

    fun stopNavigation() {
        navigationJob?.cancel()
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.MAIN_APP,
                currentTab = MainTab.MAP,
                navigationState = it.navigationState.copy(isActive = false)
            )
        }
    }

    fun selectTaxiOption(option: TaxiOption) {
        _uiState.update { it.copy(selectedTaxiOption = option) }
    }

    fun requestTaxi() {
        val option = _uiState.value.selectedTaxiOption ?: _uiState.value.taxiOptions.first()
        _uiState.update {
            it.copy(
                taxiRide = TaxiRide(
                    status = TaxiRideStatus.SEARCHING,
                    selectedOption = option,
                    priceSoles = option.priceSoles
                )
            )
        }

        taxiSimulationJob?.cancel()
        taxiSimulationJob = viewModelScope.launch {
            delay(2000)
            _uiState.update {
                it.copy(
                    taxiRide = it.taxiRide.copy(
                        status = TaxiRideStatus.DRIVER_ASSIGNED,
                        driverName = "Carlos Mendoza",
                        driverRating = 4.92,
                        carModel = if (option.iconType == "mototaxi") "Bajaj Torito RE" else "Toyota Yaris Negro",
                        carPlate = "M3F-419",
                        etaMinutes = 2
                    )
                )
            }
            delay(4000)
            _uiState.update {
                it.copy(
                    taxiRide = it.taxiRide.copy(
                        status = TaxiRideStatus.ARRIVING,
                        etaMinutes = 1
                    )
                )
            }
        }
    }

    fun cancelTaxiRequest() {
        taxiSimulationJob?.cancel()
        _uiState.update {
            it.copy(taxiRide = TaxiRide(status = TaxiRideStatus.IDLE))
        }
    }

    fun reportIncident(
        type: IncidentType,
        priority: PriorityLevel,
        locationName: String,
        description: String
    ) {
        repository.addIncident(type, priority, locationName, description)
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.MAIN_APP,
                currentTab = MainTab.MAP,
                notificationMessage = "¡Incidencia reportada con éxito en $locationName!"
            )
        }
    }

    fun toggleTrafficLight(nodeId: String) {
        repository.toggleTrafficLight(nodeId)
    }

    fun selectCameraFeed(camera: CameraFeed) {
        _uiState.update { it.copy(selectedCamera = camera) }
    }

    fun toggleMapLayerMode() {
        val nextMode = when (_uiState.value.mapLayerMode) {
            MapLayerMode.STANDARD -> MapLayerMode.TRAFFIC
            MapLayerMode.TRAFFIC -> MapLayerMode.TRANSIT
            MapLayerMode.TRANSIT -> MapLayerMode.SATELLITE
            MapLayerMode.SATELLITE -> MapLayerMode.STANDARD
        }
        _uiState.update { it.copy(mapLayerMode = nextMode) }
    }

    fun toggleRoleSwitcher(show: Boolean) {
        _uiState.update { it.copy(showRoleSwitcherDialog = show) }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    companion object {
        fun provideFactory(repository: SmartChiclayoRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SmartChiclayoViewModel(repository) as T
                }
            }
    }
}
