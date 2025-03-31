package com.example.projetinfo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class IdentificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identification)

        // Récupération des vues du formulaire
        val etNom = findViewById<EditText>(R.id.etNom)
        val etPrenom = findViewById<EditText>(R.id.etPrenom)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etMotDePasse = findViewById<EditText>(R.id.etMotDePasse)
        val etTelephone = findViewById<EditText>(R.id.etTelephone)
        val btnInscription = findViewById<Button>(R.id.btnInscription)

        btnInscription.setOnClickListener {
            val nom = etNom.text.toString()
            val prenom = etPrenom.text.toString()
            val email = etEmail.text.toString()
            val motDePasse = etMotDePasse.text.toString()
            val telephone = etTelephone.text.toString()

            if (nom.isNotEmpty() && prenom.isNotEmpty() && email.isNotEmpty() && motDePasse.isNotEmpty() && telephone.isNotEmpty()) {
                val utilisateur = Utilisateur(nom, prenom, email, motDePasse, telephone)
                // Logique d'inscription (enregistrement, vérification, etc.)
                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        // Configuration de la barre de navigation inférieure
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_login -> {
                    // Déjà sur la page d'identification
                    true
                }
                R.id.navigation_map -> {
                    // Retourner à la carte en terminant l'activité d'identification
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
