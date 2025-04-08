package com.example.projetinfo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.widget.ImageView



class DetailsVoitureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_voiture)

        // Récupérer la description de la voiture passée via l'Intent
        val vehicleDescription = intent.getStringExtra("vehicleDescription") ?: ""

        // Variables pour stocker le niveau une seule fois
        var niveauBatterieMemo: Int? = null
        var niveauEssenceMemo: Int? = null

        // Parser la description pour créer un objet Voiture
        val voiture = parseVoiture(vehicleDescription)

        // Affichage des caractéristiques de la voiture
        val marqueText = findViewById<TextView>(R.id.marqueText)
        val modeleText = findViewById<TextView>(R.id.modeleText)
        val anneeText = findViewById<TextView>(R.id.anneeText)
        val couleurText = findViewById<TextView>(R.id.couleurText)
        val tarifText = findViewById<TextView>(R.id.tarifText)
        val typeText = findViewById<TextView>(R.id.typeText)
        val typeIcon = findViewById<ImageView>(R.id.typeIcon)

        val type = when (voiture) {
            is VoitureElectrique -> "Voiture électrique"
            is VoitureEssence -> "Voiture à essence"
            else -> "Inconnu"
        }


        typeText.text = "Type de voiture : $type"
        marqueText.text = "Marque : ${voiture.marque}"
        modeleText.text = "Modèle : ${voiture.modele}"
        anneeText.text = "Année : ${voiture.annee}"
        couleurText.text = "Couleur : ${voiture.couleur}"
        tarifText.text = "Tarif : ${voiture.tarif} €/jour"


        when (voiture) {
            is VoitureElectrique -> {
                typeIcon.setImageResource(R.drawable.battery_icon)
            }
            is VoitureEssence -> {
                typeIcon.setImageResource(R.drawable.essence_icon)
            }
            else -> {
                typeIcon.setImageResource(android.R.drawable.ic_dialog_alert) // par défaut
            }
        }


        // Ajouter un bouton pour afficher le niveau de la batterie/essence
        val btnNiveau = findViewById<Button>(R.id.btnNiveau)

        btnNiveau.setOnClickListener {
            if (voiture is VoitureElectrique) {
                // Génère une seule fois le niveau de batterie
                if (niveauBatterieMemo == null) {
                    niveauBatterieMemo = voiture.niveauBatterie()
                }
                Toast.makeText(this, "Niveau de batterie : $niveauBatterieMemo%", Toast.LENGTH_LONG).show()
            } else if (voiture is VoitureEssence) {
                // Génère une seule fois le niveau d'essence
                if (niveauEssenceMemo == null) {
                    niveauEssenceMemo = voiture.niveauEssence()
                }
                Toast.makeText(this, "Niveau d'essence : $niveauEssenceMemo%", Toast.LENGTH_LONG).show()
            }
        }

        // Ajouter le bouton pour l'identification
        val btnIdentification = findViewById<Button>(R.id.btnIdentification)
        btnIdentification.setOnClickListener {
            // Lancer l'activité d'identification
            val intent = Intent(this, IdentificationActivity::class.java)
            intent.putExtra("context", "louer_voiture")  // Si tu as besoin de passer des informations supplémentaires
            startActivity(intent)
        }
    }

    // Fonction pour parser la description en un objet Voiture
    private fun parseVoiture(description: String): Voiture {
        val marque = Regex("Marque: ([^,]+)").find(description)?.groupValues?.get(1) ?: ""
        val modele = Regex("Modèle/année: ([^/]+)/").find(description)?.groupValues?.get(1) ?: ""
        val annee = Regex("/(\\d{4})").find(description)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val couleur = Regex("Couleur: ([^,]+)").find(description)?.groupValues?.get(1) ?: ""
        val tarif = Regex("Tarif: ([\\d.]+)").find(description)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
        val type = Regex("Type: ([^,]+)").find(description)?.groupValues?.get(1)?.trim()?.lowercase() ?: ""

        return when (type) {
            "voiture électrique" -> VoitureElectrique(marque, modele, annee, couleur, tarif)
            "voiture à essence" -> VoitureEssence(marque, modele, annee, couleur, tarif)
            else -> VoitureEssence(marque, modele, annee, couleur, tarif) // Valeur par défaut
        }
    }
}


