package com.example.projetinfo

// Identification.kt
class Identification(
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val telephone: String
) {
    fun verifierDonnees(): Boolean {
        return nom.isNotBlank() &&
                prenom.isNotBlank() &&
                email.isNotBlank() &&
                motDePasse.isNotBlank() &&
                telephone.isNotBlank() &&
                emailEstValide() &&
                telephoneEstValide()
    }

    private fun emailEstValide(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }

    private fun telephoneEstValide(): Boolean {
        return telephone.all { it.isDigit() }
    }

    fun creerUtilisateur(): Utilisateur {
        return Utilisateur(nom, prenom, email, telephone)
    }
}

