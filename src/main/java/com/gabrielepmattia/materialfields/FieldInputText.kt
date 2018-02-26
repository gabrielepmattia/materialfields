package com.gabrielepmattia.materialfields

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
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

    var changeListener: (() -> String)? = null

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

    override fun onFinishInflate() {
        super.onFinishInflate()

        mContainer!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        Dialogs.showDialogWithInputAndPNButtons(context,
                LayoutInflater.from(context),
                title,
                context.getString(R.string.dialog_action_ok),
                context.getString(R.string.dialog_action_cancel), PositiveAction(), NegativeAction())
    }

    /*
     * Action
     */

    inner class PositiveAction: DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
            var dialogView: AlertDialog = dialogInterface as AlertDialog
            var newText = dialogView.findViewById<EditText>(R.id.dialog_input_edittext).text
            if(!newText.equals(mCurrentValue)) {
                mSubtitleView!!.text = newText
                if(changeListener != null) changeListener!!()
            }
        }

    }

    inner class NegativeAction: DialogInterface.OnClickListener {
        override fun onClick(p0: DialogInterface?, p1: Int) {
            p0!!.cancel()
        }

    }
}