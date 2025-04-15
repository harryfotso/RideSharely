package com.example.projetinfo

open class StatistiquesParking(private val parking: Parking) : Observer {
    private var nombreDeReservations: Int = 0

    init {
        parking.ajouterObservateur(this)
    }

    override fun mettreAJour() {
        nombreDeReservations = parking.obtenirNombreDeReservations()
    }

    fun obtenirStatistiques(): String {
        return "Nombre de véhicules : $nombreDeReservations"
    }
}