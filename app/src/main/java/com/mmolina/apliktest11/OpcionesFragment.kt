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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class OpcionesFragment : Fragment() {
    private lateinit var AutorStr:String
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        AutorStr = requireArguments().getString("DATA_Autor_to_opcfragm").toString()
    }

    // Funcion para manejar fragmentos
    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?) : View? {
        val fragmento = inflater.inflate(R.layout.activity_opciones_fragment, container, false)
        val CrearPregunta:Button = fragmento.findViewById(R.id.btnOpcionesFragment_CrearPregunta)
        val RealizarTest:Button = fragmento.findViewById(R.id.btnOpcionesFragment_RealizarTest)
        val VerificarTest:Button = fragmento.findViewById(R.id.btnOpcionesFragment_VerificarTest)
        //
        CrearPregunta.setOnClickListener({
            val intentPreguntaMain = Intent(requireContext(), PreguntaMain::class.java)
            intentPreguntaMain.putExtra("DATA_Autor_to_pregmain", AutorStr)
            startActivity(intentPreguntaMain)
            //val bundle_OpcionesFragment:Bundle = Bundle()
            //bundle_OpcionesFragment.putString("DATA_Autor_to_pregfragm", AutorStr)
            //->bundle_OpcionesFragment
            /*
            activity?.getSupportFragmentManager()
                ?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.ContenedorVistaFragments, PreguntaMain::class.java, null, "DATA_Autor_to_pregfragm")
                ?.addToBackStack("")
                ?.commit()
            */
        })
        RealizarTest.setOnClickListener({
            /*
            activity?.getSupportFragmentManager()
                ?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.ContenedorVistaFragments, TestFragment::class.java, null, "Test")
                ?.addToBackStack("")
                ?.commit()
             */
        })
        return fragmento
    }
}
