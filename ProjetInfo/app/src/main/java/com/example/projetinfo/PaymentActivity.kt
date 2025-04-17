package com.example.projetinfo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast



class PaymentActivity : AppCompatActivity(), Notifier {

    private val toastNotifier by lazy { ToastNotifier(this)}

    private var payment: Payment? = null

    override fun notify(message: String) {
        toastNotifier.notify(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        // Récupération de l'objet Reservation depuis l'Intent
        val reservation = intent.getSerializableExtra("reservation") as? Reservation

        // Vérifie si l'objet Reservation est nul
        if (reservation == null) {
            notify("Erreur: La réservation n'a pas été trouvée")
            finish() // Fermer l'activité si réservation est nulle
            return
        }

        val etNumCarte = findViewById<EditText>(R.id.etNumCarte)
        val etDateExpiration = findViewById<EditText>(R.id.etDateExpiration)
        val etCVV = findViewById<EditText>(R.id.etCVV)
        val btnConfirmerPaiement = findViewById<Button>(R.id.btnConfirmerPaiement)
        val btnAfficherMontant = findViewById<Button>(R.id.btnAfficherMontant)


        btnConfirmerPaiement.setOnClickListener {
            val numCarte = etNumCarte.text.toString()
            val dateExpiration = etDateExpiration.text.toString()
            val cvv = etCVV.text.toString()

            // Créer un objet Payment avec les données saisies
            val payment = Payment(numCarte, dateExpiration, cvv, reservation)

            // Validation des données de paiement
            if (payment.verifierNumCarte() && payment.verifierDateExpiration() && payment.verifierCvv()) {
                // Si tout est valide
                notify("Paiement effectué avec succès")

                // Retour à MainActivity (affichage de la carte)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Fermer PaymentActivity
            } else {
                // Si les données sont invalides
                notify("Veuillez remplir tous les champs correctement")
            }
        }

        // Affiche le montant total de la réservation
        btnAfficherMontant.setOnClickListener {
            val numCarte = etNumCarte.text.toString()
            val dateExpiration = etDateExpiration.text.toString()
            val cvv = etCVV.text.toString()

            // Créer un objet Payment avec les données saisies
            val payment = Payment(numCarte, dateExpiration, cvv, reservation)

            notify(payment.afficherMontant())
        }
    }
}




