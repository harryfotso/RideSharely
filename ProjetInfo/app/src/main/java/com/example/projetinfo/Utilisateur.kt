package com.example.projetinfo

class Utilisateur(
    val nom: String,
    val prenom: String,
    val email: String,
    val telephone: String
) {
    fun afficherInformations(): String {
        return """
            Nom : $nom
            Prénom : $prenom
            Email : $email
            Téléphone : $telephone
        """.trimIndent()
    }
}