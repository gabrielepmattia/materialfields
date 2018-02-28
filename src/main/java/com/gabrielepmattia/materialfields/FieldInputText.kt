package com.gabrielepmattia.materialfields

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.utils.Dialogs

/**
 * Created by gabry3795 on 25/02/2018.
 */
class FieldInputText : Field, View.OnClickListener {

    var changeListener: ((oldValue: String?, newValue: String?) -> Unit)? = null

    /*
* Constructors
*/

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes) {
        initAttrs(attrs)
    }

    /*
     * Helpers
     */

    private fun initAttrs(attrs: AttributeSet) {
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldInputText) as TypedArray
        val req = t.getBoolean(R.styleable.FieldInputText_required, false)
        t.recycle()

        if (req) setRequired(true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mContainer!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        Dialogs.showDialogWithInputAndPNButtons(context,
                LayoutInflater.from(context),
                title,
                context.getString(R.string.dialog_action_ok),
                context.getString(R.string.dialog_action_cancel), PositiveAction(), NegativeAction(), value)
    }

    /*
     * Action
     */

    inner class PositiveAction : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
            var dialogView: AlertDialog = dialogInterface as AlertDialog
            var newText = dialogView.findViewById<EditText>(R.id.dialog_input_edittext).text
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
            this.setValidator(context.getString(R.string.required_tooltip), { s: String? ->
                !(s == null || s.isEmpty())
            })
        } else this.removeValidator()
    }
}