package com.example.projetinfo

class Parking : Observable {
    private val observateurs = mutableListOf<Observer>()
    private val reservations = mutableListOf<Reservation>() // Liste des réservations

    fun ajouterReservation(reservation: Reservation) {
        reservations.add(reservation)
        notifierObservateurs()
    }

    fun retirerReservation(reservation: Reservation) {
        reservations.remove(reservation)
        notifierObservateurs()
    }

    fun obtenirNombreDeReservations(): Int {
        return reservations.size
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