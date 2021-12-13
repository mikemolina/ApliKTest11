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

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mmolina.apliktest11.Modelo.Persona
import com.mmolina.apliktest11.Modelo.PersonaDB
import com.mmolina.apliktest11.database.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    // Variable globales
    private lateinit var editarUsername:EditText
    private lateinit var editarPassword:EditText
    private lateinit var editarNombreOculto:EditText
    private lateinit var editarApellidoOculto:EditText
    private lateinit var editarCorreoOculto:EditText
    // Lista de Usuarios - memoria temporal
    val LstPersonas:MutableList<Persona> = mutableListOf(
        Persona("Miguel", "Molina","mmolina", "1234", "mmolina@domain.com"),
        Persona("Juan", "Acosta", "juanco", "qwer", "juanco@domain.com")
    )
    // Variables base de datos Persona
    private lateinit var databasePersona:AppDatabase
    private lateinit var var_persona:PersonaDB
    private var LstPersonasDB = emptyList<PersonaDB>()

    // Main
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Recoger valores del formulario
        editarUsername = findViewById(R.id._EditarUsername)
        editarPassword = findViewById(R.id._EditarPassword)
        editarNombreOculto = findViewById(R.id._EditarNombreOculto)
        editarApellidoOculto = findViewById(R.id._EditarApellidoOculto)
        editarCorreoOculto = findViewById(R.id._EditarCorreoOculto)
        // Instancias base de datos Persona
        databasePersona = AppDatabase.getDataBase(this)
        databasePersona.DBPersona().getLstPersonaDB().observe(this, {
            LstPersonasDB = it
        })
    }

    // Funcion para mostrar/ocultar la clave
    fun MostrarOcultarPwd(view:android.view.View) {
        if( view.id == R.id._EditarImgOcultarPassword ) {
            if( _EditarPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance()) ) {
                // Mostrar clave
                (view as ImageView).setImageResource(R.drawable.ic_visibility_off)
                _EditarPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                // Ocultar clave
                (view as ImageView).setImageResource(R.drawable.ic_visibility)
                _EditarPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }
    }

    // Funcion inicio de sesion del usuario
    fun InicioSesion(view:android.view.View) {
        var trToastMSJ:String = ""
        // Variables locales
        val loc_editarUsername:String = editarUsername.text.toString()
        val loc_editarPassword:String = editarPassword.text.toString()
        // Mensaje emergente de creacion de sesion
        val trDialogoTitle:String = getResources().getString(R.string.textInicioSesion_DialogoTitle)
        val trDialogoMessage:String = getResources().getString(R.string.textInicioSesion_DialogoMessage)
        val trDialogoOpcPositiva:String = getResources().getString(R.string.textInicioSesion_OpcPositiva)
        val trDialogoOpcNegativa:String = getResources().getString(R.string.textInicioSesion_OpcNegativa)
        val positivoBtn = { dialog:DialogInterface, which:Int->ActivarMSJ() }
        val negativoBtn = { _:DialogInterface, _:Int-> }
        val Dialogo = AlertDialog.Builder(this)
            .setTitle(trDialogoTitle)
            .setMessage(trDialogoMessage)
            .setPositiveButton(trDialogoOpcPositiva, positivoBtn)
            .setNegativeButton(trDialogoOpcNegativa, negativoBtn)
        // Validacion campos vacios
        if( ValidarCampo(loc_editarUsername) && ValidarCampo(loc_editarPassword) ) {
            // Validacion de usuario registrado
            val rta = ValidarUsuarioRegistrado(loc_editarUsername, loc_editarPassword)
            if( rta ) {
                DesactivarMSJ()
                val intentBienvenida = Intent(this, Bienvenida::class.java)
                intentBienvenida.putExtra("DATA_Ususario", var_persona.usuario)
                intentBienvenida.putExtra("DATA_Nombre", var_persona.nombre)
                intentBienvenida.putExtra("DATA_Apellido", var_persona.apellido)
                intentBienvenida.putExtra("DATA_Email", var_persona.correo)
                startActivity(intentBienvenida)
            } else {
                Dialogo.create()
                Dialogo.show()
            }
        } else {
            trToastMSJ = this.getResources().getString(R.string.textInicioSesion_CampoVacio)
            Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
        }
    }

    // Activacion de mensaje emergente
    fun ActivarMSJ() {
        btnCrearUsuario.visibility = View.VISIBLE
        editarNombreOculto.visibility = View.VISIBLE
        editarApellidoOculto.visibility = View.VISIBLE
        editarCorreoOculto.visibility = View.VISIBLE
    }

    // Desactivacion de mensaje emergente
    fun DesactivarMSJ() {
        btnCrearUsuario.visibility = View.GONE
        editarNombreOculto.visibility= View.GONE
        editarApellidoOculto.visibility= View.GONE
        editarCorreoOculto.visibility= View.GONE
        editarUsername.setText("")
        editarPassword.setText("")
        editarNombreOculto.setText("")
        editarApellidoOculto.setText("")
        editarCorreoOculto.setText("")
    }

    // Creacion de usuario
    fun CrearUsuario(view:android.view.View) {
        var trToastMSJ:String = ""
        // Variables locales
        val loc_editarUsername:String = editarUsername.text.toString()
        val loc_editarPassword:String = editarPassword.text.toString()
        val loc_editarNombreOculto:String = editarNombreOculto.text.toString()
        val loc_editarApellidoOculto:String = editarApellidoOculto.text.toString()
        val loc_editarCorreoOculto:String = editarCorreoOculto.text.toString()
        // Validacion campos vacios
        if( ValidarCampo(loc_editarUsername) && ValidarCampo(loc_editarPassword) && ValidarCampo(loc_editarNombreOculto)
            && ValidarCampo(loc_editarApellidoOculto) && ValidarCampo(loc_editarCorreoOculto) ) {
            // Verificacion de existencia de usuario
            val rtaUser = ExisteUsuario(loc_editarUsername)
            if( !rtaUser ) {
                val rtaPwd = ValidarClaveMet1(loc_editarPassword)
                if( rtaPwd ) {
                    val rtaCampoUser = ValidarUsuario(loc_editarUsername)
                    if( rtaCampoUser ) {
                        val rtaEmail = ValidarCorreo(loc_editarCorreoOculto)
                        if( rtaEmail ) {
                            // Crear persona en la base de datos
                            val cifrarpwd:CifradoPWD = CifradoPWD()
                            val claveSalt:ByteArray? = cifrarpwd.getSalt()
                            val claveSegura:String = cifrarpwd.getClaveSegura(loc_editarPassword, claveSalt) as String
                            var_persona = PersonaDB(loc_editarNombreOculto, loc_editarApellidoOculto, loc_editarUsername, claveSalt, claveSegura, loc_editarCorreoOculto)
                            CoroutineScope(Dispatchers.IO).launch {
                                databasePersona.DBPersona().insertPersonaDBAll(var_persona)
                                this@MainActivity.finish()
                            }
                            //LstPersonas.add(Persona(loc_editarNombreOculto, loc_editarApellidoOculto, loc_editarUsername, loc_editarPassword, loc_editarCorreoOculto))
                            trToastMSJ = this.getResources()
                                .getString(R.string.textCrearUsuario_Adicion, loc_editarUsername)
                            Toast.makeText(this, trToastMSJ, Toast.LENGTH_SHORT).show()
                        } else {
                            trToastMSJ = this.getResources().getString(R.string.textCrearUsuario_CorreoIncorrecto)
                            Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        trToastMSJ = this.getResources().getString(R.string.textCrearUsuario_UsuarioDebil)
                        Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
                    }
                } else {
                    trToastMSJ = this.getResources().getString(R.string.textCrearUsuario_ClaveDebil)
                    Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
                }
            } else {
                trToastMSJ = this.getResources()
                    .getString(R.string.textCrearUsuario_Existe, loc_editarUsername)
                Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
            }
        } else {
            trToastMSJ = this.getResources().getString(R.string.textInicioSesion_CampoVacio)
            Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
        }
    }

    // Verificacion de usuario registrado
    fun ValidarUsuarioRegistrado(loc_editarUsername:String, loc_editarPassword:String) : Boolean {
        var rta:Boolean = false
        val cifrarpwd:CifradoPWD = CifradoPWD()
        //for( item in LstPersonas ) {
        for( item in LstPersonasDB ) {
            if( item.usuario == loc_editarUsername ) {
                val claveTest:String = cifrarpwd.getClaveSegura(loc_editarPassword, item.claveSalt) as String
                if( item.claveSHA256 == claveTest ) {
                    var_persona = item
                    rta = true
                }
            }
        }
        return rta
    }

    // Verificaion de existencia de usuario en la creacion de sesion
    fun ExisteUsuario(loc_editarUsername:String) : Boolean {
        var rta:Boolean = false
        //for( item in LstPersonas ) {
        for( item in LstPersonasDB ) {
            if( item.usuario == loc_editarUsername ) {
                rta = true
            }
        }
        return rta
    }

    // Verificacion de clave metodo validacion ASCII
    fun ValidarClaveMet1(loc_editarPassword:String) : Boolean {
        var rta:Boolean = false
        var n:Int = 0
        var contNumeros:Int = 0
        var contMayusculas:Int = 0
        var contMinusculas:Int = 0
        var contCaracteresEsp = 0
        var trToastMSJ:String = ""
        if( loc_editarPassword.length >= 8 ) {
            for( cadena:Char in loc_editarPassword ){
                n = cadena.toByte().toInt()
                if( (n>96) && (n<123) ){
                    contMinusculas++
                } else if( (n>64) && (n<91) ){
                    contMayusculas++
                } else if( (n>47) && (n<58) ){
                    contNumeros++
                } else if( ((n>32) && (n<48)) ||((n>57) && (n<65)) || ((n>90) && (n<97)) ){
                    contCaracteresEsp++
                }
            }
        } else {
            trToastMSJ = this.getResources().getString(R.string.textValidarClave_PwdIncorrecto)
            Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
        }
        if( (contMinusculas>0) && (contMayusculas>0) && (contNumeros>0) && (contCaracteresEsp>0) ) {
            rta = true
        }
        return rta
    }

    // Verificacion de clave metodo validacion con expresiones regulares
    fun ValidarClaveMet2(loc_editarPassword:String) : Boolean {
        val patron:Pattern
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._*+/])(?=\\S+$).{4,}$"
        patron = Pattern.compile(PASSWORD_PATTERN)
        val cmp:Matcher = patron.matcher(loc_editarPassword)
        return cmp.matches()
    }

    // Verificacion nombre de usuario
    fun ValidarUsuario(loc_editarUsername:String) : Boolean {
        var rta:Boolean = false
        val patron:Pattern
        val PATRON_ESPACIOS_BLANCO = ".*\\s+.*"
        patron = Pattern.compile(PATRON_ESPACIOS_BLANCO)
        val cmp:Matcher = patron.matcher(loc_editarUsername)
        if( !cmp.matches() && (loc_editarUsername.length <= 15) ) {
            rta = true
        }
        return rta
    }

    // Verificacion de correo electronico
    fun ValidarCorreo(loc_editarCorreoOculto:String) : Boolean {
        var rta:Boolean = false
        val patron:Pattern
        val PATRON_CORREO = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"
        patron = Pattern.compile(PATRON_CORREO)
        val cmp:Matcher = patron.matcher(loc_editarCorreoOculto)
        if( cmp.matches() ) {
            rta = true
        }
        return rta
    }

    // Verificacion de campos vacios
    fun ValidarCampo(campo:String) : Boolean {
        var rta:Boolean = false
        if( !campo.isEmpty() ) {
            rta = true
        }
        return rta
    }
}
