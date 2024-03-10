package com.example.restaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.restaapp.screen.InicioSesion
import androidx.navigation.compose.composable
import com.example.restaapp.screen.Mesa
import com.example.restaapp.screen.Mesas


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.InicioSesion.route){
        composable(route = Routes.InicioSesion.route){ InicioSesion(navController = navController)}
        composable(route = Routes.Mesas.route){ Mesas(navController = navController)}
        composable(route = "mesa/{mesaId}") { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getString("mesaId")
            Mesa(mesaId = mesaId)
        }
    }
}