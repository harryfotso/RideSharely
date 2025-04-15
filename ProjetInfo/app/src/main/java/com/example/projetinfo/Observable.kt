package com.example.projetinfo

interface Observable {
    fun ajouterObservateur(observateur: Observer)
    fun supprimerObservateur(observateur: Observer)
    fun notifierObservateurs()
}