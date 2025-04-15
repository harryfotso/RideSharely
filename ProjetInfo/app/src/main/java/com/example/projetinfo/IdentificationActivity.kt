package com.example.projetinfo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


class IdentificationActivity : AppCompatActivity(), Notifier {

    private val toastNotifier by lazy { ToastNotifier(this)}

    override fun notify(message: String) {
        toastNotifier.notify(message)
    }

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
                notify("Inscription réussie")

                when (context) {
                    "ajout_voiture" -> {
                        val intent = Intent(this, AddVehicleActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    "louer_voiture" -> {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("utilisateur", utilisateur)
                        }
                        startActivity(intent)
                        finish()
                    }

                    else -> {
                        notify("Contexte inconnu")
                    }
                }
            } else {
                notify("Veuillez remplir tous les champs correctement")
            }
        }
    }
}


