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
class FieldInputText : LinearLayout, View.OnClickListener {

    private var mTitleView: TextView? = null
    private var mSubtitleView: TextView? = null
    private var mDrawableView: ImageView? = null
    private var mContainer: ConstraintLayout? = null

    private var mTitle: String = ""
    private var mSubtitle: String = ""
    private var mDrawable: Drawable? = null

    private var mCurrentValue: String? = null

    var changeListener: (() -> Unit)? = null

    /*
     * Constructors
     */

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldInputText) as TypedArray
        mTitle = t.getString(R.styleable.FieldInputText_title)
        mSubtitle = t.getString(t.getIndex(R.styleable.FieldInputText_subtitle))
        mDrawable = t.getDrawable(R.styleable.FieldInputText_drawable)
        mContainer = findViewById(R.id.component_field_input_container)
        t.recycle()

        mContainer!!.setOnClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes) {
        initView(context)
    }

    /*
     * Helpers
     */

    private fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field_input_text, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mTitleView = findViewById(R.id.field_input_text_title)
        mSubtitleView = findViewById(R.id.field_input_text_subtitle)
        mDrawableView = findViewById(R.id.field_input_text_image)

        mTitleView!!.text = mTitle
        mSubtitleView!!.text = mSubtitle
        mDrawableView!!.setImageDrawable(mDrawable)

    }

    override fun onClick(p0: View?) {
        Dialogs.showDialogWithInputAndPNButtons(context,
                LayoutInflater.from(context),
                mTitle,
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