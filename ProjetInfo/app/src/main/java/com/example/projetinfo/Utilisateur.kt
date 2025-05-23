package com.example.projetinfo

import java.io.Serializable

class Utilisateur(
    val nom: String,
    val prenom: String,
    val email: String,
    val telephone: String
) : Serializable {

    // Méthode pour afficher le nom complet
    fun getNomComplet(): String {
        return "$prenom $nom"
    }
}
