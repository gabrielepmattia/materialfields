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

/**
 * @Project aj-android
 * @Author gabry3795
 * @Date 25/02/2018 21:16
 */
class FieldInputText : FieldGeneric {

    var changeListener: ((oldValue: String?, newValue: String?) -> Unit)? = null

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
        val req = t.getBoolean(R.styleable.FieldInputText_materialfieldsRequired, false)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // set required if passed as parameter
        if (req) setRequired(true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Init the listener for the input dialog
        initOnClickListener()
    }

    private fun initOnClickListener() {
        setOnClickListener {
            Dialogs.showDialogWithInputAndPNButtons(context,
                    LayoutInflater.from(context),
                    title,
                    context.getString(R.string.dialog_action_ok),
                    context.getString(R.string.dialog_action_cancel), PositiveAction(), NegativeAction(), value)
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
                value = newText.toString()

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