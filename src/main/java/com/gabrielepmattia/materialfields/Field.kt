package com.gabrielepmattia.materialfields

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by gabry3795 on 26/02/2018.
 */
open class Field : LinearLayout {
    var mTitleView: TextView? = null
    var mSubtitleView: TextView? = null
    var mDrawableView: ImageView? = null
    var mContainer: ConstraintLayout? = null

    var title: String = ""
    var subtitle: String = ""
    var drawable: Drawable? = null

    protected var mCurrentValue: String? = null

    /*
 * Constructors
 */

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldInputText) as TypedArray
        title = t.getString(R.styleable.FieldInputText_title)
        subtitle = t.getString(t.getIndex(R.styleable.FieldInputText_subtitle))
        drawable = t.getDrawable(R.styleable.FieldInputText_drawable)
        t.recycle()
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
        mContainer = findViewById(R.id.field_input_container)

        mTitleView!!.text = title
        mSubtitleView!!.text = subtitle
        mDrawableView!!.setImageDrawable(drawable)

    }

    override fun setOnClickListener(l: OnClickListener?) {
        mContainer!!.setOnClickListener(l)
    }


}