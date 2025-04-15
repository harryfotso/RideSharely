package com.example.projetinfo

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
                telephone.isNotBlank() &&
                Identification.emailEstValide(email) &&  // Utilisation de la méthode statique pour l'email
                Identification.telephoneEstValide(telephone)  // Utilisation de la méthode statique pour le téléphone
    }

    fun creerUtilisateur(): Utilisateur {
        return Utilisateur(nom, prenom, email, telephone)
    }

    // Méthode statique pour valider un email indépendamment d'une instance
    companion object {
        fun emailEstValide(email: String): Boolean {
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            return emailRegex.matches(email)
        }

        fun telephoneEstValide(telephone: String): Boolean {
            return telephone.all { it.isDigit() }
        }
    }
}

