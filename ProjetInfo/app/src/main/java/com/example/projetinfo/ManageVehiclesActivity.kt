package com.example.projetinfo

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent

class ManageVehiclesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var vehicleList: MutableList<Pair<String, String>> // Pair<vehicleDescription, parkingKey>
    private lateinit var adapter: VehicleAdapter // Utilisation du VehicleAdapter personnalisé

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_vehicles)

        VehicleRepository.init(this)

        listView = findViewById(R.id.listViewVehicles)
        loadAllVehicles()

        // Remplacement de ArrayAdapter par VehicleAdapter
        adapter = VehicleAdapter(
            this,
            vehicleList // On passe la liste des véhicules directement
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedVehicle = vehicleList[position]

            AlertDialog.Builder(this)
                .setTitle("Supprimer le véhicule")
                .setMessage("Voulez-vous vraiment supprimer ce véhicule :\n\n${selectedVehicle.first} ?")
                .setPositiveButton("Supprimer") { _, _ ->
                    VehicleRepository.removeVehicle(selectedVehicle.first, selectedVehicle.second)
                    vehicleList.removeAt(position)
                    refreshList()
                    Toast.makeText(this, "Véhicule supprimé", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Annuler", null)
                .show()
        }

        // Configuration du BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Action pour l'item "Ajouter un véhicule"
                    // Lancer l'activité AddVehicleActivity
                    val intent = Intent(this, AddVehicleActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_manage -> {
                    // Action pour un autre item (si tu en as)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadAllVehicles() {
        vehicleList = mutableListOf()
        for (i in 1..3) {
            val parkingKey = i.toString()
            val vehicles = VehicleRepository.getVehicles(parkingKey)
            vehicles.forEach { vehicle ->
                vehicleList.add(Pair(vehicle, parkingKey)) // Ajoute la paire (description, parkingKey)
            }
        }
    }

    private fun refreshList() {
        adapter.clear()
        adapter.addAll(vehicleList) // Ajouter la liste complète d'objets Pair<String, String>
        adapter.notifyDataSetChanged()
    }
}
