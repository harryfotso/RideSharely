package com.example.projetinfo

import android.content.Context
import android.content.SharedPreferences

object VehicleRepository {
    private lateinit var sharedPreferences: SharedPreferences // Déclare une variable sharedPreferences, utilisée pour accéder au système de stockage local clé/valeur d'Android

    private val vehiclesByParking = mutableMapOf<String, MutableList<String>>() // Crée une map qui associe chaque nom de parking (String) à une liste mutable de véhicules (MutableList<String>)

    // Fonction d'initialisation
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("ParkingData", Context.MODE_PRIVATE) // Initialise sharedPreferences avec le fichier nommé "VehicleRepo"
        loadVehicles() // Charge les véhicules enregistrés dans le fichier "ParkingData" et les place dans vehiclesByParking
    }

    // Méthode pour ajouter un véhicule dans un parking spécifique
    fun addVehicle(vehicleDescription: String, parkingKey: String) {
        val vehicles = vehiclesByParking.getOrPut(parkingKey) { mutableListOf() } // Récupère la liste de véhicules pour ce parking, ou crée une nouvelle liste vide si elle n’existe pas encore
        vehicles.add(vehicleDescription) // Ajoute la description du nouveau véhicule à la liste
        saveVehicles() // Enregistre les nouvelles données dans SharedPreferences
    }

    // Méthode pour récupérer la liste des véhicules d’un parking donné
    fun getVehicles(parkingKey: String): List<String> {
        return vehiclesByParking[parkingKey] ?: emptyList() // Si le parking n’a pas de véhicule, retourne une liste vide
    }

    fun removeVehicle(vehicle: String, parking: String) {
        val vehicles = vehiclesByParking[parking]
        if (vehicles != null) {
            vehicles.remove(vehicle)
            saveVehicles()
        }
    }

    // Prépare un éditeur pour écrire dans les SharedPreferences.
    private fun saveVehicles() {
        val editor = sharedPreferences.edit()
        vehiclesByParking.forEach { (parking, vehicles) -> // Pour chaque parking, la liste est convertit en Set<String> et est stockée dans le fichier sous la clé du parking
            editor.putStringSet(parking, vehicles.toSet())
        }
        editor.apply() //
    }

    private fun loadVehicles() {
        vehiclesByParking.clear() // Vide complètement la Map en mémoire (remise à zéro)
        sharedPreferences.all.forEach { (key, value) ->
            if (value is Set<*>) {
                val vehicleList = value.map { it.toString() }.toMutableList() // Recrée une liste de véhicules à partir du Set, puis la stocke dans vehiclesByParking
                vehiclesByParking[key] = vehicleList
            }
        }
    }

}
