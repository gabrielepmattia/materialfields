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
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.gabrielepmattia.materialfields.R

open class FieldGeneric : Field {
    protected var mDrawableView: ImageView? = null

    var drawable: Drawable?
        set(d) {
            if (d == null) {
                mDrawableView!!.visibility = LinearLayout.GONE
            } else {
                mDrawableView!!.setImageDrawable(d)
            }
        }
        get() {
            return mDrawableView!!.drawable
        }

    override var disabled: Boolean
        set(b) {
            if (b == disabled) return
            super.disabled = b
            if (b) mDrawableView?.setColorFilter(
                ContextCompat.getColor(context, R.color.grey500),
                PorterDuff.Mode.SRC_IN
            )
            else mDrawableView?.setColorFilter(ContextCompat.getColor(context, R.color.grey700), PorterDuff.Mode.SRC_IN)
        }
        get() {
            return super.disabled
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
        i.inflate(R.layout.component_field_generic, this, true)
    }

    override fun initAttrs(attrs: AttributeSet) {
        mDrawableView = findViewById(R.id.field_image)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldGeneric) as TypedArray
        val tempDrawable = t.getDrawable(R.styleable.FieldGeneric_drawable)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // init attrs
        drawable = tempDrawable
    }

    /*
     * Validation
     */

    /**
     * For a generic field a validator is a function that takes as input a string a returns a boolean
     * whether the validation passed or not. Remember to set the error message when setting a validator.
     */
    protected open var validator: ((_: String?) -> Boolean)? = null

    /**
     * Validate the field by calling the set validator
     *
     * @return Response of validation
     */
    fun validate(): Boolean {
        if (validator == null) {
            Log.w(TAG, "No validator set! Validation returns always true")
            return true
        }
        val res = validator!!(value)
        setAlertState(!res)
        // show a toast when validation does not succeed
        if (!res) {
            val t: Toast? = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
            t!!.show()
        }
        return res
    }


    companion object {
        private var TAG = FieldGeneric::class.java.simpleName
    }

}