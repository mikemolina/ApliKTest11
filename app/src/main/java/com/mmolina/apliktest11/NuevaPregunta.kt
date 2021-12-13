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

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mmolina.apliktest11.Modelo.PreguntaDB
import com.mmolina.apliktest11.database.AppDatabase
import kotlinx.android.synthetic.main.activity_nueva_pregunta.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevaPregunta : AppCompatActivity() {
    private lateinit var AutorStr:String
    // Lista etiquetas asignatura
    private val LstTagMateria:List<String> = listOf("CNAT", "LENG", "MAT", "SOC")
    // Lista de asignaturas idioma EN
    private val asignaturaLstEN:List<String> = listOf("Science", "Language", "Mathematics", "Socials")
    // Lista de asignaturas idioma ES
    private val asignaturaLstES:List<String> = listOf("Ciencias", "Lenguaje", "Matemáticas", "Sociales")
    // Lista de asignaturas idioma PT
    private val asignaturaLstPT:List<String> = listOf("Ciência", "Linguagem", "Matemática", "Sociais")
    // Lista etiquetas respuestas
    private val LstRespuestas:List<String> = listOf("A", "B", "C", "D")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tmp1:String = intent.extras?.get("DATA_Autor_to_nuevapreg_v1").toString()
        if( tmp1 != null ) {
            AutorStr = tmp1
        }
        val tmp2:String = intent.extras?.get("DATA_Autor_to_nuevapreg_v2").toString()
        if( tmp2 != null ) {
            AutorStr = tmp2
        }
        setContentView(R.layout.activity_nueva_pregunta)
        // RadioGrupo asignatura
        val radioGrupoAsignatura: RadioGroup = findViewById(R.id.GrupoAsignatura)
        // RadioGrupo respuesta
        val radioGrupoRespuesta: RadioGroup = findViewById(R.id.GrupoRespuesta)
        // Id selecionado en RadioGrupo
        var selectId:Int = 0
        // Instancia de la base de datos
        val databasePregunta = AppDatabase.getDataBase(this)
        // Lectura/Edicion/Eliminacion de pregunta
        // Etiquetas procedentes de activity_nueva_pregunta.xml
        var idPregunta:Int? = null
        if( intent.hasExtra("ObjPregunta") ) {
            val var_pregunta = intent.extras?.getSerializable("ObjPregunta") as PreguntaDB
            idPregunta = var_pregunta.idPregunta
            selectId = LstTagMateria.indexOf(var_pregunta.asignatura)
            radioGrupoAsignatura.check(radioGrupoAsignatura.getChildAt(selectId).id)
            editNuevaPreguntaEnunciado.setText(var_pregunta.enunciado)
            editNuevaPreguntaOpcionA.setText(var_pregunta.opcItemA)
            editNuevaPreguntaOpcionB.setText(var_pregunta.opcItemB)
            editNuevaPreguntaOpcionC.setText(var_pregunta.opcItemC)
            editNuevaPreguntaOpcionD.setText(var_pregunta.opcItemD)
            selectId = LstRespuestas.indexOf(var_pregunta.respuesta)
            radioGrupoRespuesta.check(radioGrupoRespuesta.getChildAt(selectId).id)
            AutorStr = var_pregunta.autor
        }
        // Crear/Guardar pregunta
        // Etiquetas procedentes de activity_nueva_pregunta.xml
        btnNuevaPreguntaAgregar.setOnClickListener {
            selectId = radioGrupoAsignatura.checkedRadioButtonId
            val radioAsignatura: RadioButton = findViewById(selectId)
            val asignatura = AsignaturaStrTr2Tag(radioAsignatura.text.toString())
            val enunciado = editNuevaPreguntaEnunciado.text.toString()
            val opcItemA = editNuevaPreguntaOpcionA.text.toString()
            val opcItemB = editNuevaPreguntaOpcionB.text.toString()
            val opcItemC = editNuevaPreguntaOpcionC.text.toString()
            val opcItemD = editNuevaPreguntaOpcionD.text.toString()
            selectId = radioGrupoRespuesta.checkedRadioButtonId
            val radioRespuesta: RadioButton = findViewById(selectId)
            val respuesta = radioRespuesta.text.toString()
            val autor = AutorStr
            Log.i("autor (nueva pregunta)", AutorStr)
            val imgAsignatura = SetImgAsignatura(asignatura)
            val var_pregunta = PreguntaDB(asignatura, enunciado, opcItemA, opcItemB, opcItemC, opcItemD, respuesta, autor, imgAsignatura)
            // Edicion pregunta
            if( idPregunta != null ) {
                CoroutineScope(Dispatchers.IO).launch {
                    var_pregunta.idPregunta = idPregunta
                    databasePregunta.DBPregunta().updatePreguntaDB(var_pregunta)
                    this@NuevaPregunta.finish()
                }
                var trToastMSJ:String = this.getResources().getString(R.string.txtbtnNuevaPreguntaActualizada)
                Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
            }
            // Crear pregunta
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    databasePregunta.DBPregunta().insertPreguntaDBAll(var_pregunta)
                    this@NuevaPregunta.finish()
                }
                var trToastMSJ:String = this.getResources().getString(R.string.txtbtnNuevaPreguntaAdicionada)
                Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Funcion para convertir cadena asignatura a etiqueta asignatura
    private fun AsignaturaStrTr2Tag(materia:String):String {
        var tagMateria:String = ""
        val tagEN:String = auxAsignaturaStrTr2Tag(materia, asignaturaLstEN)
        if( !tagEN.isEmpty() ) {
            tagMateria = tagEN
        }
        val tagES:String = auxAsignaturaStrTr2Tag(materia, asignaturaLstES)
        if( !tagES.isEmpty() ) {
            tagMateria = tagES
        }
        val tagPT:String = auxAsignaturaStrTr2Tag(materia, asignaturaLstPT)
        if( !tagPT.isEmpty() ) {
            tagMateria = tagPT
        }
        return tagMateria
    }

    // Funcion auxiliar para convertir cadena asignatura a etiqueta asignatura
    private fun auxAsignaturaStrTr2Tag(materia:String, asignaturaLstXX:List<String>):String {
        var etiqueta:String = ""
        for( (indice, item) in asignaturaLstXX.withIndex() ) {
            if( materia.equals(item) ) {
                etiqueta = LstTagMateria[indice]
            }
        }
        return etiqueta
    }

    // Funcion para poner imagen segun asignatura de la pregunta
    fun SetImgAsignatura(asignatura:String):Int {
        var imagen:Int = 0
        if( asignatura.equals("CNAT") ) {
            imagen = R.drawable.ic_icon_cnat_256
        }
        if( asignatura.equals("LENG") ) {
            imagen = R.drawable.ic_icon_leng_256
        }
        if( asignatura.equals("MAT") ) {
            imagen = R.drawable.ic_icon_mat_256
        }
        if( asignatura.equals("SOC") ) {
            imagen = R.drawable.ic_icon_soc_256
        }
        return imagen
    }
}