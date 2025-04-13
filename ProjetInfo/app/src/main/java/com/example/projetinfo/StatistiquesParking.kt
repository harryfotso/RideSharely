package com.example.projetinfo

open class StatistiquesParking(private val parking: Parking) : Observer {
    init {
        parking.ajouterObservateur(this)
    }

    override fun mettreAJour() {
        val nombreDeReservations = parking.obtenirNombreDeReservations()
        println("Statistiques mises à jour : Nombre de réservations = $nombreDeReservations")
    }
}