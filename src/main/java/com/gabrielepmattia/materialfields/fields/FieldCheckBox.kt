package com.gabrielepmattia.materialfields.fields

import android.content.Context
import android.content.res.TypedArray
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.R

/**
 * Created by gabry3795 on 27/02/2018.
 */

/**
 * Simple checkbox form control. You have to specify:
 *  - title: The title of the checkbox (mandatory)
 *  - subtitle: The possible description of the checkbox
 */
class FieldCheckBox : LinearLayout {

    private var mTitleView: TextView? = null
    private var mSubtitleView: TextView? = null
    private var mCheckBox: CheckBox? = null
    private var mContainer: ConstraintLayout? = null

    var title: String? = null
        set(s) {
            mTitleView!!.text = s
        }

    var subtitle: String? = null
        set(s) {
            mSubtitleView!!.text = s
        }

    var checked: Boolean
        set(b) {
            mCheckBox!!.isChecked = b
        }
        get() {
            return mCheckBox!!.isChecked
        }

    /*
* Constructors
*/

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr) {
        initView(context)
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes) {
        initView(context)
        initAttrs(attrs)
    }

    /*
     * Helpers
     */
    private fun initView(c: Context): View {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return i.inflate(R.layout.component_field_checkbox, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        mTitleView = findViewById(R.id.field_checkbox_item_title)
        mSubtitleView = findViewById(R.id.field_checkbox_item_subtitle)
        mCheckBox = findViewById(R.id.field_checkbox_item_check)
        mContainer = findViewById(R.id.field_checkbox_item_container)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldCheckBox) as TypedArray
        val tempTitle = t.getString(R.styleable.FieldCheckBox_title)
        if (tempTitle == null) throw RuntimeException("title cannot be empty!") else title = tempTitle

        val tempSubtitle = t.getString(R.styleable.FieldCheckBox_subtitle)
        if (tempSubtitle == null) mSubtitleView!!.visibility = GONE else subtitle = tempSubtitle

        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mContainer!!.setOnClickListener({ v: View ->
            mCheckBox!!.toggle()
        })
    }

}