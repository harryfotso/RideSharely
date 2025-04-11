package com.example.projetinfo

import java.util.Calendar

class Reservation(
    val modele: String,
    val dateDebut: Calendar,
    val dateFin: Calendar,
    val tarifJournalier: Double,
    val nombreDeJours: Int,
    var montantTotal: Double
) {
    fun calculerDuree(): Int {
        val diff = dateFin.timeInMillis - dateDebut.timeInMillis
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }

    fun calculerMontantTotal(): Double {
        val jours = calculerDuree()
        return tarifJournalier * jours
    }
}
