package com.example.projetinfo

import java.io.Serializable

open class Voiture(
    val marque: String,
    val modele: String,
    val annee: Int,
    val couleur: String,
    val tarif: Double,
    val type: String
) {
    // Méthodes communes à toutes les voitures
}

class VoitureElectrique(
    marque: String,
    modele: String,
    annee: Int,
    couleur: String,
    tarif: Double
) : Voiture(marque, modele, annee, couleur, tarif, "Électrique") {

    fun niveauBatterie(): Int {
        return (0..100).random()  // Niveau de batterie généré aléatoirement
    }
}

class VoitureEssence(
    marque: String,
    modele: String,
    annee: Int,
    couleur: String,
    tarif: Double
) : Voiture(marque, modele, annee, couleur, tarif, "Essence") {

    fun niveauEssence(): Int {
        return (0..100).random()  // Niveau d'essence généré aléatoirement
    }
}

