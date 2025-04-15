package com.example.projetinfo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.github.chrisbanes.photoview.PhotoView
import java.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Récupérer l'utilisateur passé de l'IdenticationActivity
        val utilisateur = intent.getSerializableExtra("utilisateur") as Utilisateur?

        // Trouver l'icône et la mettre en place pour afficher les informations
        val btnAfficherUtilisateur = findViewById<ImageButton>(R.id.btnAfficherUtilisateur)
        btnAfficherUtilisateur.setOnClickListener {
            // Afficher un Dialog avec les infos utilisateur
            if (utilisateur != null) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Informations Utilisateur")
                    .setMessage("Nom et Prénom: ${utilisateur.getNomComplet()}\nEmail: ${utilisateur.email}\nTéléphone: ${utilisateur.telephone}")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .create()
                dialog.show()
            }
        }

        // Initialiser VehicleRepository
        VehicleRepository.init(this)

        val photoView = findViewById<PhotoView>(R.id.campusMapView)
        photoView.maximumScale = 5.0f
        photoView.mediumScale = 3.0f

        // Utilisation de setOnPhotoTapListener pour obtenir des coordonnées normalisées
        photoView.setOnPhotoTapListener { view, x, y ->
            val imageView = view as ImageView
            val drawable = imageView.drawable ?: return@setOnPhotoTapListener

            // Dimensions réelles de l'image
            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight

            // Conversion des coordonnées normalisées en pixels
            val xPixel = (x * intrinsicWidth).toInt()
            val yPixel = (y * intrinsicHeight).toInt()

            // Vérification des zones spécifiques pour chaque parking
            when {
                xPixel in 3180..4500 && yPixel in 4180..5800 -> showDialogMenu("3") // Parking 3
                xPixel in 2000..2500 && yPixel in 4050..4900 -> showDialogMenu("1") // Parking 1
                xPixel in 8400..10200 && yPixel in 3420..4450 -> showDialogMenu("2") // Parking 2
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_map -> {
                    // Rester sur la page actuelle
                    true
                }
                R.id.navigation_login -> {
                    val intent = Intent(this, IdentificationActivity::class.java)
                    intent.putExtra("context", "louer_voiture")
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun showDialogMenu(parkingKey: String) {
        val builder = AlertDialog.Builder(this)

        // Initialisation de Parking et StatistiquesParking
        val parking = Parking(parkingKey)
        val statistiquesParking = StatistiquesParking(parking)
        parking.notifierObservateurs() // Met à jour les observateurs immédiatement

        val vehicleList = VehicleRepository.getVehicles(parkingKey)
        val statistiques = statistiquesParking.obtenirStatistiques()

        // Ajout des statistiques dans le titre personnalisé
        val titleView = TextView(this).apply {
            text = "Parking $parkingKey Solbosch\n$statistiques"
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        builder.setCustomTitle(titleView)

        if (vehicleList.isEmpty()) {
            builder.setMessage("Aucun véhicule disponible dans ce parking.")
        } else {
            val simplifiedList = vehicleList.map { description ->
                val marque = Regex("Marque: ([^,]+)").find(description)?.groupValues?.get(1) ?: "Inconnu"
                val modele = Regex("Modèle/année: ([^/]+)/").find(description)?.groupValues?.get(1) ?: "?"
                val type = Regex("Type: ([^,]+)").find(description)?.groupValues?.get(1)?.trim()?.lowercase() ?: "Inconnu"

                val typeAffichage = when (type) {
                    "voiture électrique" -> "électrique"
                    "voiture à essence" -> "essence"
                    else -> "Inconnu"
                }

                "$marque $modele, $typeAffichage"
            }

            builder.setItems(simplifiedList.toTypedArray()) { _, which ->
                val selectedVehicle = vehicleList[which]

                val intent = Intent(this, DetailsVoitureActivity::class.java)
                intent.putExtra("vehicleDescription", selectedVehicle)
                startActivity(intent)
            }
        }

        builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}

