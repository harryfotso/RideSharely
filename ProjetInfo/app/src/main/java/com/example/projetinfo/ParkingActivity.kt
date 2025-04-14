package com.example.projetinfo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ParkingActivity : AppCompatActivity() {
    private lateinit var parking: Parking
    private lateinit var statistiquesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_vehicles)

        // Initialisation du parking avec une clé spécifique
        parking = Parking("1")
        statistiquesTextView = findViewById(R.id.statistiquesParking)

        // Initialisation de l'observateur
        val statistiquesParking = object : StatistiquesParking(parking) {
            override fun mettreAJour() {
                runOnUiThread {
                    statistiquesTextView.text = "Statistiques : ${parking.obtenirNombreDeReservations()} réservations"
                }
            }
        }

        // Enregistrer l'observateur
        parking.ajouterObservateur(statistiquesParking)

        // Notifier les observateurs au démarrage
        parking.notifierObservateurs()
    }
}