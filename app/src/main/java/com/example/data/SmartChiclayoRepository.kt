package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SmartChiclayoRepository {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "USR-7714",
            operatorId = "OP-CHICLAYO-01",
            name = "Admin User",
            email = "operador.chiclayo@smartcity.pe",
            role = UserRole.CIUDADANO,
            clearanceLevel = "GENERAL ADMINISTRATOR",
            coordinates = "SYS.AUTH.v4.41 // LAT: -6.7714 LON: -79.8409"
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _transitLines = MutableStateFlow(
        listOf(
            TransitLine(
                id = "line_12",
                number = "12",
                name = "Línea 12",
                routeName = "La Victoria - Centro",
                status = "En camino",
                distanceMeters = "800 m",
                estimatedTime = "4 min",
                badgeColor = 0xFF00E5FF,
                hasDelay = false,
                currentStreet = "Av. Santa Victoria / Los Rosales"
            ),
            TransitLine(
                id = "line_05",
                number = "05",
                name = "Línea 05",
                routeName = "Moshoqueque - UNPRG",
                status = "Retraso",
                distanceMeters = "3.2 km",
                estimatedTime = "15 min",
                badgeColor = 0xFF263238,
                hasDelay = true,
                currentStreet = "Av. José Leonardo Ortiz"
            ),
            TransitLine(
                id = "line_c1",
                number = "C1",
                name = "Combi 1",
                routeName = "Pomalca - Chiclayo",
                status = "En ruta",
                distanceMeters = "1.5 km",
                estimatedTime = "8 min",
                badgeColor = 0xFFECEFF1,
                hasDelay = false,
                currentStreet = "Av. Bolognesi"
            ),
            TransitLine(
                id = "line_33",
                number = "33",
                name = "Línea 33",
                routeName = "José Leonardo Ortiz",
                status = "En ruta",
                distanceMeters = "2.1 km",
                estimatedTime = "12 min",
                badgeColor = 0xFF37474F,
                hasDelay = false,
                currentStreet = "Av. Balta Norte"
            ),
            TransitLine(
                id = "line_17",
                number = "17",
                name = "Línea 17",
                routeName = "Pimentel - Chiclayo Centro",
                status = "En camino",
                distanceMeters = "4.0 km",
                estimatedTime = "9 min",
                badgeColor = 0xFF00B4D8,
                hasDelay = false,
                currentStreet = "Carretera Pimentel"
            )
        )
    )
    val transitLines: StateFlow<List<TransitLine>> = _transitLines.asStateFlow()

    private val _routeOptions = MutableStateFlow(
        listOf(
            RouteOption(
                id = "route_1",
                name = "Vía Evitamiento",
                durationMinutes = 12,
                distanceKm = 4.2,
                trafficCondition = "TRAFFIC: FLUID",
                tag = "MÁS RÁPIDA",
                isFastest = true,
                isSafest = false,
                estimatedArrivalTime = "14:45 ETA",
                viaStreet = "Vía Evitamiento"
            ),
            RouteOption(
                id = "route_2",
                name = "Av. Salaverry",
                durationMinutes = 18,
                distanceKm = 3.8,
                trafficCondition = "TRAFFIC: FLUID",
                tag = "RUTA MÁS SEGURA",
                isFastest = false,
                isSafest = true,
                estimatedArrivalTime = "14:51 ETA",
                viaStreet = "Av. Salaverry"
            ),
            RouteOption(
                id = "route_3",
                name = "Av. José Balta",
                durationMinutes = 22,
                distanceKm = 3.5,
                trafficCondition = "TRAFFIC: MODERATE",
                tag = "COMERCIAL",
                isFastest = false,
                isSafest = false,
                estimatedArrivalTime = "14:55 ETA",
                viaStreet = "Av. José Balta"
            )
        )
    )
    val routeOptions: StateFlow<List<RouteOption>> = _routeOptions.asStateFlow()

    private val _taxiOptions = MutableStateFlow(
        listOf(
            TaxiOption(
                id = "taxi_standard",
                name = "Standard",
                priceSoles = 12.00,
                etaMinutes = 3,
                subtitle = "Económico",
                iconType = "standard",
                isPopular = true
            ),
            TaxiOption(
                id = "taxi_premium",
                name = "Premium",
                priceSoles = 18.50,
                etaMinutes = 5,
                subtitle = "Mayor confort",
                iconType = "premium"
            ),
            TaxiOption(
                id = "taxi_mototaxi",
                name = "Moto-Taxi",
                priceSoles = 6.00,
                etaMinutes = 2,
                subtitle = "Rápido & Express Chiclayano",
                iconType = "mototaxi"
            )
        )
    )
    val taxiOptions: StateFlow<List<TaxiOption>> = _taxiOptions.asStateFlow()

    private val _incidents = MutableStateFlow(
        listOf(
            IncidentReport(
                id = "INC-101",
                type = IncidentType.ROAD_WORK,
                priority = PriorityLevel.MEDIUM,
                locationName = "Av. José Leonardo Ortiz #420",
                latitude = -6.7620,
                longitude = -79.8420,
                description = "Obras de recapeo asfáltico en carril derecho. Tránsito lento.",
                timestamp = "Hace 15 min",
                status = "Confirmado",
                votes = 14
            ),
            IncidentReport(
                id = "INC-102",
                type = IncidentType.ACCIDENT,
                priority = PriorityLevel.HIGH,
                locationName = "Av. Balta y Av. Bolognesi",
                latitude = -6.7710,
                longitude = -79.8390,
                description = "Colisión vehicular menor bloqueando intersección.",
                timestamp = "Hace 28 min",
                status = "En atención",
                votes = 8
            ),
            IncidentReport(
                id = "INC-103",
                type = IncidentType.CONGESTION,
                priority = PriorityLevel.MEDIUM,
                locationName = "Mercado Modelo de Chiclayo",
                latitude = -6.7680,
                longitude = -79.8360,
                description = "Alto flujo de transporte y carga matutina.",
                timestamp = "Hace 40 min",
                status = "Monitoreado",
                votes = 22
            ),
            IncidentReport(
                id = "INC-104",
                type = IncidentType.POTHOLE,
                priority = PriorityLevel.LOW,
                locationName = "Av. Salaverry cdra 11",
                latitude = -6.7780,
                longitude = -79.8450,
                description = "Bache pronunciado en carril central.",
                timestamp = "Hace 2 horas",
                status = "Programado",
                votes = 5
            )
        )
    )
    val incidents: StateFlow<List<IncidentReport>> = _incidents.asStateFlow()

    private val _trafficLightNodes = MutableStateFlow(
        listOf(
            TrafficLightNode("Node A-12", "Av. Bolognesi", TrafficLightState.GREEN, 42),
            TrafficLightNode("Node B-04", "Cora Sur", TrafficLightState.RED, 18),
            TrafficLightNode("Node C-08", "Av. Lora y Lora", TrafficLightState.YELLOW, 5),
            TrafficLightNode("Node A-15", "Plaza de Armas", TrafficLightState.GREEN, 35)
        )
    )
    val trafficLightNodes: StateFlow<List<TrafficLightNode>> = _trafficLightNodes.asStateFlow()

    private val _cameraFeeds = MutableStateFlow(
        listOf(
            CameraFeed(
                id = "CAM-01",
                locationName = "Av. Salaverry / Av. Balta",
                status = "Normal Traffic",
                isLive = true,
                trafficDensity = "status: Normal Traffic"
            ),
            CameraFeed(
                id = "CAM-04",
                locationName = "Av. Leonardo Ortiz",
                status = "Dense Traffic",
                isLive = true,
                trafficDensity = "status: Dense Traffic"
            ),
            CameraFeed(
                id = "CAM-07",
                locationName = "Plaza Principal / San José",
                status = "Clear",
                isLive = true,
                trafficDensity = "status: Fluid Flow"
            ),
            CameraFeed(
                id = "CAM-12",
                locationName = "Mercado Modelo / Balta",
                status = "Heavy Flow",
                isLive = true,
                trafficDensity = "status: Congestion Alert"
            )
        )
    )
    val cameraFeeds: StateFlow<List<CameraFeed>> = _cameraFeeds.asStateFlow()

    val popularLocations = listOf(
        ChiclayoLocation("Casa", "Calle Los Pinos 234, Santa Victoria", "Favorito", "home", 0.35f, 0.65f),
        ChiclayoLocation("Trabajo", "Av. Salaverry 850, Chiclayo Centro", "Favorito", "work", 0.50f, 0.45f),
        ChiclayoLocation("Centro Comercial", "Real Plaza Chiclayo, Av. Bolognesi", "Comercio", "shopping_bag", 0.70f, 0.72f),
        ChiclayoLocation("Mercado Modelo", "Av. Balta / Arica, Chiclayo", "Comercio", "store", 0.55f, 0.35f),
        ChiclayoLocation("Universidad Sipán", "Carretera a Pimentel km 5", "Educación", "school", 0.20f, 0.80f),
        ChiclayoLocation("Hospital Las Mercedes", "Av. Luis Gonzales", "Salud", "local_hospital", 0.48f, 0.40f)
    )

    fun setUserRole(role: UserRole) {
        _userProfile.update { it.copy(role = role) }
    }

    fun addIncident(
        type: IncidentType,
        priority: PriorityLevel,
        locationName: String,
        description: String
    ) {
        val newIncident = IncidentReport(
            id = "INC-${(100..999).random()}",
            type = type,
            priority = priority,
            locationName = locationName,
            latitude = -6.7714 + (Math.random() - 0.5) * 0.02,
            longitude = -79.8409 + (Math.random() - 0.5) * 0.02,
            description = description.ifBlank { "Incidencia reportada por ciudadano." },
            timestamp = "Justo ahora",
            status = "Reportado",
            votes = 1
        )
        _incidents.update { listOf(newIncident) + it }
    }

    fun toggleTrafficLight(nodeId: String) {
        _trafficLightNodes.update { list ->
            list.map { node ->
                if (node.id == nodeId) {
                    val nextState = when (node.state) {
                        TrafficLightState.GREEN -> TrafficLightState.YELLOW
                        TrafficLightState.YELLOW -> TrafficLightState.RED
                        TrafficLightState.RED -> TrafficLightState.GREEN
                    }
                    node.copy(state = nextState)
                } else node
            }
        }
    }
}
