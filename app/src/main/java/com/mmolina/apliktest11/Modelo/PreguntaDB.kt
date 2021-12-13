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

package com.mmolina.apliktest11.Modelo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/* Clase PreguntaDB */
@Entity(tableName = "tblPregunta")
class PreguntaDB (
    val asignatura:String,
    val enunciado:String,
    val opcItemA:String,
    val opcItemB:String,
    val opcItemC:String,
    val opcItemD:String,
    val respuesta:String,
    val autor:String,
    val imgAsignatura:Int,
    @PrimaryKey( autoGenerate = true )
    var idPregunta:Int = 0
) : Serializable
