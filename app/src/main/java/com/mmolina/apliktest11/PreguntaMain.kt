/*
 * ApliKTest11
 * Copyright (C) 2021 Miguel Molina
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.mmolina.apliktest11

import android.content.Intent
import android.os.Bundle
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.mmolina.apliktest11.Modelo.PreguntaDB
import com.mmolina.apliktest11.database.AppDatabase
import com.mmolina.apliktest11.ui.AdaptadorPregunta
import kotlinx.android.synthetic.main.activity_pregunta_main.*

class PreguntaMain : AppCompatActivity() {
    private lateinit var AutorStr:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutorStr = intent.extras?.get("DATA_Autor_to_pregmain").toString()
        setContentView(R.layout.activity_pregunta_main)
        var ListaPreguntas = emptyList<PreguntaDB>()
        val databasePregunta = AppDatabase.getDataBase(this)
        val btnEFA: ExtendedFloatingActionButton = findViewById(R.id.btnExtdFloatAgregarPregunta)
        // Acceso a la base de datos
        databasePregunta.DBPregunta().getLstPreguntasDB().observe(this, Observer {
            ListaPreguntas = it
            val adaptador = AdaptadorPregunta(this, ListaPreguntas)
            ListaMainAct.adapter = adaptador
        })
        // Vista de la lista de preguntas
        ListaMainAct.setOnItemClickListener { parent, view, position, id ->
            val intentDetallePregunta = Intent(this, DetallePregunta::class.java)
            intentDetallePregunta.putExtra("id", ListaPreguntas[position].idPregunta)
            intentDetallePregunta.putExtra("DATA_Autor_to_detpreg", AutorStr)
            startActivity(intentDetallePregunta)
        }
        // Boton flotante extendido
        btnExtdFloatAgregarPregunta.setOnClickListener {
            val intentNuevaPregunta = Intent(this, NuevaPregunta::class.java)
            intentNuevaPregunta.putExtra("DATA_Autor_to_nuevapreg_v2", AutorStr)
            startActivity(intentNuevaPregunta)
        }
        // Animacion del Boton flotante extendido
        ListaMainAct.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view:AbsListView, scrollState:Int) {}
            override fun onScroll(view:AbsListView, firstVisibleItem:Int, visibleItemCount:Int, totalItemCount:Int ) {
                if (firstVisibleItem == 0) {
                    btnEFA.extend()
                }
                if (firstVisibleItem > 0) {
                    btnEFA.shrink()
                }
            }
        })
    }
}