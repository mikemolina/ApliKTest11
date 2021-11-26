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

import android.annotation.TargetApi
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CircularShadowViewOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view:View, outline:Outline) {
        val size = Math.max(view.width, view.height)
        outline.setRoundRect(0, 0, size, size, size / 2f)
    }
}