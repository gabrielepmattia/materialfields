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
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.gabrielepmattia.materialfields.R

open class FieldAction : LinearLayout {

    protected var mTitleView: TextView? = null
    protected var mContainer: ConstraintLayout? = null
    protected var mAlertDrawableView: ImageView? = null
    protected var mBottomLineSeparator: View? = null
    protected var mDrawableView: ImageView? = null

    /**
     * The title of the action
     */
    var title: String?
        set(s) {
            if (s == null) {
                mTitleView!!.text = Field::class.java.simpleName
            } else {
                mTitleView!!.text = s
            }
        }
        get() {
            return mTitleView!!.text.toString()
        }

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


    /**
     * Set decoration and interaction disabled of the field
     */
    open var disabled: Boolean = false
        set(b) {
            if (b == field) return
            field = b
            if (b) {
                mContainer?.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
                mContainer?.isClickable = false
                mContainer?.isFocusable = false
                mTitleView?.setTextColor(ContextCompat.getColor(context, R.color.grey600))
                mBottomLineSeparator?.setBackgroundColor(ContextCompat.getColor(context, R.color.grey400))
                mDrawableView?.setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.SRC_IN)
            } else {
                mContainer?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                mContainer?.isClickable = true
                mContainer?.isFocusable = true
                mTitleView?.setTextColor(ContextCompat.getColor(context, R.color.black))
                mBottomLineSeparator?.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
                mDrawableView?.setColorFilter(ContextCompat.getColor(context, R.color.grey700), PorterDuff.Mode.SRC_IN)
            }
        }
    /*
     * Constructors
     */
    /*
 * Constructors
 */

    constructor(context: Context) : super(context) {
        this.initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.initView(context)
        this.initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr) {
        this.initView(context)
        this.initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(
        context,
        attrs,
        defAttr,
        defRes
    ) {
        this.initView(context)
        this.initAttrs(context, attrs)
    }

    open fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field_action, this, true)
    }

    protected open fun initAttrs(context: Context, attrs: AttributeSet) {
        mTitleView = findViewById(R.id.field_title)
        mContainer = findViewById(R.id.field_container)
        mBottomLineSeparator = findViewById(R.id.field_bottom_line_separator)
        mDrawableView = findViewById(R.id.field_image)
        mAlertDrawableView = findViewById(R.id.field_alert_image)
        mAlertDrawableView!!.visibility = GONE

        // get attrs
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldAction) as TypedArray
        val tempTitle = t.getString(R.styleable.FieldAction_materialfieldsTitle)
        val tempDisabled = t.getBoolean(R.styleable.FieldAction_materialfieldsDisabled, false)
        val tempDrawable = t.getDrawable(R.styleable.FieldAction_materialfieldsDrawable)
        val tempDrawableTint = t.getColor(
            (R.styleable.FieldAction_materialfieldsDrawableTint),
            ContextCompat.getColor(context, R.color.grey700)
        )
        t.recycle()

        // set attrs
        if (tempTitle != null) title = tempTitle
        disabled = tempDisabled
        if (tempDrawable != null) {
            tempDrawable.setColorFilter(tempDrawableTint, PorterDuff.Mode.SRC_ATOP)
            drawable = tempDrawable
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        this.mContainer!!.setOnClickListener(l)
    }
}
