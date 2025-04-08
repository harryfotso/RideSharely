package com.example.projetinfo

import android.content.Context
import android.content.SharedPreferences

object VehicleRepository {
    private lateinit var sharedPreferences: SharedPreferences //On déclare une variable sharedPreferences, utilisée pour accéder au système de stockage local clé/valeur d'Android.
    //lateinit signifie qu'elle sera initialisée plus tard (dans init()).
    private val vehiclesByParking = mutableMapOf<String, MutableList<String>>() //Crée une map qui associe chaque nom de parking (String) à une liste mutable de véhicules (MutableList<String>)

    // Fonction d'initialisation
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("ParkingData", Context.MODE_PRIVATE) //Initialise sharedPreferences avec le fichier nommé "VehicleRepo"
        loadVehicles() //Charge les véhicules enregistrés dans le fichier "ParkingData" et les place dans vehiclesByParking
    }

    //Méthode publique pour ajouter un véhicule dans un parking spécifique.
    fun addVehicle(vehicleDescription: String, parkingKey: String) {
        val vehicles = vehiclesByParking.getOrPut(parkingKey) { mutableListOf() } // Récupère la liste de véhicules pour ce parking, ou crée une nouvelle liste vide si elle n’existe pas encore
        vehicles.add(vehicleDescription) // Ajoute la description du nouveau véhicule à la liste.
        saveVehicles() // Enregistre les nouvelles données dans les SharedPreferences.
    }

    //Méthode publique pour récupérer la liste des véhicules d’un parking donné.
    //Si le parking n’a pas de véhicule, retourne une liste vide.
    fun getVehicles(parkingKey: String): List<String> {
        return vehiclesByParking[parkingKey] ?: emptyList()
    }

    fun removeVehicle(vehicle: String, parking: String) {
        val vehicles = vehiclesByParking[parking]
        if (vehicles != null) {
            vehicles.remove(vehicle)
            saveVehicles()
        }
    }

    //Prépare un éditeur pour écrire dans les SharedPreferences.
    private fun saveVehicles() {
        val editor = sharedPreferences.edit()
        vehiclesByParking.forEach { (parking, vehicles) -> //Pour chaque parking, convertit la liste en Set<String> et la stocke dans le fichier sous la clé du parking.
            editor.putStringSet(parking, vehicles.toSet())
        }
        editor.apply() //Applique les modifications de manière asynchrone (sans bloquer l’interface utilisateur)
    }

    private fun loadVehicles() {
        vehiclesByParking.clear() //Vide complètement la Map en mémoire (remise à zéro).
        sharedPreferences.all.forEach { (key, value) -> //Parcourt toutes les paires clé/valeur dans le fichier "ParkingData".
            if (value is Set<*>) { //Vérifie que la valeur stockée est bien un Set (ensemble de chaînes de caractères).
                val vehicleList = value.map { it.toString() }.toMutableList() // On recrée une liste de véhicules à partir du Set, puis la stocke dans vehiclesByParking.
                vehiclesByParking[key] = vehicleList
            }
        }
    }

}
