package com.example.projetinfo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddVehicleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        // Initialiser le repository de véhicules
        VehicleRepository.init(this)

        val spinnerParking = findViewById<Spinner>(R.id.spinnerParking)
        val marqueInput = findViewById<EditText>(R.id.marqueInput)
        val modeleInput = findViewById<EditText>(R.id.modeleInput)
        val anneeInput = findViewById<EditText>(R.id.anneeInput)
        val couleurInput = findViewById<EditText>(R.id.couleurInput)
        val tarifInput = findViewById<EditText>(R.id.tarifInput)
        val btnAjouter = findViewById<Button>(R.id.btnAjouter)

        // Configuration du Spinner pour le choix du parking
        val parkingOptions = listOf("Parking 1", "Parking 2", "Parking 3")
        val parkingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, parkingOptions)
        parkingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerParking.adapter = parkingAdapter

        btnAjouter.setOnClickListener {
            val marque = marqueInput.text.toString()
            val modele = modeleInput.text.toString()
            val annee = anneeInput.text.toString().toIntOrNull()
            val couleur = couleurInput.text.toString()
            val tarif = tarifInput.text.toString().toDoubleOrNull()
            // Récupération de la clé de parking ("1", "2" ou "3")
            val parkingNumber = (spinnerParking.selectedItemPosition + 1).toString()

            if (marque.isEmpty() || modele.isEmpty() || annee == null || couleur.isEmpty() || tarif == null || annee <= 0 || tarif <= 0) {
                Toast.makeText(this, "Veuillez remplir tous les champs correctement", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val vehicleDescription = "$marque $modele, Tarif: $tarif €/jour"

            // Ajout du véhicule dans le repository
            VehicleRepository.addVehicle(vehicleDescription, parkingNumber)

            Toast.makeText(this, "Véhicule ajouté dans Parking $parkingNumber", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configuration du BottomNavigationView pour le loueur
        // Ce menu doit être défini dans le layout activity_add_vehicle.xml et contenir les items avec les identifiants "navigation_manage" et "navigation_home"
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_manage -> {
                    // Lancer l'activité de gestion des véhicules pour le parking sélectionné
                    val parkingNumber = (spinnerParking.selectedItemPosition + 1).toString()
                    val intent = Intent(this, ManageVehiclesActivity::class.java)
                    intent.putExtra("parkingKey", parkingNumber)
                    startActivity(intent)
                    true
                }
                R.id.navigation_home -> {
                    // Rester sur l'activité d'ajout
                    true
                }
                else -> false
            }
        }
    }
}
