package com.gabrielepmattia.materialfields

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by gabry3795 on 25/02/2018.
 */

class FieldsGroupHeader : LinearLayout {
    var mTitleView: TextView? = null

    var title: String
        set(s) {
            mTitleView!!.text = s
        }
        get() {
            return mTitleView!!.text.toString()
        }

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

    private fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_fields_group_header, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        mTitleView = findViewById(R.id.fields_group_header_title)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldsGroupHeader) as TypedArray
        title = t.getString(R.styleable.FieldsGroupHeader_title)
        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}
