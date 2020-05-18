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

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import com.gabrielepmattia.materialfields.R
import com.gabrielepmattia.materialfields.utils.Dialogs

open class FieldInputText : FieldGeneric {

    protected var changeListener: ((oldValue: String?, newValue: String?) -> Unit)? = null
    protected var autoSetValue = false

    /*
     * Constructors
     */

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr)
    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes)

    /*
     * Helpers
     */

    override fun initAttrs(attrs: AttributeSet) {
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldInputText) as TypedArray
        val tempRequired = t.getBoolean(R.styleable.FieldInputText_required, false)
        val tempAutoSetValue = t.getBoolean(R.styleable.FieldInputText_autoSetValue, false)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // set required if passed as parameter
        if (tempRequired) setRequired(true)
        autoSetValue = tempAutoSetValue
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Init the listener for the input dialog
        initOnClickListener()
    }

    private fun initOnClickListener() {
        setOnClickListener {
            Dialogs.showDialogWithInputAndPNButtons(
                context,
                LayoutInflater.from(context),
                title,
                context.getString(R.string.dialog_action_ok),
                context.getString(R.string.dialog_action_cancel), PositiveAction(), NegativeAction(), value
            )
        }
    }

    /*
     * Action
     */

    inner class PositiveAction : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
            val dialogView: AlertDialog = dialogInterface as AlertDialog
            val newText = dialogView.findViewById<EditText>(R.id.dialog_input_edittext).text
            if (!newText.equals(value)) {
                if (changeListener != null) changeListener!!(value, newText.toString())
                if (autoSetValue) value = newText.toString()

                // Validate new value
                if (validator != null) this@FieldInputText.validate()
            }
        }

    }

    inner class NegativeAction : DialogInterface.OnClickListener {
        override fun onClick(p0: DialogInterface?, p1: Int) {
            p0!!.cancel()
        }

    }

    /*
     * Common validators
     */

    /**
     * This function sets the fields as [required]. In other words, it sets a simple validator to the
     * field that checks if the field is not empty. If set to false it simply calls removeValidator
     */
    fun setRequired(required: Boolean) {
        if (required) {
            this.errorMessage = context.getString(R.string.required_tooltip, title)
            this.validator = { s: String? -> !(s == null || s.isEmpty()) }
        } else this.validator = null
    }
}