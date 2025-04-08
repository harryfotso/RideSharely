package com.example.projetinfo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val etNumCarte = findViewById<EditText>(R.id.etNumCarte)
        val etDateExpiration = findViewById<EditText>(R.id.etDateExpiration)
        val etCVV = findViewById<EditText>(R.id.etCVV)
        val btnConfirmerPaiement = findViewById<Button>(R.id.btnConfirmerPaiement)

        btnConfirmerPaiement.setOnClickListener {
            val numCarte = etNumCarte.text.toString()
            val dateExpiration = etDateExpiration.text.toString()
            val cvv = etCVV.text.toString()

            // Validation basique des champs
            if (numCarte.isNotEmpty() && dateExpiration.isNotEmpty() && cvv.isNotEmpty()) {
                Toast.makeText(this, "Paiement effectué avec succès", Toast.LENGTH_LONG).show()

                // Retour à MainActivity (affichage de la carte)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Fermer PaymentActivity

            } else {
                // Afficher un message d'erreur si un champ est vide
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            }
        }
    }
}
