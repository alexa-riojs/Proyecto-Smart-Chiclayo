package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class UserRole(
    val title: String,
    val description: String,
    val iconName: String,
    val badgeColor: Long
) {
    CIUDADANO(
        title = "Ciudadano",
        description = "Planifica tu ruta y viaja mejor.",
        iconName = "person",
        badgeColor = 0xFF00E5FF
    ),
    CONDUCTOR_TRANSPORTE(
        title = "Conductor de Transporte",
        description = "Gestiona tu línea de servicio.",
        iconName = "directions_bus",
        badgeColor = 0xFF29B6F6
    ),
    CONDUCTOR_TAXI(
        title = "Conductor de Taxi",
        description = "Recibe solicitudes y genera ingresos.",
        iconName = "local_taxi",
        badgeColor = 0xFFFFCA28
    ),
    ADMINISTRADOR(
        title = "Administrador",
        description = "Gestión y control de movilidad de la ciudad.",
        iconName = "admin_panel_settings",
        badgeColor = 0xFF7C4DFF
    )
}

data class UserProfile(
    val id: String = "USR-7714",
    val operatorId: String = "OP-CHICLAYO-01",
    val name: String = "Admin User",
    val email: String = "operador.chiclayo@smartcity.pe",
    val role: UserRole = UserRole.CIUDADANO,
    val clearanceLevel: String = "LEVEL-4 OMNI",
    val coordinates: String = "LAT: -6.7714 LON: -79.8409"
)

enum class IncidentType(
    val displayName: String,
    val iconEmoji: String,
    val color: Long
) {
    ACCIDENT("Accident", "💥", 0xFFFF3D57),
    ROAD_WORK("Road Work", "🛠", 0xFFFF9100),
    POTHOLE("Pothole", "🕳", 0xFFFFD600),
    CONGESTION("Congestion", "🚦", 0xFFFF7043),
    EMERGENCY("Emergency", "🚨", 0xFFE040FB)
}

enum class PriorityLevel(
    val displayName: String,
    val severityColor: Long
) {
    LOW("Baja - Tráfico leve", 0xFF00E676),
    MEDIUM("Media - Tráfico lento", 0xFFFFD600),
    HIGH("Alta - Bloqueo de carril", 0xFFFF9100),
    CRITICAL("Crítica - Vía cerrada", 0xFFFF3D57)
}

data class IncidentReport(
    val id: String,
    val type: IncidentType,
    val priority: PriorityLevel,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val timestamp: String,
    val status: String = "Reportado",
    val votes: Int = 1,
    val reporter: String = "Operador Chiclayo"
)

data class TransitLine(
    val id: String,
    val number: String,
    val name: String,
    val routeName: String,
    val status: String,
    val distanceMeters: String,
    val estimatedTime: String,
    val badgeColor: Long,
    val hasDelay: Boolean = false,
    val stopsCount: Int = 14,
    val frequencyMinutes: String = "6-8 min",
    val nextBusPlate: String = "M3A-789",
    val currentStreet: String = "Av. Bolognesi"
)

data class RouteOption(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val distanceKm: Double,
    val trafficCondition: String,
    val tag: String,
    val isFastest: Boolean = false,
    val isSafest: Boolean = false,
    val estimatedArrivalTime: String = "14:45",
    val viaStreet: String = "Vía Evitamiento"
)

data class TaxiOption(
    val id: String,
    val name: String,
    val priceSoles: Double,
    val etaMinutes: Int,
    val subtitle: String,
    val iconType: String = "standard",
    val isPopular: Boolean = false
)

enum class TaxiRideStatus {
    IDLE,
    SEARCHING,
    DRIVER_ASSIGNED,
    ARRIVING,
    IN_RIDE,
    COMPLETED
}

data class TaxiRide(
    val status: TaxiRideStatus = TaxiRideStatus.IDLE,
    val selectedOption: TaxiOption? = null,
    val pickupAddress: String = "Av. Balta 123, Chiclayo",
    val destinationAddress: String = "Real Plaza Chiclayo",
    val driverName: String = "Juan Carlos Perez",
    val driverPhoto: String = "",
    val driverRating: Double = 4.9,
    val carModel: String = "Toyota Yaris Sedan",
    val carPlate: String = "M4K-812",
    val carColor: String = "Negro Metálico",
    val priceSoles: Double = 12.00,
    val etaMinutes: Int = 3
)

enum class TrafficLightState {
    GREEN,
    YELLOW,
    RED
}

data class TrafficLightNode(
    val id: String,
    val locationName: String,
    val state: TrafficLightState,
    val timerSeconds: Int,
    val isAutomated: Boolean = true
)

data class CameraFeed(
    val id: String,
    val locationName: String,
    val status: String,
    val isLive: Boolean = true,
    val trafficDensity: String = "Normal Traffic",
    val resolution: String = "1080p 30fps"
)

data class NavigationStep(
    val distanceMeters: Int,
    val streetName: String,
    val instruction: String,
    val iconType: String
)

data class NavigationState(
    val isActive: Boolean = false,
    val currentStepIndex: Int = 0,
    val steps: List<NavigationStep> = listOf(
        NavigationStep(400, "Av. José Leonardo Ortiz", "Hacia el norte", "turn_right"),
        NavigationStep(800, "Av. Salaverry", "Continúa recto por 800m", "straight"),
        NavigationStep(1200, "Vía Evitamiento", "Gira a la izquierda", "turn_left"),
        NavigationStep(800, "Av. Panamericana Norte", "Llegada a Real Plaza", "flag")
    ),
    val distanceToTurn: String = "400 m",
    val currentStreet: String = "Av. José Leonardo Ortiz",
    val directionText: String = "Hacia el norte",
    val remainingMinutes: String = "12 min",
    val etaTime: String = "14:45 ETA",
    val remainingDistance: String = "3.2 km",
    val trafficStatus: String = "Tráfico Fluido",
    val alertWarning: String? = "Obras en 300m (J. L. Ortiz)",
    val progressPercent: Float = 0.25f
)

data class ChiclayoLocation(
    val name: String,
    val address: String,
    val category: String,
    val icon: String,
    val xRatio: Float,
    val yRatio: Float
)

enum class MapLayerMode {
    STANDARD,
    TRAFFIC,
    TRANSIT,
    SATELLITE
}
