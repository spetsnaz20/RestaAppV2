package com.example.restaapp.database

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CompletableDeferred


class AddFactura {

    val db = FirebaseFirestore.getInstance()

    fun tratarCuenta(
        data: Map<String, Any>?,
        cuentaTotal: Double,
        context: Context,
    ){
        try {
            val firebaseData = data?.toMutableMap() ?: mutableMapOf()
            firebaseData["cuentaTotal"] = cuentaTotal

            val db = FirebaseFirestore.getInstance()
            val facturaRef = db.collection("elcruce").document("Facturas").collection("Factura")
            facturaRef.get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val lastDocument = querySnapshot.documents[querySnapshot.size() - 1]
                        val nextDocumentName = String.format("%06d", (lastDocument.id.toInt() + 1))
                        facturaRef.document(nextDocumentName).set(firebaseData)
                            .addOnSuccessListener {
                                Toast.makeText(context,"Documento creado con éxito.",Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(context,"Error al crear documento: $exception",Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Handle the case where the collection is empty
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error al obtener documentos: $exception")
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}