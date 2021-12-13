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
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import kotlinx.android.synthetic.main.activity_bienvenida.*
import java.text.SimpleDateFormat

class Bienvenida : AppCompatActivity() {
    private lateinit var InfoUsuarioStr:String
    private lateinit var InfoNombreStr:String
    private lateinit var InfoApellidoStr:String
    private lateinit var InfoEmailStr:String

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
        InfoSesion()
        MostrarBienvenida()
        RedondearImagenUsuario()
        setSupportActionBar(findViewById(R.id.MenuBarMain))
    }

    // Funcion con informacion de la sesion
    fun InfoSesion() {
        InfoUsuarioStr = intent.extras?.get("DATA_Ususario").toString()
        InfoNombreStr = intent.extras?.get("DATA_Nombre").toString()
        InfoApellidoStr = intent.extras?.get("DATA_Apellido").toString()
        InfoEmailStr = intent.extras?.get("DATA_Email").toString()
    }

    // Funcion para presentar Bienvenida
    fun MostrarBienvenida() {
        val trToastMSJ:String = this.getResources().getString(R.string.textMostrarBienvenida, InfoUsuarioStr)
        Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
        textNombreUsuario.text = InfoUsuarioStr
    }

    // Funcion para redondear imagen usuario
    // Tomado de <https://stackoverflow.com/questions/22105775/imageview-in-circular-through-xml>
    fun RedondearImagenUsuario() {
        val originalDrawable_male = ContextCompat.getDrawable(this, R.drawable.avatar_male)!!
        val originalDrawable_female = ContextCompat.getDrawable(this, R.drawable.avatar_female)!!
        val bitmap_male = convertDrawableToBitmap(originalDrawable_male)
        val bitmap_female = convertDrawableToBitmap(originalDrawable_female)
        val drawable_male = RoundedBitmapDrawableFactory.create(resources, bitmap_male)
        val drawable_female = RoundedBitmapDrawableFactory.create(resources, bitmap_female)
        drawable_male.setAntiAlias(true)
        drawable_male.cornerRadius = Math.max(bitmap_male.width, bitmap_male.height) / 2.0f
        imgUserMale.setImageDrawable(drawable_male)
        drawable_female.setAntiAlias(true)
        drawable_female.cornerRadius = Math.max(bitmap_female.width, bitmap_female.height) / 2.0f
        imgUserFemale.setImageDrawable(drawable_female)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgUserMale.outlineProvider = CircularShadowViewOutlineProvider()
            imgUserFemale.outlineProvider = CircularShadowViewOutlineProvider()
        }
    }

    // Funcion para renderizar imagen Usuario
    companion object {
        @JvmStatic
        fun convertDrawableToBitmap(drawable:Drawable) : Bitmap {
            if(drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            // We ask for the bounds if they have been set as they would be most
            // correct, then we check we are  > 0
            val bounds = drawable.bounds
            val width = if(!bounds.isEmpty) bounds.width() else drawable.intrinsicWidth
            val height = if(!bounds.isEmpty) bounds.height() else drawable.intrinsicHeight
            // Now we check we are > 0
            val bitmap = Bitmap.createBitmap(
                if(width <= 0) 1 else width, if(height <= 0) 1 else height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }

    // Funcion para activar el menu con iconos
    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu:Menu?) : Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    // Funcion para seleccionar opcion del menu
    override fun onOptionsItemSelected(item:MenuItem) : Boolean = when(item.itemId) {
        R.id.nav_Opciones -> {
            Toast.makeText(this, "Continua a Menu Usuario", Toast.LENGTH_LONG).show()
            val intentMenuUsuario = Intent(this, MenuUsuario::class.java)
            intentMenuUsuario.putExtra("DATA_Autor", InfoUsuarioStr)
            startActivity(intentMenuUsuario)
            true
        }
        R.id.nav_InfoSesion -> {
            val trDialogoMessageStyle:SpannableStringBuilder = SpannableStringBuilder(getString(R.string.textBienvenidaNav_InfoSesion, InfoNombreStr, InfoApellidoStr, InfoEmailStr))
            val Dialogo = AlertDialog.Builder(this)
                .setTitle(getString(R.string.textBienvenidaNav_InfoSesionTitle, InfoUsuarioStr))
                .setMessage(trDialogoMessageStyle)
                .setPositiveButton("OK", null)
            Dialogo.create()
            Dialogo.show()
            true
        }
        R.id.nav_Licencia -> {
            //var trDialogoMessage:String = getResources().getString(R.string.textBienvenidaNav_Licencia)
            val trDialogoMessageStyle:SpannableStringBuilder = SpannableStringBuilder(getString(R.string.textBienvenidaNav_Licencia))
            val Dialogo = AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(trDialogoMessageStyle)
                .setPositiveButton("OK", null)
            Dialogo.create()
            Dialogo.show()
            true
        }
        R.id.nav_AcercaDe -> {
            val BuildDate:String = SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(BuildConfig.BUILT_TIME)
            //var trDialogoMessage:String = getResources().getString(R.string.textBienvenidanav_AcercaDe, BuildConfig.VERSION_NAME, BuildDate)
            val trDialogoMessageStyle:SpannableStringBuilder = SpannableStringBuilder(getString(R.string.textBienvenidanav_AcercaDe, BuildConfig.VERSION_NAME, BuildDate))
            val Dialogo = AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(trDialogoMessageStyle)
                .setPositiveButton("OK", null)
            Dialogo.create()
            Dialogo.show()
            true
        }
        R.id.nav_Logout -> {
            val trToastMSJ:String = this.getResources().getString(R.string.textBienvenidaNav_Logout)
            Toast.makeText(this, trToastMSJ, Toast.LENGTH_LONG).show()
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
