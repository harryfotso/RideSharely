package com.example.projetinfo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class IdentificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identification)

        val context = intent.getStringExtra("context")

        val etNom = findViewById<EditText>(R.id.etNom)
        val etPrenom = findViewById<EditText>(R.id.etPrenom)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etMotDePasse = findViewById<EditText>(R.id.etMotDePasse)
        val etTelephone = findViewById<EditText>(R.id.etTelephone)
        val btnInscription = findViewById<Button>(R.id.btnInscription)

        btnInscription.setOnClickListener {
            val identification = Identification(
                etNom.text.toString(),
                etPrenom.text.toString(),
                etEmail.text.toString(),
                etMotDePasse.text.toString(),
                etTelephone.text.toString()
            )

            if (identification.verifierDonnees()) {
                val utilisateur = identification.creerUtilisateur()
                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_LONG).show()

                when (context) {
                    "ajout_voiture" -> {
                        val intent = Intent(this, AddVehicleActivity::class.java).apply {
                            putExtra("nom", utilisateur.nom)
                            putExtra("prenom", utilisateur.prenom)
                            putExtra("telephone", utilisateur.telephone)
                            putExtra("email", utilisateur.email)
                        }
                        startActivity(intent)
                        finish()
                    }

                    "louer_voiture" -> {
                        val intent = Intent(this, PaymentActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {
                        Toast.makeText(this, "Contexte inconnu", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_manage -> {
                    val intent = Intent(this, ManageVehiclesActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_home -> true
                else -> false
            }
        }
    }
}



