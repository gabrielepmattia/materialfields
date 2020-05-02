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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.gabrielepmattia.materialfields.R


/**
 * Simple checkbox form control. You have to specify:
 *  - title: The title of the checkbox (mandatory)
 *  - subtitle: The possible description of the checkbox
 */
class FieldCheckBox : Field {

    private var mCheckBox: CheckBox? = null

    var checked: Boolean
        set(b) {
            mCheckBox!!.isChecked = b
        }
        get() {
            return mCheckBox!!.isChecked
        }

    override var disabled: Boolean
        set(b) {
            if (b == disabled) return
            super.disabled = b
            mCheckBox?.isEnabled = !b
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
        i.inflate(R.layout.component_field_checkbox, this, true)
    }

    override fun initAttrs(attrs: AttributeSet) {
        mCheckBox = findViewById(R.id.field_check)

        // get attrs
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldCheckBox) as TypedArray
        val tempChecked = t.getBoolean(R.styleable.FieldCheckBox_materialfieldsChecked, false)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // init attrs
        checked = tempChecked
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mContainer!!.setOnClickListener { _: View ->
            if (!disabled) mCheckBox!!.toggle()
        }
    }
}