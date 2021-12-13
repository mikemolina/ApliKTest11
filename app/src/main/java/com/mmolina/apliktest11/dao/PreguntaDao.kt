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

package com.mmolina.apliktest11.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mmolina.apliktest11.Modelo.PreguntaDB

@Dao
interface PreguntaDao {
    // Obtener lista de preguntas
    @Query("SELECT * FROM tblPregunta")
    fun getLstPreguntasDB(): LiveData<List<PreguntaDB>>

    // Obtener una sola pregunta
    @Query("SELECT * FROM tblPregunta WHERE idPregunta = :id")
    fun getPreguntaDB(id:Int): LiveData<PreguntaDB>

    // Insertar pregunta
    @Insert
    fun insertPreguntaDBAll(vararg vararg_pregunta:PreguntaDB)

    // Actualizar pregunta
    @Update
    fun updatePreguntaDB(var_pregunta:PreguntaDB)

    // Eliminar pregunta
    @Delete
    fun deletePreguntaDB(var_pregunta:PreguntaDB)
}
