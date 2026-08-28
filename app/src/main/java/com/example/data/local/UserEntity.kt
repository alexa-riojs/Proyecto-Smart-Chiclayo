package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.UserProfile
import com.example.data.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val operatorId: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val role: String,
    val clearanceLevel: String,
    val coordinates: String = "LAT: -6.7714 LON: -79.8409",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
) {
    fun toUserProfile(): UserProfile {
        val parsedRole = try {
            UserRole.valueOf(role)
        } catch (e: Exception) {
            UserRole.CIUDADANO
        }
        return UserProfile(
            id = id,
            operatorId = operatorId,
            name = fullName,
            email = email,
            role = parsedRole,
            clearanceLevel = clearanceLevel,
            coordinates = coordinates
        )
    }
}
