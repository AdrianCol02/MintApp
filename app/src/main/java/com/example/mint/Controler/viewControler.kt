package com.example.mint.Controler

import android.app.Application
import android.content.Context
import android.content.Intent

class viewControler(context: Context): Application() {
    private var context : Context = context

    fun cambiarVentana(contexto: Context, clase: Class<*>){
        val ventanaNueva = Intent(contexto, clase)

        contexto.startActivity(ventanaNueva)
    }
}