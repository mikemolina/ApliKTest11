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
import com.mmolina.apliktest11.Modelo.PersonaDB

@Dao
interface PersonaDao {
    // Obtener lista de personas
    @Query("SELECT * FROM tblPersona")
    fun getLstPersonaDB() : LiveData<List<PersonaDB>>

    // Obtener una sola persona
    @Query("SELECT * FROM tblPersona WHERE idPersona = :id")
    fun getPersonaDB(id:Int) : LiveData<PersonaDB>

    // Insertar persona
    @Insert
    fun insertPersonaDBAll(vararg vararg_persona:PersonaDB)

    // Actualizar persona
    @Update
    fun updatePersonaDB(var_persona:PersonaDB)

    // Eliminar persona
    @Delete
    fun deletePersonaDB(var_persona:PersonaDB)
}
