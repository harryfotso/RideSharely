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


// Classe Utilisateur
class Utilisateur(
    val nom: String,
    val prenom: String,
    val email: String,
    private val motDePasse: String,
    val telephone: String
) {
    fun sInscrire(): Boolean = true
    fun seConnecter(email: String, motDePasse: String): Boolean = (this.email == email && this.motDePasse == motDePasse)
}


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser VehicleRepository
        VehicleRepository.init(this)

        val photoView = findViewById<PhotoView>(R.id.campusMapView)
        photoView.maximumScale = 5.0f
        photoView.mediumScale = 3.0f

        // Utilisation de setOnPhotoTapListener pour obtenir des coordonnées normalisées
        photoView.setOnPhotoTapListener { view, x, y ->
            // x et y sont des coordonnées normalisées (entre 0 et 1)
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
    }

    // Méthode unique pour afficher un dialogue en fonction du numéro de parking
    private fun showDialogMenu(parkingKey: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Parking $parkingKey Solbosch")

        val vehicleList = VehicleRepository.getVehicles(parkingKey)

        if (vehicleList.isEmpty()) {
            builder.setMessage("Aucun véhicule disponible dans ce parking.")
        } else {
            // 🔹 Crée une version simplifiée pour l'affichage dans la boîte de dialogue
            val simplifiedList = vehicleList.map { description ->
                // Extraction des données pour la version simplifiée
                val marque = Regex("Marque: ([^,]+)").find(description)?.groupValues?.get(1) ?: "Inconnu"
                val modele = Regex("Modèle/année: ([^/]+)/").find(description)?.groupValues?.get(1) ?: "?"
                val type = Regex("Type: ([^,]+)").find(description)?.groupValues?.get(1)?.trim()?.lowercase() ?: "Inconnu"

                // Formater la ligne avec marque, modèle et type
                val typeAffichage = when (type) {
                    "voiture électrique" -> "électrique"
                    "voiture à essence" -> "essence"
                    else -> "Inconnu"
                }

                "$marque $modele, $typeAffichage"
            }

            // 🔹 Utilise la liste simplifiée pour l'affichage
            builder.setItems(simplifiedList.toTypedArray()) { _, which ->
                val selectedVehicle = vehicleList[which] // Récupère la description complète

                val intent = Intent(this, DetailsVoitureActivity::class.java)
                intent.putExtra("vehicleDescription", selectedVehicle) // Envoie tous les détails
                startActivity(intent)
            }
        }

        builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
