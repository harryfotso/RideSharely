package com.example.projetinfo

interface Observable {
    val observateurs: MutableList<Observer>

    fun ajouter(observateur: Observer) {
        observateurs.add(observateur)
    }

    fun notifier() {
        observateurs.forEach { it.mettreAJour() }
    }
}