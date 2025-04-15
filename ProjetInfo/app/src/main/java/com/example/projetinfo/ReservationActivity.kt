package com.example.projetinfo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class ReservationActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val modele = intent.getStringExtra("modele") ?: "N/A"
        val dateDebut = intent.getStringExtra("dateDebut") ?: "N/A"
        val dateFin = intent.getStringExtra("dateFin") ?: "N/A"
        val nombreDeJours = intent.getIntExtra("nombreDeJours", 0)
        val montantTotal = intent.getDoubleExtra("montantTotal", 0.0)

        val reservation = Reservation(
            modele = modele,
            dateDebut = Calendar.getInstance(),
            dateFin = Calendar.getInstance(),
            tarifJournalier = 0.0,
            nombreDeJours = nombreDeJours,
            montantTotal = montantTotal
        )

        findViewById<TextView>(R.id.tvVehicleModel).text = "Modèle : ${reservation.modele}"
        findViewById<TextView>(R.id.tvStartDate).text = "Date début : $dateDebut"
        findViewById<TextView>(R.id.tvEndDate).text = "Date fin : $dateFin"
        findViewById<TextView>(R.id.tvDays).text = "Nombre de jours : ${reservation.nombreDeJours}"
        findViewById<TextView>(R.id.tvTotalAmount).text = "Montant total : ${reservation.montantTotal} €"
        findViewById<TextView>(R.id.tvLocalisation).text = "Localisation : ${Reservation.localisation}"

        findViewById<Button>(R.id.btnPaiement).setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("context", "louer_voiture")
            startActivity(intent)
        }
    }
}
