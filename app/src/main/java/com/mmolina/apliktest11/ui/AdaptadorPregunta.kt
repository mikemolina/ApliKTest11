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

package com.mmolina.apliktest11.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.ConfigurationCompat
import com.mmolina.apliktest11.Modelo.PreguntaDB
import com.mmolina.apliktest11.R
import kotlinx.android.synthetic.main.item_pregunta.view.*

class AdaptadorPregunta(private val nContext: Context,
                        private val LstPreguntas:List<PreguntaDB>):ArrayAdapter<PreguntaDB>(nContext,0, LstPreguntas) {
    // Lista etiquetas materia
    private val LstTagMateria:List<String> = listOf("CNAT", "LENG", "MAT", "SOC")
    // Lista de asignaturas idioma EN
    private val asignaturaLstEN:List<String> = listOf("Science", "Language", "Mathematics", "Socials")
    // Lista de asignaturas idioma ES
    private val asignaturaLstES:List<String> = listOf("Ciencias", "Lenguaje", "Matemáticas", "Sociales")
    // Lista de asignaturas idioma PT
    private val asignaturaLstPT:List<String> = listOf("Ciência", "Linguagem", "Matemática", "Sociais")

    override fun getView(position:Int, convertView: View?, parent: ViewGroup) : View {
        val layout = LayoutInflater.from(nContext).inflate(R.layout.item_pregunta, parent, false)
        val var_pregunta = LstPreguntas[position]
        // Etiquetas procedentes de layout/item_pregunta.xml
        layout.txtItemAsignatura.text = Tag2AsignaturaStrTr(var_pregunta.asignatura)
        layout.txtItemEnunciado.text = var_pregunta.enunciado
        layout.txtItemAutor.text = var_pregunta.autor
        layout.imgItemAsignatura.setImageResource(var_pregunta.imgAsignatura)
        return layout
    }

    // Funcion para convertir etiqueta asignatura a cadena traducida segun idioma local
    private fun Tag2AsignaturaStrTr(materia:String):String {
        var asignaturaStrTr = ""
        val LocaleActual:String = ConfigurationCompat.getLocales(context.resources.configuration)[0].toString()
        val idioma:String = Regex("_").split(LocaleActual)[0].uppercase()
        if( idioma.equals("EN") ) {
            asignaturaStrTr = auxTag2AsignaturaStrTr(materia, asignaturaLstEN)
        }
        if( idioma.equals("ES") ) {
            asignaturaStrTr = auxTag2AsignaturaStrTr(materia, asignaturaLstES)
        }
        if( idioma.equals("PT") ) {
            asignaturaStrTr = auxTag2AsignaturaStrTr(materia, asignaturaLstPT)
        }
        return asignaturaStrTr
    }

    // Funcion auxiliar para convertir etiqueta asignatura a cadena traducida segun idioma local
    private fun auxTag2AsignaturaStrTr(materia:String, asignaturaLstXX:List<String>):String {
        var cadenaTr:String = ""
        for( (indice, item) in LstTagMateria.withIndex() ) {
            if( materia.equals(item) ) {
                cadenaTr = asignaturaLstXX[indice]
            }
        }
        return cadenaTr
    }
}
