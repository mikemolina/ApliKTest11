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

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mmolina.apliktest11.Modelo.PreguntaDB
import com.mmolina.apliktest11.database.AppDatabase
import kotlinx.android.synthetic.main.activity_detalle_pregunta.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetallePregunta : AppCompatActivity() {
    private lateinit var databasePregunta:AppDatabase
    private lateinit var var_pregunta:PreguntaDB
    private lateinit var LstPreguntaLiveData:LiveData<PreguntaDB>
    private lateinit var AutorStr:String
    // Lista etiquetas materia
    private val LstTagMateria:List<String> = listOf("CNAT", "LENG", "MAT", "SOC")
    // Lista de asignaturas idioma EN
    private val asignaturaLstEN:List<String> = listOf("Science", "Language", "Mathematics", "Socials")
    // Lista de asignaturas idioma ES
    private val asignaturaLstES:List<String> = listOf("Ciencias", "Lenguaje", "Matemáticas", "Sociales")
    // Lista de asignaturas idioma PT
    private val asignaturaLstPT:List<String> = listOf("Ciência", "Linguagem", "Matemática", "Sociais")

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        AutorStr = intent.extras?.get("DATA_Autor_to_detpreg").toString()
        setContentView(R.layout.activity_detalle_pregunta)
        setSupportActionBar(findViewById(R.id.MenuBarDetallesPregunta))
        // Iniciar instancia de la base de datos
        databasePregunta = AppDatabase.getDataBase(this)
        val idPregunta = intent.getIntExtra("id", 0)
        LstPreguntaLiveData = databasePregunta.DBPregunta().getPreguntaDB(idPregunta)
        // Etiquetas procedentes de activity_detalle_pregunta.xml
        LstPreguntaLiveData.observe(this, Observer {
            var_pregunta = it
            setDetallePreguntaAsignatura.text = Tag2AsignaturaStrTr(var_pregunta.asignatura)
            imgDetallePreguntaAsignatura.setImageResource(var_pregunta.imgAsignatura)
            setDetallePreguntaEnunciado.text = var_pregunta.enunciado
            setDetallePreguntaOpcItemA.text = var_pregunta.opcItemA
            setDetallePreguntaOpcItemB.text = var_pregunta.opcItemB
            setDetallePreguntaOpcItemC.text = var_pregunta.opcItemC
            setDetallePreguntaOpcItemD.text = var_pregunta.opcItemD
            setDetallePreguntaAutor.text = var_pregunta.autor
        })
    }

    // Inflar menu de opciones
    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu:Menu?) : Boolean {
        menuInflater.inflate(R.menu.menu_pregunta, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    // Opciones del menu
    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val positivoBtn = {
                dialogo: DialogInterface, which:Int -> eliminarPregunta()
        }
        val negativoBtn = { _: DialogInterface, _:Int -> }
        var trDiagTit:String = getString(R.string.txtDetallePreguntaDiagTit)
        var trDiagMsj:String = getString(R.string.txtDetallePreguntaDiagMsj)
        var trDiagPos:String = getString(R.string.txtDetallePreguntaDiagPos)
        var trDiagNeg:String = getString(R.string.txtDetallePreguntaDiagNeg)
        val dialogo = AlertDialog.Builder(this)
            .setTitle(trDiagTit)
            .setMessage(trDiagMsj)
            .setPositiveButton(trDiagPos, positivoBtn)
            .setNegativeButton(trDiagNeg, negativoBtn)
        when( item.itemId ) {
            R.id.menuItemEditar -> {
                val intentNuevaPregunta = Intent(this, NuevaPregunta::class.java)
                intentNuevaPregunta.putExtra("ObjPregunta", var_pregunta)
                intentNuevaPregunta.putExtra("DATA_Autor_to_nuevapreg_v1", AutorStr)
                startActivity(intentNuevaPregunta)
            }
            R.id.menuItemBorrar -> {
                dialogo.create()
                dialogo.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Funcion para eliminar pregunta
    fun eliminarPregunta() {
        LstPreguntaLiveData.removeObservers(this)
        CoroutineScope(Dispatchers.IO).launch {
            databasePregunta.DBPregunta().deletePreguntaDB(var_pregunta)
            this@DetallePregunta.finish()
        }
        var trToastMSJ:String = this.getResources().getString(R.string.txtDetallePreguntaEliminada)
        Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
    }

    // Funcion para convertir etiqueta asignatura a cadena traducida segun idioma local
    fun Tag2AsignaturaStrTr(materia:String):String {
        var asignaturaStrTr = ""
        val LocaleActual:String = ConfigurationCompat.getLocales(resources.configuration)[0].toString()
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
