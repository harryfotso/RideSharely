package com.example.projetinfo

import java.io.Serializable

class Utilisateur(
    private val nom: String,
    private val prenom: String,
    val email: String,
    val telephone: String
) : Serializable {

    // Méthode pour afficher le nom complet
    fun getNomComplet(): String {
        return "$prenom $nom"
    }
}
