package com.gabrielepmattia.materialfields.fields

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.gabrielepmattia.materialfields.R

/**
* @Project aj-android
* @Author gabry3795
* @Date 27/02/2018 21:16
*/

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
            if(b == disabled) return
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

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes)

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
        val tempChecked = t.getBoolean(R.styleable.FieldCheckBox_checked, false)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // init attrs
        checked = tempChecked
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mContainer!!.setOnClickListener({ _: View ->
            if(!disabled) mCheckBox!!.toggle()
        })
    }
}