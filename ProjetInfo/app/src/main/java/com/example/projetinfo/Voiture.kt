package com.example.projetinfo

import java.io.Serializable

open class Voiture(
    val marque: String,
    val modele: String,
    val annee: Int,
    val couleur: String,
    val tarif: Double,
    val type: String
) : Serializable

class VoitureElectrique(
    marque: String,
    modele: String,
    annee: Int,
    couleur: String,
    tarif: Double
) : Voiture(marque, modele, annee, couleur, tarif, "Électrique"), Energie

class VoitureEssence(
    marque: String,
    modele: String,
    annee: Int,
    couleur: String,
    tarif: Double
) : Voiture(marque, modele, annee, couleur, tarif, "Essence"), Energie
