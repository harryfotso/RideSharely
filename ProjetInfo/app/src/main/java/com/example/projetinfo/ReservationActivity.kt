package com.example.projetinfo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale


class ReservationActivity : AppCompatActivity(), Notifier {

    private val toastNotifier by lazy { ToastNotifier(this)}

    override fun notify(message: String) {
        toastNotifier.notify(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // Récupération de l'objet Reservation depuis l'Intent
        val reservation = intent.getSerializableExtra("reservation") as? Reservation
        if (reservation == null) {
            // Si la réservation est nulle, afficher un message et fermer l'activité
            notify("Erreur: La réservation n'a pas été trouvée")
            finish()
            return
        }

        // Créer un SimpleDateFormat pour formater les dates
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Formatage des dates avant affichage
        val dateDebutFormatted = dateFormat.format(reservation.dateDebut.time)
        val dateFinFormatted = dateFormat.format(reservation.dateFin.time)

        // Affichage des informations de la réservation dans les TextViews
        findViewById<TextView>(R.id.tvVehicleMarque).text = "Modèle : ${reservation.marque}"
        findViewById<TextView>(R.id.tvVehicleModel).text = "Modèle : ${reservation.modele}"
        findViewById<TextView>(R.id.tvStartDate).text = "Date début : $dateDebutFormatted"
        findViewById<TextView>(R.id.tvEndDate).text = "Date fin : $dateFinFormatted"
        findViewById<TextView>(R.id.tvDays).text = "Nombre de jours : ${reservation.nombreDeJours}"
        findViewById<TextView>(R.id.tvTotalAmount).text = "Montant total : ${reservation.montantTotal} €"
        findViewById<TextView>(R.id.tvLocalisation).text = "Localisation : ${Reservation.localisation}"

        // Envoi de la réservation à PaymentActivity lorsqu'on clique sur "Paiement"
        findViewById<Button>(R.id.btnPaiement).setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("reservation", reservation)
            startActivity(intent)
        }
    }
}