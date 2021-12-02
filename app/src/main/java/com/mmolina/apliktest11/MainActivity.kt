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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    // Variable globales
    private lateinit var editarUsername:EditText
    private lateinit var editarPassword:EditText
    private lateinit var editarNombreOculto:EditText
    private lateinit var editarApellidoOculto:EditText
    private lateinit var editarCorreoOculto:EditText
    // Lista de Usuarios
    val LstPersonas:MutableList<Persona> = mutableListOf(
        Persona("Miguel", "Molina","ymmolina", "1234", "ymmolina@gmail.com"),
        Persona("Juan", "Acosta", "cuancho", "qwer", "cuancho3245@gmail.com")
    )
    // Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Recoger valores del formulario
        editarUsername = findViewById(R.id._EditarUsername)
        editarPassword = findViewById(R.id._EditarPassword)
        editarNombreOculto = findViewById(R.id._EditarNombreOculto)
        editarApellidoOculto = findViewById(R.id._EditarApellidoOculto)
        editarCorreoOculto = findViewById(R.id._EditarCorreoOculto)
    }
    // Funcion para mostrar/ocultar la clave
    fun MostrarOcultarPwd(view: android.view.View) {
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
    fun InicioSesion(view: android.view.View) {
        // Variables locales
        var loc_editarUsername:String = editarUsername!!.text.toString()
        var loc_editarPassword:String = editarPassword!!.text.toString()
        var loc_dditarNombreOculto:String = editarNombreOculto!!.text.toString()
        var loc_editarApellidoOculto:String = editarApellidoOculto!!.text.toString()
        var loc_editarCorreoOculto:String = editarCorreoOculto!!.text.toString()
        // Mensaje emergente de creacion de sesion
        var trDialogoTitle:String = getResources().getString(R.string.textInicioSesion_DialogoTitle)
        var trDialogoMessage:String = getResources().getString(R.string.textInicioSesion_DialogoMessage)
        var trDialogoOpcPositiva:String = getResources().getString(R.string.textInicioSesion_OpcPositiva)
        var trDialogoOpcNegativa:String = getResources().getString(R.string.textInicioSesion_OpcNegativa)
        val positivoBtn = { dialog:DialogInterface, which:Int->ActivarMSJ() }
        val negativoBtn = { _:DialogInterface, _:Int-> }
        val Dialogo = AlertDialog.Builder(this)
            .setTitle(trDialogoTitle)
            .setMessage(trDialogoMessage)
            .setPositiveButton(trDialogoOpcPositiva, positivoBtn)
            .setNegativeButton(trDialogoOpcNegativa, negativoBtn)
        // Validacion de usuario registrado
        var rta:Boolean = ValidacionUsuario(loc_editarUsername, loc_editarPassword)
        if( rta == true ) {
            DesactivarMSJ()
            val intentBienvenida = Intent(this, Bienvenida::class.java)
            intentBienvenida.putExtra("SALUDO", loc_editarUsername)
            startActivity(intentBienvenida)
        } else {
            Dialogo.create()
            Dialogo.show()
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
    // Verificacion de usuario registrado
    fun ValidacionUsuario(loc_editarUsername:String, loc_editarPassword:String):Boolean {
        var rta:Boolean = false
        for(item in LstPersonas) {
            if( (item.usuario == loc_editarUsername) && (item.clave == loc_editarPassword) ) {
                rta = true
            }
        }
        return rta
    }
    // Creacion de usuario
    fun CrearUsuario(view: android.view.View) {
        // Variables locales
        var loc_editarUsername:String = editarUsername!!.text.toString()
        var loc_editarPassword:String = editarPassword!!.text.toString()
        var loc_editarNombreOculto:String = editarNombreOculto!!.text.toString()
        var loc_editarApellidoOculto:String = editarApellidoOculto!!.text.toString()
        var loc_editarCorreoOculto:String = editarCorreoOculto!!.text.toString()
        // Verificacion de existencia de usuario
        var rtaUser:Boolean = ExisteUsuario(loc_editarUsername)
        var trToastMSJ:String = ""
        if( !rtaUser ) {
            var rtaPwd = ValidarClaveMet1(loc_editarPassword)
            if( rtaPwd == true ) {
                LstPersonas.add(Persona(loc_editarNombreOculto, loc_editarApellidoOculto, loc_editarUsername, loc_editarPassword, loc_editarCorreoOculto))
                trToastMSJ = this.getResources().getString(R.string.textCrearUsuario_Adicion, loc_editarUsername)
                Toast.makeText(this, trToastMSJ, Toast.LENGTH_SHORT).show()
            } else {
                trToastMSJ = this.getResources().getString(R.string.textCrearUsuario_ClaveDebil)
                Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
            }
        } else {
            trToastMSJ = this.getResources().getString(R.string.textCrearUsuario_Existe, loc_editarUsername)
            Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
        }
    }
    // Verificaion de existencia de usuario en la creacion de sesion
    fun ExisteUsuario(loc_editarUsername:String):Boolean {
        var rta:Boolean = false
        for( item in LstPersonas ) {
            if( item.usuario == loc_editarUsername ) {
                rta = true
            }
        }
        return rta
    }
    // Verificacion de clave metodo validacion ASCII
    fun ValidarClaveMet1(loc_editarPassword:String):Boolean {
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
    fun ValidarClaveMet2(loc_editarPassword:String):Boolean {
        var patron:Pattern
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._*+/])(?=\\S+$).{4,}$";
        patron = Pattern.compile(PASSWORD_PATTERN);
        var cmp:Matcher = patron.matcher(loc_editarPassword);
        return cmp.matches();
    }
}
