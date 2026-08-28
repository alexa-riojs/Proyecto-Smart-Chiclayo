package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.data.IncidentReport
import com.example.data.MapLayerMode
import com.example.ui.theme.*

@Composable
fun ChiclayoMapCanvas(
    modifier: Modifier = Modifier,
    layerMode: MapLayerMode = MapLayerMode.STANDARD,
    showActiveRoute: Boolean = false,
    routeProgress: Float = 0.25f,
    isNavigationMode: Boolean = false,
    incidents: List<IncidentReport> = emptyList(),
    onIncidentClick: ((IncidentReport) -> Unit)? = null
) {
    // Pulse animation for GPS & Radar
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    // Animated transit buses movement
    val busProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "busProgress"
    )

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (layerMode == MapLayerMode.SATELLITE) Color(0xFFE5DEEE) else Color(0xFFF6F2FA))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.7f, 3.5f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Center coordinate origin
            val centerX = w * 0.5f + offsetX
            val centerY = h * 0.5f + offsetY

            drawChiclayoMapGrid(
                w = w,
                h = h,
                cx = centerX,
                cy = centerY,
                scale = scale,
                layerMode = layerMode,
                showActiveRoute = showActiveRoute,
                routeProgress = routeProgress,
                isNavigationMode = isNavigationMode,
                pulseScale = pulseScale,
                pulseAlpha = pulseAlpha,
                busProgress = busProgress,
                incidents = incidents
            )
        }
    }
}

private fun DrawScope.drawChiclayoMapGrid(
    w: Float,
    h: Float,
    cx: Float,
    cy: Float,
    scale: Float,
    layerMode: MapLayerMode,
    showActiveRoute: Boolean,
    routeProgress: Float,
    isNavigationMode: Boolean,
    pulseScale: Float,
    pulseAlpha: Float,
    busProgress: Float,
    incidents: List<IncidentReport>
) {
    // 1. Draw Map Background grid lines / blocks (Clean Minimalism Light palette)
    val baseRoadColor = Color(0xFFD6CEE0)
    val arterialRoadColor = Color(0xFFB5A9C4)
    val secondaryRoadColor = Color(0xFFEBE4F3)
    val landSectorColor = Color(0xFFEDE7F6)

    // Sector blocks
    drawRect(
        color = landSectorColor,
        topLeft = Offset(cx - w * 0.45f * scale, cy - h * 0.45f * scale),
        size = androidx.compose.ui.geometry.Size(w * 0.9f * scale, h * 0.9f * scale)
    )

    // Minor street grid
    val gridSpacing = 45f * scale
    for (i in -15..15) {
        val y = cy + i * gridSpacing
        drawLine(
            color = secondaryRoadColor,
            start = Offset(cx - w * scale, y),
            end = Offset(cx + w * scale, y),
            strokeWidth = 2f * scale
        )
        val x = cx + i * gridSpacing
        drawLine(
            color = secondaryRoadColor,
            start = Offset(x, cy - h * scale),
            end = Offset(x, cy + h * scale),
            strokeWidth = 2f * scale
        )
    }

    // 2. Chiclayo Major Arteries
    // Vía Evitamiento (Outer Loop)
    val evitamientoPath = Path().apply {
        moveTo(cx - w * 0.45f * scale, cy - h * 0.35f * scale)
        cubicTo(
            cx - w * 0.30f * scale, cy - h * 0.42f * scale,
            cx + w * 0.20f * scale, cy - h * 0.40f * scale,
            cx + w * 0.45f * scale, cy - h * 0.25f * scale
        )
        cubicTo(
            cx + w * 0.48f * scale, cy + h * 0.15f * scale,
            cx + w * 0.35f * scale, cy + h * 0.42f * scale,
            cx - w * 0.10f * scale, cy + h * 0.45f * scale
        )
        cubicTo(
            cx - w * 0.35f * scale, cy + h * 0.42f * scale,
            cx - w * 0.45f * scale, cy + h * 0.10f * scale,
            cx - w * 0.45f * scale, cy - h * 0.35f * scale
        )
    }
    drawPath(
        path = evitamientoPath,
        color = arterialRoadColor,
        style = Stroke(width = 8f * scale, cap = StrokeCap.Round)
    )

    // Av. José Balta (North-South central spine)
    drawLine(
        color = arterialRoadColor,
        start = Offset(cx, cy - h * 0.45f * scale),
        end = Offset(cx, cy + h * 0.45f * scale),
        strokeWidth = 6f * scale
    )

    // Av. Bolognesi (East-West spine)
    drawLine(
        color = arterialRoadColor,
        start = Offset(cx - w * 0.45f * scale, cy + h * 0.12f * scale),
        end = Offset(cx + w * 0.45f * scale, cy + h * 0.12f * scale),
        strokeWidth = 6f * scale
    )

    // Av. Salaverry (Diagonal Southwestern to center)
    drawLine(
        color = baseRoadColor,
        start = Offset(cx - w * 0.35f * scale, cy + h * 0.38f * scale),
        end = Offset(cx + w * 0.35f * scale, cy - h * 0.25f * scale),
        strokeWidth = 6f * scale
    )

    // Av. José Leonardo Ortiz (Northwest road)
    val jloPath = Path().apply {
        moveTo(cx - w * 0.30f * scale, cy - h * 0.45f * scale)
        lineTo(cx - w * 0.05f * scale, cy - h * 0.05f * scale)
    }
    drawPath(
        path = jloPath,
        color = baseRoadColor,
        style = Stroke(width = 6f * scale)
    )

    // 3. Traffic Heatmap Overlay (if enabled)
    if (layerMode == MapLayerMode.TRAFFIC || showActiveRoute) {
        // Red traffic segment in JLO / Mercado Modelo
        drawLine(
            color = TrafficRed.copy(alpha = 0.85f),
            start = Offset(cx - w * 0.30f * scale, cy - h * 0.45f * scale),
            end = Offset(cx - w * 0.15f * scale, cy - h * 0.22f * scale),
            strokeWidth = 5f * scale
        )
        // Green traffic segments in Balta / Salaverry
        drawLine(
            color = TrafficGreen.copy(alpha = 0.85f),
            start = Offset(cx, cy - h * 0.05f * scale),
            end = Offset(cx, cy + h * 0.35f * scale),
            strokeWidth = 4f * scale
        )
        drawLine(
            color = TrafficGreen.copy(alpha = 0.85f),
            start = Offset(cx - w * 0.25f * scale, cy + h * 0.28f * scale),
            end = Offset(cx + w * 0.15f * scale, cy - h * 0.10f * scale),
            strokeWidth = 4f * scale
        )
    }

    // 4. Navigation / Active Route Highlight (Clean Minimalism Purple with soft glow)
    if (showActiveRoute || isNavigationMode) {
        val routePath = Path().apply {
            moveTo(cx - w * 0.05f * scale, cy + h * 0.32f * scale) // Start (Av. Balta / La Victoria)
            lineTo(cx - w * 0.05f * scale, cy + h * 0.10f * scale) // Av. Balta
            lineTo(cx + w * 0.20f * scale, cy - h * 0.08f * scale) // Av. Salaverry
            lineTo(cx + w * 0.25f * scale, cy - h * 0.30f * scale) // Real Plaza / Vía Evitamiento
        }

        // Outer Soft Purple Glow
        drawPath(
            path = routePath,
            color = MinimalPrimary.copy(alpha = 0.25f),
            style = Stroke(width = 16f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Main Route line in Deep Iris Purple
        drawPath(
            path = routePath,
            color = MinimalPrimary,
            style = Stroke(width = 7f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Alternate route (Av. Salaverry route) in dashed slate
        val altRoute = Path().apply {
            moveTo(cx - w * 0.05f * scale, cy + h * 0.32f * scale)
            lineTo(cx - w * 0.25f * scale, cy + h * 0.05f * scale)
            lineTo(cx + w * 0.25f * scale, cy - h * 0.30f * scale)
        }
        drawPath(
            path = altRoute,
            color = MinimalSecondary.copy(alpha = 0.5f),
            style = Stroke(width = 4f * scale, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f))
        )
    }

    // 5. Landmark & District Badges
    drawDistrictLabel(cx - w * 0.12f * scale, cy - h * 0.28f * scale, "José Leonardo\nOrtiz", scale)
    drawDistrictLabel(cx + w * 0.02f * scale, cy - h * 0.02f * scale, "Chiclayo", scale, isBold = true)
    drawDistrictLabel(cx + w * 0.12f * scale, cy - h * 0.18f * scale, "Santa Victoria", scale)
    drawDistrictLabel(cx + w * 0.04f * scale, cy + h * 0.26f * scale, "La Victoria", scale)
    drawDistrictLabel(cx - w * 0.32f * scale, cy + h * 0.30f * scale, "Aqua Park Chiclayo", scale)

    // Road number badges
    drawRoadBadge(cx - w * 0.38f * scale, cy - h * 0.35f * scale, "1N", scale)
    drawRoadBadge(cx + w * 0.38f * scale, cy - h * 0.38f * scale, "112", scale)
    drawRoadBadge(cx - w * 0.35f * scale, cy + h * 0.15f * scale, "1N", scale)
    drawRoadBadge(cx + w * 0.10f * scale, cy + h * 0.18f * scale, "111", scale)

    // 6. Live Moving Transit Buses & Taxis
    val busX = cx + (w * 0.30f * (busProgress - 0.5f) * 2f) * scale
    val busY = cy + (h * 0.12f) * scale
    drawVehicleMarker(busX, busY, "12", RoleTransit, scale)

    val taxiX = cx + (w * 0.18f * kotlin.math.sin(busProgress * 6.28f.toDouble()).toFloat()) * scale
    val taxiY = cy + (h * 0.15f * kotlin.math.cos(busProgress * 6.28f.toDouble()).toFloat()) * scale
    drawVehicleMarker(taxiX, taxiY, "🚕", RoleTaxi, scale, isCar = true)

    // 7. Incident Hazard Markers
    // Hazard 1: Road Works in JLO
    drawHazardPin(
        x = cx - w * 0.12f * scale,
        y = cy - h * 0.28f * scale,
        label = "Obras",
        color = AlertRed,
        scale = scale
    )

    // Hazard 2: Mercado Modelo
    drawHazardPin(
        x = cx + w * 0.18f * scale,
        y = cy - h * 0.10f * scale,
        label = "Mercado\nde Chiclayo",
        color = MinimalSecondary,
        isStore = true,
        scale = scale
    )

    // 8. User Current GPS Marker with Clean Pulsing Radar Rings
    val userX = if (isNavigationMode) {
        cx - w * 0.05f * scale + (w * 0.25f * routeProgress) * scale
    } else {
        cx - w * 0.05f * scale
    }
    val userY = if (isNavigationMode) {
        cy + h * 0.32f * scale - (h * 0.35f * routeProgress) * scale
    } else {
        cy + h * 0.32f * scale
    }

    // Outer radar ring in soft lavender
    drawCircle(
        color = MinimalPrimary.copy(alpha = pulseAlpha * 0.4f),
        radius = 36f * scale * pulseScale,
        center = Offset(userX, userY)
    )
    // Middle solid ring
    drawCircle(
        color = MinimalPrimaryContainer.copy(alpha = 0.8f),
        radius = 16f * scale,
        center = Offset(userX, userY)
    )
    // Core dot
    drawCircle(
        color = MinimalPrimary,
        radius = 9f * scale,
        center = Offset(userX, userY)
    )
    drawCircle(
        color = Color.White,
        radius = 4f * scale,
        center = Offset(userX, userY)
    )
}

private fun DrawScope.drawDistrictLabel(x: Float, y: Float, text: String, scale: Float, isBold: Boolean = false) {
    // Subtle background glow for labels in light minimalism
    drawCircle(
        color = MinimalSurface.copy(alpha = 0.85f),
        radius = 28f * scale,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawRoadBadge(x: Float, y: Float, badgeText: String, scale: Float) {
    drawRoundRect(
        color = MinimalSurfaceVariant,
        topLeft = Offset(x - 14f * scale, y - 9f * scale),
        size = androidx.compose.ui.geometry.Size(28f * scale, 18f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale, 4f * scale)
    )
    drawRoundRect(
        color = MinimalBorder,
        topLeft = Offset(x - 14f * scale, y - 9f * scale),
        size = androidx.compose.ui.geometry.Size(28f * scale, 18f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale, 4f * scale),
        style = Stroke(width = 1f * scale)
    )
}

private fun DrawScope.drawVehicleMarker(x: Float, y: Float, label: String, color: Color, scale: Float, isCar: Boolean = false) {
    drawCircle(
        color = color.copy(alpha = 0.3f),
        radius = 14f * scale,
        center = Offset(x, y)
    )
    drawCircle(
        color = color,
        radius = 8f * scale,
        center = Offset(x, y)
    )
    drawCircle(
        color = Color.White,
        radius = 3f * scale,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawHazardPin(
    x: Float,
    y: Float,
    label: String,
    color: Color,
    isStore: Boolean = false,
    scale: Float
) {
    // Draw pin head
    drawCircle(
        color = color,
        radius = 12f * scale,
        center = Offset(x, y - 10f * scale)
    )
    // Draw pin bottom point
    val pinPath = Path().apply {
        moveTo(x - 8f * scale, y - 6f * scale)
        lineTo(x, y + 6f * scale)
        lineTo(x + 8f * scale, y - 6f * scale)
        close()
    }
    drawPath(path = pinPath, color = color)
    // Inner icon dot
    drawCircle(
        color = Color.White,
        radius = 4f * scale,
        center = Offset(x, y - 10f * scale)
    )
}
