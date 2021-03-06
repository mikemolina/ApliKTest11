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

package com.mmolina.apliktest11.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mmolina.apliktest11.Modelo.PersonaDB
import com.mmolina.apliktest11.Modelo.PreguntaDB
import com.mmolina.apliktest11.dao.PersonaDao
import com.mmolina.apliktest11.dao.PreguntaDao

@Database(entities = [PersonaDB::class, PreguntaDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DBPersona() : PersonaDao
    abstract fun DBPregunta() : PreguntaDao

    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? = null

        fun getDataBase(context:Context) : AppDatabase {
            val tempInstance = INSTANCE
            if( tempInstance != null ) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
