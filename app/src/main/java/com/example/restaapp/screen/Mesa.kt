package com.example.restaapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaapp.database.GetBebidas
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaapp.database.GetRacciones
import com.example.restaapp.database.Productos
import com.example.restaapp.objetos.Producto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun Mesa(mesaId: String?){
    Cuerpo(mesaId)
}

@Composable
fun Cuerpo(mesaId: String?){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Column (modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Cuenta(mesaId)
        }
        val showBebidas = remember { mutableStateOf(false) }
        val showRacciones = remember { mutableStateOf(false) }
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
            ) {
            Button(onClick = {
                showBebidas.value = true
                showRacciones.value = false
            },colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                Text(text = "Bebidas", fontSize = 30.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                showRacciones.value = true
                showBebidas.value = false
            },colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                Text(text = "Racciones", fontSize = 30.sp, color = Color.Black)
            }

        }
        Spacer(modifier =Modifier.height(10.dp))
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            if(showBebidas.value){
                Bebidas(mesaId)
            }else if (showRacciones.value){
                Racciones(mesaId)
            }
        }
    }
}
@Composable
fun Cuenta(mesaId: String?){
    var cuentaTotal by remember { mutableStateOf(0.0) }

    Text(text = "Mesa ${mesaId}", textAlign = TextAlign.Center, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(10.dp))
    var data by remember { mutableStateOf<Map<String, Any>?>(null) }
    val db = Firebase.firestore
    val docRef = db.collection("elcruce").document("Mesas").collection("Mesa${mesaId}").document("Cuenta")
    DisposableEffect(Unit) {
            val listenerRegistration = docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Error al escuchar cambios: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    data = snapshot.data?.filterValues { it is Map<*, *> }
                    cuentaTotal = 0.0
                    data?.values?.forEach {product ->
                        val prodyctDetails = product as? Map<String, Any>
                        val precio = prodyctDetails?.get("precio") as? Double ?: 0.0
                        cuentaTotal += precio
                    }
                } else {
                    println("Datos actuales: null")
                }
            }
            onDispose {
                listenerRegistration.remove()
            }
    }
    if(data != null){
        Spacer(modifier =Modifier.height(15.dp))
        DisplayCuenta(data,mesaId, cuentaTotal)
    }
}

@Composable
fun Bebidas(mesaId: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        var data by remember { mutableStateOf<Map<String, Any>?>(null) }
        var bebidas = GetBebidas()
        Text(text = "Bebidas",textAlign = TextAlign.Center, fontSize = 25.sp, modifier = Modifier.fillMaxWidth())
        LaunchedEffect(Unit){
            GlobalScope.launch {
                data = bebidas.recuperarBedias()
            }
        }
        if(data != null){
            DisplayData(data,mesaId)
        }
    }

}

@Composable
fun Racciones(mesaId: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        var data by remember { mutableStateOf<Map<String, Any>?>(null) }
        var racciones = GetRacciones()
        Text(text = "Racciones",textAlign = TextAlign.Center, fontSize = 25.sp, modifier = Modifier.fillMaxWidth())
        LaunchedEffect(Unit){
            GlobalScope.launch {
                data = racciones.recuperarRacciones()
            }
        }
        if(data != null){
            DisplayData(data,mesaId)
        }
    }
}
@Composable
fun DisplayData(data: Map<String, Any>?,mesaId: String?) {
    var context = LocalContext.current
    val dataList = data?.entries?.toList()?.chunked(2) ?: emptyList()

    LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly){
        items(dataList) { rowItems ->
            Row(modifier = Modifier.fillMaxSize(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                for (entry in rowItems) {
                    val key = entry.key
                    val value = entry.value
                    if (value is Map<*, *>) {
                        Button(onClick = {
                            val precioDouble = value.values.first() as Double
                            val producto = Producto(1, key, precioDouble)
                            val productos = Productos()
                            productos.TratarProducto(producto, mesaId.toString(), context)
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black), modifier = Modifier
                            .width(140.dp)
                            .height(80.dp)
                            .padding(0.dp, 0.dp, 20.dp, 0.dp)) {
                            Text(text = "$key", textAlign = TextAlign.Center)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun DisplayCuenta(data: Map<String, Any>?, mesaId: String?, cuentaTotal: Double){
    Column(modifier = Modifier
        .fillMaxWidth()
        ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(data?.entries?.toList() ?: emptyList()) {entry ->
                val key = entry.key
                val value = entry.value
                if (value is Map<*,*>){
                    val cantidad = value["cantidad"] as? Long ?: 0L
                    val precio = value["precio"] as? Double ?: 0.0
                    val precioUnidad = value["precio unidad"] as? Double ?: 0.0
                    Row(modifier =  Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier.width(130.dp),text = "$key", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        Text(modifier = Modifier.width(70.dp),text = "${precio}€", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        Text(modifier = Modifier.width(55.dp),text = "$cantidad", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        Button(onClick = {
                            val producto = Producto(1, key, precioUnidad)
                            val productos = Productos()
                            productos.BorrarProducto(producto.nombre, producto.precio,mesaId.toString())

                        },colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                            Text(modifier = Modifier.width(25.dp), text = "-", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        }
                    }

                }
                Spacer(modifier =Modifier.height(15.dp))
            }
            item {
                Column {
                    Text(text = "Total: ${cuentaTotal}€", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                        Text(text = "Pagar", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}