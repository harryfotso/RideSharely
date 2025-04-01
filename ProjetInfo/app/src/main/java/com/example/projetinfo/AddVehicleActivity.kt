package com.example.projetinfo

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddVehicleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        VehicleRepository.init(this)

        val spinnerParking = findViewById<Spinner>(R.id.spinnerParking)
        val marqueInput = findViewById<EditText>(R.id.marqueInput)
        val modeleInput = findViewById<EditText>(R.id.modeleInput)
        val anneeInput = findViewById<EditText>(R.id.anneeInput)
        val couleurInput = findViewById<EditText>(R.id.couleurInput)
        val tarifInput = findViewById<EditText>(R.id.tarifInput)
        val btnAjouter = findViewById<Button>(R.id.btnAjouter)

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
            val parkingNumber = (spinnerParking.selectedItemPosition + 1).toString() // "1", "2" ou "3"

            if (marque.isEmpty() || modele.isEmpty() || annee == null || couleur.isEmpty() || tarif == null || annee <= 0 || tarif <= 0) {
                Toast.makeText(this, "Veuillez remplir tous les champs correctement", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val vehicleDescription = "$marque $modele, Tarif: $tarif €/jour"

            // 🔥 Ajout via VehicleRepository au lieu de SharedPreferences manuel
            VehicleRepository.addVehicle(vehicleDescription, parkingNumber)

            Toast.makeText(this, "Véhicule ajouté dans Parking $parkingNumber", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun saveVehicleToSharedPreferences(parkingKey: String, vehicle: String) {
        val sharedPreferences = getSharedPreferences("ParkingData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val vehicleList1 = sharedPreferences.getStringSet(parkingKey, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        vehicleList1.add(vehicle)

        editor.putStringSet(parkingKey, vehicleList1)
        val vehicleList2 = sharedPreferences.getStringSet(parkingKey, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        vehicleList2.add(vehicle)

        editor.putStringSet(parkingKey, vehicleList2)
        val vehicleList3 = sharedPreferences.getStringSet(parkingKey, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        vehicleList3.add(vehicle)

        editor.putStringSet(parkingKey, vehicleList3)
        editor.apply()
    }
}