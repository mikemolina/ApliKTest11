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

/* Cifrado de clave Hash Sha256 + salt
 * Autor: Ramesh Fadatare
 */

package com.mmolina.apliktest11

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import kotlin.experimental.and

class CifradoPWD {
    // Funcion para generar clave segura (hash + salt)
    fun getClaveSegura(pwd: String, salt:ByteArray?): String? {
        var nuevoPwd: String? = null
        try {
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            md.update(salt)
            val bytes: ByteArray = md.digest(pwd.toByteArray())
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(Integer.toString((bytes[i] and 0xff.toByte()) + 0x100, 16).substring(1))
            }
            nuevoPwd = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return nuevoPwd
    }

    // Funcion para generar salt
    @Throws(NoSuchAlgorithmException::class)
    fun getSalt():ByteArray? {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }
}
