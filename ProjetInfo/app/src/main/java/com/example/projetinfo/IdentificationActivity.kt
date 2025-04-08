package com.example.projetinfo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


// Déclaration de la classe IdentificationActivity qui hérite de AppCompatActivity.
// Cela permet à l'activité d'utiliser les fonctionnalités modernes de l'interface utilisateur d'Android.
class IdentificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // onCreate() est une méthode est appelée lorsque l'activité est créée. Elle est utilisée pour initialiser
        // l'interface utilisateur et les variables ainsi que pour définir le comportement de l'activité.
        super.onCreate(savedInstanceState)
        // super.onCreate(savedInstanceState) appelle la méthode onCreate() de la classe parente
        // AppCompatActivity pour initialiser l'activité correctement.
        setContentView(R.layout.activity_identification) // setContentView() définit le layout de l'activité, c'est-à-dire l'interface utilisateur de cette activité

        val context = intent.getStringExtra("context")

        // Récupération des vues du formulaire
        // findViewById est une méthode qui est utilisée pour récupérer des éléments de l'interface utilisateur par leur ID.
        val etNom = findViewById<EditText>(R.id.etNom)
        val etPrenom = findViewById<EditText>(R.id.etPrenom)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etMotDePasse = findViewById<EditText>(R.id.etMotDePasse)
        val etTelephone = findViewById<EditText>(R.id.etTelephone)
        val btnInscription = findViewById<Button>(R.id.btnInscription)

        // setOnClickListener est déclenché lorsqu'on clique sur le bouton btnInscription.
        // À l'intérieur de ce bloc, on récupère les valeurs saisies dans les champs de texte.
        btnInscription.setOnClickListener {
            val nom = etNom.text.toString() // text.toString() permet de récupérer le texte saisi dans chaque champ sous forme de chaîne de caractères.
            val prenom = etPrenom.text.toString()
            val email = etEmail.text.toString()
            val motDePasse = etMotDePasse.text.toString()
            val telephone = etTelephone.text.toString()

            // Vérification que tous les champs sont remplis :
            // Si tous les champs (nom, prénom, email, mot de passe, téléphone) ne sont pas vides, on exécute le bloc de code suivant.
            if (nom.isNotEmpty() && prenom.isNotEmpty() && email.isNotEmpty() && motDePasse.isNotEmpty() && telephone.isNotEmpty()) {
                val utilisateur = Utilisateur(nom, prenom, email, motDePasse, telephone) // Création d'un objet Utilisateur avec les informations saisies par l'utilisateur (nom, prénom, email, mot de passe, téléphone).
                // Un message Toast est affiché pour informer l'utilisateur que l'inscription a réussi.
                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_LONG).show()

                when (context) {
                    "ajout_voiture" -> {
                        val intent = Intent(this, AddVehicleActivity::class.java)
                        intent.putExtra("nom", nom)
                        intent.putExtra("prenom", prenom)
                        intent.putExtra("telephone", telephone)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()  // Ferme l'activité actuelle
                    }

                    "louer_voiture" -> {
                        val intent = Intent(this, PaymentActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {
                        // Si le contexte ne correspond à aucun des cas, on affiche un Toast pour informer l'utilisateur.
                        Toast.makeText(this, "Contexte inconnu", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                // Si un ou plusieurs champs sont vides, un Toast est affiché pour demander à l'utilisateur de remplir tous les champs.
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            }

        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_manage -> {
                    // Ici on peut juste retourner à l'ajout de voiture ou autre action
                    val intent = Intent(this, ManageVehiclesActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_home -> {
                    // Si on est déjà sur IdentificationActivity, on ne fait rien ou on revient en arrière
                    true
                }
                else -> false
            }
        }
    }
}



