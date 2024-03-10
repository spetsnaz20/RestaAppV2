package com.example.restaapp.database

import android.widget.Toast
import com.example.restaapp.objetos.Producto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Productos {

    val db = FirebaseFirestore.getInstance()

    fun TratarProducto(producto: Producto,mesa: String,context: android.content.Context){
        try {
            InsertarProducto(producto,mesa,context)
        }
        catch (e: Exception){
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun InsertarProducto(producto: Producto, mesa: String,context: android.content.Context){
        try {
            val docRef = db.collection("elcruce").document("Mesas").collection("Mesa${mesa}").document("Cuenta")
            docRef.get()
                .addOnSuccessListener { document ->
                    if(document != null){
                        val productMap = document.data?.get(producto.nombre) as? Map<String, Any>
                        if (productMap != null){
                            val existingCantidad = productMap["cantidad"] as Long
                            val existingPrecio = productMap["precio"] as Double
                            val existingPrecioUnidad = productMap["precio unidad"] as Double

                            val newCantidad = existingCantidad + producto.cantidad
                            val newPrecio = existingPrecio + producto.precio
                            val productoMap = mapOf(
                                producto.nombre to mapOf(
                                    "cantidad" to newCantidad,
                                    "precio" to newPrecio,
                                    "precio unidad" to existingPrecioUnidad
                                )
                            )

                            db.collection("elcruce").document("Mesas").collection("Mesa${mesa}").document("Cuenta").update(productoMap)
                        }
                        else{
                            val productoMap = mapOf(
                                producto.nombre to mapOf(
                                    "cantidad" to producto.cantidad,
                                    "precio" to producto.precio,
                                    "precio unidad" to producto.precio
                                )
                            )
                            db.collection("elcruce").document("Mesas").collection("Mesa${mesa}").document("Cuenta").set(productoMap, SetOptions.merge())
                        }
                    }
                }

        }catch (e: Exception){
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }

    fun BorrarProducto(nombre: String,precio: Double, mesa: String){
        val docRef = db.collection("elcruce").document("Mesas").collection("Mesa${mesa}").document("Cuenta")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    val productMap = document.data?.get(nombre) as? Map<String, Any>
                    if(productMap != null){
                        if(productMap["cantidad"] as Long > 1){
                            val existingCantidad = productMap["cantidad"] as Long
                            val existingPrecio = productMap["precio"] as Double
                            val existingPrecioUnidad = productMap["precio unidad"] as Double

                            val newCantidad = existingCantidad - 1
                            val newPrecio = existingPrecio - precio
                            val productoMap = mapOf(
                                nombre to mapOf(
                                    "cantidad" to newCantidad,
                                    "precio" to newPrecio,
                                    "precio unidad" to existingPrecioUnidad
                                )
                            )
                            db.collection("elcruce").document("Mesas").collection("Mesa${mesa}").document("Cuenta").update(productoMap)
                        }
                        else{
                            val delete = hashMapOf<String, Any>(
                                nombre to FieldValue.delete()
                            )
                            docRef.update(delete)
                        }
                    }
                }
            }
    }
}