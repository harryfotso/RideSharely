package com.example.projetinfo

import android.os.Bundle
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



// Classe Voiture
open class Voiture(
    val marque: String,
    val modele: String,
    val annee: Int,
    val type: String, // ici "Standard" par défaut
    val couleur: String,
    val tarifJournalier: Double,
    var disponibilite: Boolean
) {
    fun verifierDisponibilite(): Boolean = disponibilite
    fun calculerCoutLocation(nbJours: Int): Double = nbJours * tarifJournalier
    fun mettreAJourDisponibilite(etat: Boolean) {
        disponibilite = etat
    }
}

// Sous-classes de Voiture
class VoitureElectrique(
    marque: String, modele: String, annee: Int, typeDeVoiture: String, couleur: String, tarifJournalier: Double,
    disponibilite: Boolean, val capaciteBatterie: Int, val tempsRecharge: Double, val autonomie: Int
) : Voiture(marque, modele, annee, typeDeVoiture, couleur, tarifJournalier, disponibilite) {
    fun recharger() {}
    fun afficherEtatBatterie() {}
}

class VoitureHybride(
    marque: String, modele: String, annee: Int, typeDeVoiture: String, couleur: String, tarifJournalier: Double,
    disponibilite: Boolean, val capaciteBatterie: Int, val capaciteReservoirCarburant: Int,
    val consommationCarburant: Double, var pourcentageBatterie: Int, val tempsRecharge: Double
) : Voiture(marque, modele, annee, typeDeVoiture, couleur, tarifJournalier, disponibilite) {
    fun recharger() {}
    fun faireLePlein() {}
    fun passerEnModeElectrique() {}
    fun passerEnModeEssence() {}
    fun afficherEtatBatterie() {}
    fun verifierNiveauCarburant() {}
}

class VoitureEssence(
    marque: String, modele: String, annee: Int, typeDeVoiture: String, couleur: String, tarifJournalier: Double,
    disponibilite: Boolean, val capaciteReservoirCarburant: Int, val consommationCarburant: Double
) : Voiture(marque, modele, annee, typeDeVoiture, couleur, tarifJournalier, disponibilite) {
    fun faireLePlein() {}
    fun verifierNiveauCarburant() {}
}

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

// Classe Reservation
class Reservation(
    val dateDebut: Date,
    val dateFin: Date,
    val etat: String,
    val vehicule: Voiture,
    val client: Utilisateur
) {
    val prixTotal: Double = vehicule.calculerCoutLocation(calculerDuree())

    fun confirmerReservation(): Boolean = true
    fun annulerReservation(): Boolean = true
    fun calculerDuree(): Int = ((dateFin.time - dateDebut.time) / (1000 * 60 * 60 * 24)).toInt()
    fun calculerPrixTotal(): Double = prixTotal
}

// Classe Paiement
class Paiement(
    val montantTotal: Double,
    val methode: String,
    val statut: String
) {
    fun confirmerPaiement(): Boolean = true
    fun annulerPaiement(): Boolean = true
}

// Classe ServiceLocation
class ServiceLocation {
    fun chercherVehicule(emplacement: Map<String, String>): List<Voiture> = listOf()
    fun creerReservation(utilisateur: Utilisateur, vehicule: Voiture, dateDebut: Date, dateFin: Date): Reservation {
        return Reservation(dateDebut, dateFin, "Confirmée", vehicule, utilisateur)
    }
    fun genererFacture(reservation: Reservation): Paiement {
        return Paiement(reservation.prixTotal, "Carte Bancaire", "Payé")
    }
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

            // Affichage des coordonnées pour vérification
            Toast.makeText(this, "Coordonnées: $xPixel, $yPixel", Toast.LENGTH_SHORT).show()

            // Vérification de la zone spécifique (en pixels)
            if (xPixel in 3180..4500 && yPixel in 4180..5800) {
                showDialogMenuP3()
            }
            else if (xPixel in 2000..2500 && yPixel in 4050..4900){
                showDialogMenuP1()
            }
            else if (xPixel in 8400..10200 && yPixel in 3420..4450){
                showDialogMenuP2()
            }
        }

        // Configuration de la barre de navigation inférieure
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_login -> {
                    // Rediriger vers la page d'identification
                    //Toast.makeText(this, "Redirection vers l'identification", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, IdentificationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_map -> {
                    // Retour à la carte
                    //Toast.makeText(this, "Retour à la carte", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }


    private fun showDialogMenuP1() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Parking 1 Solbosch")

        // Récupère la liste des véhicules pour le parking 1
        val vehicleList1 = VehicleRepository.getVehicles("1")


        if (vehicleList1.isEmpty()) {
            builder.setMessage("Aucun véhicule disponible dans ce parking.")
        } else {
            builder.setItems(vehicleList1.toTypedArray()) { _, which ->
                val selectedVehicle = vehicleList1[which]
                Toast.makeText(this, "Vous devez d'abord vous identifier avant de pouvoir louer $selectedVehicle", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun showDialogMenuP2() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Parking 2 Solbosch")

        // Récupère la liste des véhicules pour le parking 1
        val vehicleList2 = VehicleRepository.getVehicles("1")

        if (vehicleList2.isEmpty()) {
            builder.setMessage("Aucun véhicule disponible dans ce parking.")
        } else {
            builder.setItems(vehicleList2.toTypedArray()) { _, which ->
                val selectedVehicle = vehicleList2[which]
                Toast.makeText(this, "Vous devez d'abord vous identifier avant de pouvoir louer $selectedVehicle", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun showDialogMenuP3() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Parking 3 Solbosch")

        // Récupère la liste des véhicules pour le parking 1
        val vehicleList3 = VehicleRepository.getVehicles("1")

        if (vehicleList3.isEmpty()) {
            builder.setMessage("Aucun véhicule disponible dans ce parking.")
        } else {
            builder.setItems(vehicleList3.toTypedArray()) { _, which ->
                val selectedVehicle = vehicleList3[which]
                Toast.makeText(this, "Vous devez d'abord vous identifier avant de pouvoir louer $selectedVehicle", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setPositiveButton("Fermer") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
