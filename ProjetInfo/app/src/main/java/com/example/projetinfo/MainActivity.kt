package com.example.projetinfo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView

class MainActivity : AppCompatActivity() {

    private var utilisateur: Utilisateur? = null // Stocke l'utilisateur identifié

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Exemple d'utilisateur identifié (à remplacer par les données réelles après identification)
        utilisateur = Utilisateur("Fotso", "Harry", "harry.fotso@example.com", "0123456789")

        val photoView = findViewById<PhotoView>(R.id.campusMapView)
        photoView.maximumScale = 5.0f
        photoView.mediumScale = 3.0f

        // Utilisation de setOnPhotoTapListener pour obtenir des coordonnées normalisées
        photoView.setOnPhotoTapListener { view, x, y ->
            val imageView = view as PhotoView
            val drawable = imageView.drawable ?: return@setOnPhotoTapListener

            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight

            val xPixel = (x * intrinsicWidth).toInt()
            val yPixel = (y * intrinsicHeight).toInt()

            when {
                xPixel in 3180..4500 && yPixel in 4180..5800 -> showDialogMenu("3")
                xPixel in 2000..2500 && yPixel in 4050..4900 -> showDialogMenu("1")
                xPixel in 8400..10200 && yPixel in 3420..4450 -> showDialogMenu("2")
            }
        }

        val btnAfficherUtilisateur = findViewById<ImageButton>(R.id.btnAfficherUtilisateur)
        btnAfficherUtilisateur.setOnClickListener {
            afficherInformationsUtilisateur()
        }
    }

    private fun afficherInformationsUtilisateur() {
        if (utilisateur != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Informations de l'utilisateur")
            builder.setMessage(utilisateur!!.afficherInformations())
            builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
            builder.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Erreur")
            builder.setMessage("Aucun utilisateur identifié.")
            builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }

    private fun showDialogMenu(parkingKey: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Parking $parkingKey Solbosch")

        val vehicleList = VehicleRepository.getVehicles(parkingKey)

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