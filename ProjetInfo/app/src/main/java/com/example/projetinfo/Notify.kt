package com.example.projetinfo

import android.content.Context
import android.widget.Toast

// Définition de l'interface Notifier
interface Notifier {
    fun notify(message: String)
}

// Implémentation de l'interface Notifier avec Toast
class ToastNotifier(private val context: Context) : Notifier {
    override fun notify(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}