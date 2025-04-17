package com.example.projetinfo

import java.io.Serializable
import kotlin.random.Random

open class Voiture(
    val marque: String,
    val modele: String,
    val annee: Int,
    val couleur: String,
    val tarif: Double,
    val type: String
) : Serializable {


    protected var tarifSpecial: Double = tarif

    // Méthode pour calculer un tarif avec un réduction de 20%
    protected fun calculerTarifSpecial(): Double {
        return tarif * 0.8
    }

    // Méthode pour afficher la consommation de la voiture
    open fun afficherConsommation(): String {
        return "La consommation de la voiture n'est pas définie."
    }
}


class VoitureElectrique(
    marque: String,
    modele: String,
    annee: Int,
    couleur: String,
    tarif: Double
) : Voiture(marque, modele, annee, couleur, tarif, "Électrique"), Energie, Serializable {

    // Méthode pour appliquer le tarif avec réduction à une voiture électrique
    fun appliquerReduction() : String {
        tarifSpecial = calculerTarifSpecial()
        return "Le tarif avec la réduction pour cette voiture électrique est : $tarifSpecial €"
    }

    // Implémentation de la méthode afficherConsommation pour les voitures électriques
    override fun afficherConsommation(): String {
        // Consommation entre 15 et 25 kWh/100km pour une voiture électrique
        val consommation = Random.nextDouble(15.0, 25.0)
        return "La consommation de la voiture électrique est de ${"%.2f".format(consommation)} kWh/100km."
    }
}


class VoitureEssence(
    marque: String,
    modele: String,
    annee: Int,
    couleur: String,
    tarif: Double
) : Voiture(marque, modele, annee, couleur, tarif, "Essence"), Energie, Serializable {

    // Méthode pour appliquer le tarif avec réduction à une voiture à essence
    fun appliquerReduction(): String {
        tarifSpecial = calculerTarifSpecial() // Appel à la méthode de la classe parente
        return "Le tarif avec la réduction pour cette voiture à essence est : $tarifSpecial €"
    }

    // Implémentation de la méthode afficherConsommation pour les voitures à essence
    override fun afficherConsommation(): String {
        // Consommation entre 4 et 12 L/100km pour une voiture à essence
        val consommation = Random.nextDouble(4.0, 12.0)
        return "La consommation de la voiture à essence est de ${"%.2f".format(consommation)} L/100km."
    }
}

