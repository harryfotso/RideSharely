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

class DetailsVoitureActivity : AppCompatActivity(), Notifier {

    private val toastNotifier by lazy { ToastNotifier(this)}

    override fun notify(message: String) {
        toastNotifier.notify(message)
    }

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
        var consommation: String? = null

        val voiture = intent.getSerializableExtra("selectedVehicle") as? Voiture

        if (voiture == null) {
            notify("Erreur : voiture introuvable")
            finish()
            return
        }

        tarifJournalier = voiture.tarif

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

        val btnReduction = findViewById<Button>(R.id.btnReduction)
        val btnConsommation = findViewById<Button>(R.id.btnConsommation)

        // Gestion du bouton "Appliquer la réduction"
        btnReduction.setOnClickListener {
            if (voiture is VoitureElectrique) {
                val message = voiture.appliquerReduction()  // Appel à la méthode de la sous-classe VoitureElectrique
                notify(message)
            } else if (voiture is VoitureEssence) {
                val message = voiture.appliquerReduction()  // Appel à la méthode de la sous-classe VoitureEssence
                notify(message)
            }
        }

        // Initialisation de la consommation
        if (consommation == null) {
            consommation = voiture.afficherConsommation()
        }

        btnConsommation.setOnClickListener {
            // Afficher la consommation pré-calculée
            notify(consommation)
        }

        // Ajouter un bouton pour afficher le niveau de la batterie/essence
        val btnNiveau = findViewById<Button>(R.id.btnNiveau)

        btnNiveau.setOnClickListener {
            if (voiture is VoitureElectrique) {
                if (niveauBatterieMemo == null) {
                    niveauBatterieMemo = voiture.etatEnergie()
                }
                notify("Niveau de batterie : $niveauBatterieMemo%")
            } else if (voiture is VoitureEssence) {
                if (niveauEssenceMemo == null) {
                    niveauEssenceMemo = voiture.etatEnergie()
                }
                notify("Niveau d'essence : $niveauEssenceMemo%")
            }
        }

        val btnDateDebut = findViewById<Button>(R.id.btnSelectStartDate)
        val btnDateFin = findViewById<Button>(R.id.btnSelectEndDate)
        val tvNombreDeJours = findViewById<TextView>(R.id.tvDaysCount)
        val btnFacture = findViewById<Button>(R.id.btnPrintInvoice)

        btnDateDebut.setOnClickListener {
            showDatePicker { date ->
                dateDebut = date
                notify("Date début : ${formatDate.format(date.time)}")
                updateDaysCount(tvNombreDeJours)
            }
        }

        btnDateFin.setOnClickListener {
            showDatePicker { date ->
                dateFin = date
                notify("Date fin : ${formatDate.format(date.time)}")
                updateDaysCount(tvNombreDeJours)
            }
        }

        btnFacture.setOnClickListener {
            if (dateDebut == null || dateFin == null) {
                notify("Veuillez sélectionner les dates")
                return@setOnClickListener
            }


            val reservation = Reservation(
                marque = voiture.marque,
                modele = voiture.modele,
                dateDebut = dateDebut!!,
                dateFin = dateFin!!,
                tarifJournalier = tarifJournalier,
                nombreDeJours = 0,
                montantTotal = 0.0
            )

            // Maintenant qu'on a un objet reservation, on peut calculer les valeurs
            nombreDeJours = reservation.calculerDuree()
            montantTotal = reservation.calculerMontantTotal()

            // Mettre à jour les champs dans reservation
            reservation.nombreDeJours = nombreDeJours
            reservation.montantTotal = montantTotal

            // Afficher la durée et le montant total
            tvNombreDeJours.text = "Nombre de jours : $nombreDeJours"

            val intent = Intent(this, ReservationActivity::class.java).apply {
                putExtra("reservation", reservation)
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
}

