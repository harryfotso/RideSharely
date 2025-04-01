package com.example.projetinfo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddVehicleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        val marqueInput = findViewById<EditText>(R.id.marqueInput)
        val modeleInput = findViewById<EditText>(R.id.modeleInput)
        val anneeInput = findViewById<EditText>(R.id.anneeInput)
        val couleurInput = findViewById<EditText>(R.id.couleurInput)
        val tarifInput = findViewById<EditText>(R.id.tarifInput)
        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)
        val btnAjouter = findViewById<Button>(R.id.btnAjouter)

        btnAjouter.setOnClickListener {
            val marque = marqueInput.text.toString()
            val modele = modeleInput.text.toString()
            val annee = anneeInput.text.toString().toIntOrNull() ?: 0
            val couleur = couleurInput.text.toString()
            val tarif = tarifInput.text.toString().toDoubleOrNull() ?: 0.0
            val type = typeSpinner.selectedItem.toString()

            if (marque.isNotEmpty() && modele.isNotEmpty() && annee > 0 && couleur.isNotEmpty() && tarif > 0) {
                val newVehicle = Voiture(marque, modele, annee, type, couleur, tarif, true)
                Toast.makeText(this, "Véhicule ajouté : ${newVehicle.marque} ${newVehicle.modele}", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
