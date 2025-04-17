package com.example.projetinfo

import java.io.Serializable
import java.util.Calendar


class Reservation(
    val marque: String,
    val modele: String,
    val dateDebut: Calendar,
    val dateFin: Calendar,
    val tarifJournalier: Double,
    var nombreDeJours: Int,
    var montantTotal: Double
): Serializable {
    companion object {
        const val localisation = "Solbosch"
    }

    fun calculerDuree(): Int {
        val diff = dateFin.timeInMillis - dateDebut.timeInMillis
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }

    fun calculerMontantTotal(): Double {
        val jours = calculerDuree()
        return tarifJournalier * jours
    }
}

