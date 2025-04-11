package com.example.projetinfo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddVehicleActivity : AppCompatActivity(), Notifier {

    private val toastNotifier by lazy { ToastNotifier(this) }

    override fun notify(message: String) {
        toastNotifier.notify(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        // Initialisation du repository
        VehicleRepository.init(this)

        // Récupération des vues
        val spinnerParking = findViewById<Spinner>(R.id.spinnerParking)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val marqueInput = findViewById<EditText>(R.id.marqueInput)
        val modeleInput = findViewById<EditText>(R.id.modeleInput)
        val anneeInput = findViewById<EditText>(R.id.anneeInput)
        val couleurInput = findViewById<EditText>(R.id.couleurInput)
        val tarifInput = findViewById<EditText>(R.id.tarifInput)
        val btnAjouter = findViewById<Button>(R.id.btnAjouter)

        // Configuration des spinners
        val parkingOptions = listOf("Parking 1", "Parking 2", "Parking 3")
        spinnerParking.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, parkingOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val typeOptions = listOf("Voiture à essence", "Voiture électrique")
        spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Action sur le bouton Ajouter
        btnAjouter.setOnClickListener {
            val marque = marqueInput.text.toString().trim()
            val modele = modeleInput.text.toString().trim()
            val annee = anneeInput.text.toString().toIntOrNull()
            val couleur = couleurInput.text.toString().trim()
            val tarif = tarifInput.text.toString().toDoubleOrNull()
            val parkingNumber = (spinnerParking.selectedItemPosition + 1).toString()
            val selectedType = spinnerType.selectedItem.toString()

            if (marque.isEmpty() || modele.isEmpty() || annee == null || couleur.isEmpty() || tarif == null || annee <= 0 || tarif <= 0) {
                notify("Veuillez remplir tous les champs correctement")
                return@setOnClickListener
            }

            val vehicleDescription = "Marque: $marque, Modèle/année: $modele/$annee, Couleur: $couleur, Tarif: $tarif €/jour, Type: $selectedType"
            VehicleRepository.addVehicle(vehicleDescription, parkingNumber)
            notify("Véhicule ajouté dans Parking $parkingNumber")
            finish()
        }

        // Navigation inférieure
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_manage -> {
                    val parkingNumber = (spinnerParking.selectedItemPosition + 1).toString()
                    startActivity(Intent(this, ManageVehiclesActivity::class.java).apply {
                        putExtra("parkingKey", parkingNumber)
                    })
                    true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(this, IdentificationActivity::class.java).apply {
                        putExtra("context", "ajout_voiture")
                    })
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}