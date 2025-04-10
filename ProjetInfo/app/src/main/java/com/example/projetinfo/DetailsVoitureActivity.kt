package com.example.projetinfo

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*

class DetailsVoitureActivity : AppCompatActivity() {
    private var dateDebut: Calendar? = null
    private var dateFin: Calendar? = null
    private val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var tarifJournalier: Double = 0.0
    private var nombreDeJours: Int = 0
    private var montantTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_voiture)

        // Variables pour stocker le niveau une seule fois
        var niveauBatterieMemo: String? = null
        var niveauEssenceMemo: String? = null

        val descriptionVoiture = intent.getStringExtra("vehicleDescription") ?: ""
        val voiture = parseVoiture(descriptionVoiture)
        tarifJournalier = voiture.tarif
        val modele = voiture.modele

        findViewById<TextView>(R.id.marqueText).text = "Marque : ${voiture.marque}"
        findViewById<TextView>(R.id.modeleText).text = "Modèle : ${voiture.modele}"
        findViewById<TextView>(R.id.anneeText).text = "Année : ${voiture.annee}"
        findViewById<TextView>(R.id.couleurText).text = "Couleur : ${voiture.couleur}"
        findViewById<TextView>(R.id.tarifText).text = "Tarif : ${voiture.tarif} €/jour"

        val typeText = findViewById<TextView>(R.id.typeText)
        val typeIcon = findViewById<ImageView>(R.id.typeIcon)

        val type = when (voiture) {
            is VoitureElectrique -> "Voiture électrique"
            is VoitureEssence -> "Voiture à essence"
            else -> "Inconnu"
        }

        typeText.text = "Type de voiture : $type"

        when (voiture) {
            is VoitureElectrique -> typeIcon.setImageResource(R.drawable.battery_icon)
            is VoitureEssence -> typeIcon.setImageResource(R.drawable.essence_icon)
            else -> typeIcon.setImageResource(android.R.drawable.ic_dialog_alert)
        }

        // Ajouter un bouton pour afficher le niveau de la batterie/essence
        val btnNiveau = findViewById<Button>(R.id.btnNiveau)
        btnNiveau.setOnClickListener {
            if (voiture is VoitureElectrique) {
                if (niveauBatterieMemo == null) {
                    niveauBatterieMemo = voiture.etatEnergie()
                }
                Toast.makeText(this, "Niveau de batterie : $niveauBatterieMemo%", Toast.LENGTH_LONG).show()
            } else if (voiture is VoitureEssence) {
                if (niveauEssenceMemo == null) {
                    niveauEssenceMemo = voiture.etatEnergie()
                }
                Toast.makeText(this, "Niveau d'essence : $niveauEssenceMemo%", Toast.LENGTH_LONG).show()
            }
        }

        val btnDateDebut = findViewById<Button>(R.id.btnSelectStartDate)
        val btnDateFin = findViewById<Button>(R.id.btnSelectEndDate)
        val tvNombreDeJours = findViewById<TextView>(R.id.tvDaysCount)
        val btnFacture = findViewById<Button>(R.id.btnPrintInvoice)

        btnDateDebut.setOnClickListener {
            showDatePicker { date ->
                dateDebut = date
                Toast.makeText(this, "Date début : ${formatDate.format(date.time)}", Toast.LENGTH_SHORT).show()
                updateDaysCount(tvNombreDeJours)
            }
        }

        btnDateFin.setOnClickListener {
            showDatePicker { date ->
                dateFin = date
                Toast.makeText(this, "Date fin : ${formatDate.format(date.time)}", Toast.LENGTH_SHORT).show()
                updateDaysCount(tvNombreDeJours)
            }
        }

        btnFacture.setOnClickListener {
            if (dateDebut == null || dateFin == null) {
                Toast.makeText(this, "Veuillez sélectionner les dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Assurez-vous que les dates sont non nulles avant de créer la réservation
            val reservation = Reservation(
                modele = modele,
                dateDebut = dateDebut!!, // Utilisation de "!!" pour garantir que ce n'est pas null
                dateFin = dateFin!!,
                tarifJournalier = tarifJournalier,
                nombreDeJours = nombreDeJours,
                montantTotal = montantTotal
            )

            // Calculer la durée et le montant total de la réservation
            nombreDeJours = reservation.calculerDuree() // Calcul de la durée
            montantTotal = reservation.calculerMontantTotal() // Calcul du montant total

            // Afficher la durée et le montant total
            tvNombreDeJours.text = "Nombre de jours : $nombreDeJours"

            val intent = Intent(this, ReservationActivity::class.java).apply {
                putExtra("modele", voiture.modele)
                putExtra("dateDebut", formatDate.format(dateDebut!!.time))
                putExtra("dateFin", formatDate.format(dateFin!!.time))
                putExtra("nombreDeJours", nombreDeJours)
                putExtra("montantTotal", montantTotal)
            }
            startActivity(intent)
        }
    }

    private fun showDatePicker(onDateSelected: (Calendar) -> Unit) {
        val calendrier = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, annee, mois, jour ->
                val dateChoisie = Calendar.getInstance()
                dateChoisie.set(annee, mois, jour)
                onDateSelected(dateChoisie)
            },
            calendrier.get(Calendar.YEAR),
            calendrier.get(Calendar.MONTH),
            calendrier.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDaysCount(tv: TextView) {
        if (dateDebut != null && dateFin != null) {
            val diff = dateFin!!.timeInMillis - dateDebut!!.timeInMillis
            val jours = (diff / (1000 * 60 * 60 * 24)).toInt()
            tv.text = "Nombre de jours : $jours"
        }
    }

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
            else -> VoitureEssence(marque, modele, annee, couleur, tarif)
        }
    }
}

