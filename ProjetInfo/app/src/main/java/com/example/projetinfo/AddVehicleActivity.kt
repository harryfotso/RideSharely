package com.example.projetinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddVehicleActivity : AppCompatActivity(), Notifier {

    private val toastNotifier by lazy { ToastNotifier(this)}

    override fun notify(message: String) {
        toastNotifier.notify(message)
    }

    // Méthode appelée lorsque l'activité est créée
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)  // Définit le layout pour cette activité

        // Initialisation du repository pour les véhicules
        VehicleRepository.init(this)

        // Liaison des éléments de l'interface utilisateur à des variables Kotlin
        val spinnerParking = findViewById<Spinner>(R.id.spinnerParking)
        val marqueInput = findViewById<EditText>(R.id.marqueInput)
        val modeleInput = findViewById<EditText>(R.id.modeleInput)
        val anneeInput = findViewById<EditText>(R.id.anneeInput)
        val couleurInput = findViewById<EditText>(R.id.couleurInput)
        val tarifInput = findViewById<EditText>(R.id.tarifInput)
        val btnAjouter = findViewById<Button>(R.id.btnAjouter)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)

        // Définition des options du spinner pour choisir le parking
        val parkingOptions = listOf("Parking 1", "Parking 2", "Parking 3")
        val parkingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, parkingOptions)
        parkingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerParking.adapter = parkingAdapter  // Affectation de l'adaptateur au spinner permettant d'afficher les parkings dans l'interface

        // Définition des options du spinner pour choisir le type de voiture
        val typeOptions = listOf("Voiture à essence", "Voiture électrique")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOptions)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = typeAdapter


        // Configuration du clic sur le bouton "Ajouter"
        btnAjouter.setOnClickListener { //Quand l'utilisateur clique sur ce bouton, l'action suivante est exécutée
            // Récupération des valeurs saisies dans les champs
            val marque = marqueInput.text.toString()
            val modele = modeleInput.text.toString()
            val annee = anneeInput.text.toString().toIntOrNull()  // Conversion en entier, ou null si invalide
            val couleur = couleurInput.text.toString()
            val tarif = tarifInput.text.toString().toDoubleOrNull()  // Conversion en double, ou null si invalide
            val parkingNumber = (spinnerParking.selectedItemPosition + 1).toString() // "1", "2" ou "3"
            val selectedType = spinnerType.selectedItem.toString()


            // Vérification si tous les champs sont remplis correctement
            if (marque.isEmpty() || modele.isEmpty() || annee == null || couleur.isEmpty() || tarif == null || annee <= 0 || tarif <= 0) {
                notify("Veuillez remplir tous les champs correctement")
                //Toast.LENGTH_SHORT détermine la durée de l'affichage du message
                //Toast est une classe dans Android qui permet d'afficher un message court et temporaire à l'utilisateur sous forme de pop-up
                return@setOnClickListener  // Sortie de la fonction si un champ est invalide
            }

            val vehicleDescription = "Marque: $marque, Modèle/année: $modele/$annee, Couleur: $couleur, Tarif: $tarif €/jour, Type: $selectedType" // Description du véhicule ajouté avec une chaîne de caractères


            // Ajoute dans le bon parking
            VehicleRepository.addVehicle(vehicleDescription, parkingNumber)

            // Affichage d'un message de confirmation
            notify("Véhicule ajouté dans Parking $parkingNumber")

            //Termine l'activité actuelle, fermant la page où l'utilisateur ajoute un véhicule et donc l''utilisateur est renvoyé à l'activité précédente après l'ajout du véhicule
            finish()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_manage -> {
                    val intent = Intent(this, ManageVehiclesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}

