package com.example.projetinfo

class StatistiquesParking(private val parking: Parking) : Observer {
    private var nombreDeVoitures: Int = 0

    init {
        parking.ajouter(this) // S'enregistre comme observateur
    }

    override fun mettreAJour() {
        nombreDeVoitures = parking.obtenirNombreDeVoitures()
        println("Statistiques mises à jour : $nombreDeVoitures réservations")
    }
}