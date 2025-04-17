package com.example.projetinfo

import java.time.LocalDate


class Payment(
    private val numCarte: String,
    private val dateExpiration: String,
    private val cvv: String,
    private val reservation: Reservation,
) {


    fun afficherMontant(): String {
        return "Montant à payer : ${reservation.montantTotal} €"
    }

    // Vérifie si le CVV est bien composé de 3 chiffres
    fun verifierCvv(): Boolean {
        val regex = "^\\d{3}\$".toRegex()  // Vérifie que le CVV est composé exactement de 3 chiffres
        return cvv.matches(regex)
    }

    // Vérifie si la carte est expirée
    fun verifierDateExpiration(): Boolean {
        val expirationMonth = dateExpiration.take(2).toInt()
        val expirationYear = "20${dateExpiration.takeLast(2)}".toInt()  // Convertir AA en 20AA

        val currentDate = LocalDate.now()
        val expirationDate = LocalDate.of(expirationYear, expirationMonth, 1)

        return !currentDate.isAfter(expirationDate)
    }

    // Vérifie si le numéro de carte est valide (exemple : 16 chiffres)
    fun verifierNumCarte(): Boolean {
        val regex = "^\\d{16}\$".toRegex()  // Vérifie que le numéro de carte a exactement 16 chiffres
        return numCarte.matches(regex)
    }
}

