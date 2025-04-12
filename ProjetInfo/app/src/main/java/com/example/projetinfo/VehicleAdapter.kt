package com.example.projetinfo // Remplace par ton propre package

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.projetinfo.R

class VehicleAdapter(
    context: Context,
    private val vehicles: List<Pair<String, String>> // Liste de véhicules
) : ArrayAdapter<Pair<String, String>>(context, android.R.layout.simple_list_item_1, vehicles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val vehicle = getItem(position) // On récupère l'élément à la position donnée
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = vehicle?.first // Affichage de la description du véhicule
        return view
    }
}
