package com.example.projetinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class VehicleAdapter(
    context: Context,
    vehicles: List<Pair<String, String>> // Liste de véhicules
) : ArrayAdapter<Pair<String, String>>(context, android.R.layout.simple_list_item_1, vehicles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val vehicle = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = vehicle?.first // Affichage de la description du véhicule
        return view
    }
}