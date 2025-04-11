package com.example.projetinfo

import kotlin.random.Random

interface Energie {
    fun etatEnergie(): String {
        val niveau = Random.nextInt(30, 101) // Génère un niveau entre 30 et 100
        return "$niveau"
    }
}