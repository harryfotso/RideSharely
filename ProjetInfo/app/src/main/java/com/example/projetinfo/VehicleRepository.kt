package com.example.projetinfo

import android.content.Context
import android.content.SharedPreferences

object VehicleRepository {
    private lateinit var sharedPreferences: SharedPreferences
    private val vehiclesByParking = mutableMapOf<String, MutableList<String>>()

    // Fonction d'initialisation
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("VehicleRepo", Context.MODE_PRIVATE)
        loadVehicles()
    }

    fun addVehicle(vehicleDescription: String, parking: String) {
        val vehicles = vehiclesByParking.getOrPut(parking) { mutableListOf() }
        vehicles.add(vehicleDescription)
        saveVehicles()
    }

    fun getVehicles(parking: String): List<String> {
        return vehiclesByParking[parking] ?: emptyList()
    }

    private fun saveVehicles() {
        val editor = sharedPreferences.edit()
        vehiclesByParking.forEach { (parking, vehicles) ->
            editor.putStringSet(parking, vehicles.toSet())
        }
        editor.apply()
    }

    private fun loadVehicles() {
        vehiclesByParking.clear()
        sharedPreferences.all.forEach { (key, value) ->
            if (value is Set<*>) {
                val vehicleList1 = value.map { it.toString() }.toMutableList()
                vehiclesByParking[key] = vehicleList1
                val vehicleList2 = value.map { it.toString() }.toMutableList()
                vehiclesByParking[key] = vehicleList2
                val vehicleList3 = value.map { it.toString() }.toMutableList()
                vehiclesByParking[key] = vehicleList3

                // 🔥 Debugging: Afficher les véhicules chargés
                println("Chargement du parking $key : ${vehiclesByParking[key]}")
            }
        }
    }

}
