package com.example.projetinfo

class Parking(private val parkingKey: String) : Observable {
    override val observateurs: MutableList<Observer> = mutableListOf()

    // Récupère tous les véhicules qui sont dans le VehicleRepository
    private fun obtenirVoitures(): List<String> {
        return VehicleRepository.getVehicles(parkingKey)
    }

    // Retourne le nombre de voitures
    fun obtenirNombreDeVoitures(): Int {
        return obtenirVoitures().size
    }
}