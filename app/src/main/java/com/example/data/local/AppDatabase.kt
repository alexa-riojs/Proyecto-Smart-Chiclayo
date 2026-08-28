package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_chiclayo_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialUsers(database.userDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        if (database.userDao().countUsers() == 0) {
                            populateInitialUsers(database.userDao())
                        }
                    }
                }
            }

            suspend fun populateInitialUsers(userDao: UserDao) {
                val initialUsers = listOf(
                    UserEntity(
                        id = "USR-001",
                        operatorId = "OP-CHICLAYO-01",
                        fullName = "Administrador Municipal",
                        email = "operador.chiclayo@smartcity.pe",
                        phone = "+51 974 812 390",
                        passwordHash = "admin123",
                        role = "ADMINISTRADOR",
                        clearanceLevel = "LEVEL-4 MUNICIPAL DISPATCH",
                        coordinates = "LAT: -6.7714 LON: -79.8409"
                    ),
                    UserEntity(
                        id = "USR-002",
                        operatorId = "OP-CIUDADANO-02",
                        fullName = "Alejandro Vilcabana",
                        email = "vilcabanaale@gmail.com",
                        phone = "+51 987 654 321",
                        passwordHash = "chiclayo2026",
                        role = "CIUDADANO",
                        clearanceLevel = "CIUDADANO VERIFICADO",
                        coordinates = "LAT: -6.7725 LON: -79.8385"
                    ),
                    UserEntity(
                        id = "USR-003",
                        operatorId = "BUS-LINE12-88",
                        fullName = "Manuel Gonzáles (Chofer)",
                        email = "chofer.linea12@chiclayo.pe",
                        phone = "+51 944 112 334",
                        passwordHash = "bus123",
                        role = "CONDUCTOR_TRANSPORTE",
                        clearanceLevel = "LINEA 12 - UNIDAD 04",
                        coordinates = "LAT: -6.7650 LON: -79.8420"
                    ),
                    UserEntity(
                        id = "USR-004",
                        operatorId = "TAXI-FAST-19",
                        fullName = "Carlos Mendoza (Taxi)",
                        email = "carlos.taxi@chiclayo.pe",
                        phone = "+51 955 778 990",
                        passwordHash = "taxi123",
                        role = "CONDUCTOR_TAXI",
                        clearanceLevel = "TAXI VIP CHICLAYO - M3F-419",
                        coordinates = "LAT: -6.7790 LON: -79.8350"
                    )
                )

                for (user in initialUsers) {
                    try {
                        userDao.insertUser(user)
                    } catch (_: Exception) {
                        // Already exists
                    }
                }
            }
        }
    }
}
