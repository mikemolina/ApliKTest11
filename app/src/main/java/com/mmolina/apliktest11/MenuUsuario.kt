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
import androidx.appcompat.app.AppCompatActivity

class MenuUsuario : AppCompatActivity() {
    private lateinit var AutorStr:String

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_usuario)
        AutorStr = intent.extras?.get("DATA_Autor").toString()
        if( savedInstanceState == null ) {
            val bundle_MenuUsuario:Bundle = Bundle()
            bundle_MenuUsuario.putString("DATA_Autor_to_opcfragm", AutorStr)
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.ContenedorVistaFragments, OpcionesFragment::class.java, bundle_MenuUsuario, "DATA_frag_Autor")
                .commit()
        }
    }
}