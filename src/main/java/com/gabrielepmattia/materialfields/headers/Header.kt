package com.gabrielepmattia.materialfields.headers

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.R

/**
 * Created by gabry3795 on 25/02/2018.
 */

class Header : LinearLayout {
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

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.Header) as TypedArray
        title = t.getString(R.styleable.Header_title)
        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}
