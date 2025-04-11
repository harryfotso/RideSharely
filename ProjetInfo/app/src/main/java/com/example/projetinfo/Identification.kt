package com.example.projetinfo

// Identification.kt
class Identification(
    private val nom: String,
    private val prenom: String,
    private val email: String,
    private val motDePasse: String,
    private val telephone: String
) {
    fun verifierDonnees(): Boolean {
        return nom.isNotBlank() &&
                prenom.isNotBlank() &&
                email.isNotBlank() &&
                motDePasse.isNotBlank() &&
                telephone.isNotBlank()
    }

    fun creerUtilisateur(): Utilisateur {
        return Utilisateur(nom, prenom, email, motDePasse, telephone)
    }
}
