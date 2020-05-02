/*
 * Materialfields
 * Copyright (c) 2020 by gabrielepmattia, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.gabrielepmattia.materialfields.utils

import android.annotation.TargetApi
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.IOException


object BitmapResolver {
    private const val TAG = "BitmapResolver"

    private fun getBitmapLegacy(contentResolver: ContentResolver, fileUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun getBitmapImageDecoder(contentResolver: ContentResolver, fileUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
        if (fileUri == null) {
            Log.i(TAG, "returning null because URI was null")
            return null
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getBitmapImageDecoder(contentResolver, fileUri)
        } else {
            getBitmapLegacy(contentResolver, fileUri)
        }
    }
}