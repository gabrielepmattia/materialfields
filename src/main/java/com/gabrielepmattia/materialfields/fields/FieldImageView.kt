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

package com.gabrielepmattia.materialfields.fields

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.gabrielepmattia.materialfields.R
import com.gabrielepmattia.materialfields.utils.BitmapResolver
import com.makeramen.roundedimageview.RoundedImageView

class FieldImageView : FieldGeneric {

    private var mImageView: RoundedImageView? = null
    private var mHasImageBitmap: Boolean = false

    var imageUri: Uri? = null
        set(value) {
            if (value == null) {
                mImageView?.setImageDrawable(null)
                if (mHasImageBitmap) mImageView?.visibility = View.GONE
            } else {
                mImageView?.setImageBitmap(BitmapResolver.getBitmap(context.contentResolver, value))
                if (mHasImageBitmap) mImageView?.visibility = View.VISIBLE
            }
            field = value
        }

    /*
     * Constructors
     */

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr)
    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(
        context,
        attrs,
        defAttr,
        defRes
    )

    /*
     * Helpers
     */

    override fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = i.inflate(R.layout.component_field_image, this, true)
        mImageView = v.findViewById(R.id.field_imageview)
    }

    override fun initAttrs(attrs: AttributeSet) {
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldImageView) as TypedArray
        val imageUri = t.getString(R.styleable.FieldImageView_image)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // init attrs
        if (imageUri != null && imageUri.isNotEmpty()) this.imageUri = Uri.parse(imageUri)
    }

    fun setImageBitmap(b: Bitmap) {
        mHasImageBitmap = true
        mImageView?.setImageBitmap(b)
    }
}