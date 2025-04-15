package com.example.projetinfo

class Parking(private val parkingKey: String) : Observable {
    private val observateurs = mutableListOf<Observer>()

    // Récupère les réservations (véhicules) depuis VehicleRepository
    private fun obtenirReservations(): List<String> {
        return VehicleRepository.getVehicles(parkingKey)
    }

    // Retourne le nombre de réservations
    fun obtenirNombreDeReservations(): Int {
        return obtenirReservations().size
    }

    override fun ajouterObservateur(observateur: Observer) {
        observateurs.add(observateur)
    }

    override fun supprimerObservateur(observateur: Observer) {
        observateurs.remove(observateur)
    }

    override fun notifierObservateurs() {
        for (observateur in observateurs) {
            observateur.mettreAJour()
        }
    }
}