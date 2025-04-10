package com.example.projetinfo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale


class ReservationActivity : AppCompatActivity() {

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

        findViewById<Button>(R.id.btnIdentification).setOnClickListener {
            val intent = Intent(this, IdentificationActivity::class.java)
            intent.putExtra("context", "louer_voiture")
            startActivity(intent)
        }
    }
}
