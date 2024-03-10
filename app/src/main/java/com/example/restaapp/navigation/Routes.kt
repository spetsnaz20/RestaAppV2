package com.example.restaapp.navigation

sealed class Routes(val route:String) {
    object InicioSesion:Routes("InicioSesion")
    object Mesas:Routes("Mesas")
    object Mesa:Routes("Mesa")
}