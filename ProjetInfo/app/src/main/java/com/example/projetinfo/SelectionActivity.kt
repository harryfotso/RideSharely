package com.example.projetinfo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        val btnLocataire = findViewById<Button>(R.id.btnLocataire)
        val btnLoueur = findViewById<Button>(R.id.btnLoueur)

        btnLocataire.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Envoie l'intent avec le contexte "ajout_voiture"
        btnLoueur.setOnClickListener {
            val intent = Intent(this, IdentificationActivity::class.java)
            intent.putExtra("context", "ajout_voiture")  // Assurez-vous que vous passez le bon contexte
            startActivity(intent)
        }

    }
}

