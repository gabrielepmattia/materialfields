package com.gabrielepmattia.materialfields

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by gabry3795 on 26/02/2018.
 */

/**
 * Generic field control with title and subtitle. The subtitle represent the actual value of the field
 */
open class Field : LinearLayout {
    protected var mTitleView: TextView? = null
    protected var mSubtitleView: TextView? = null
    protected var mDrawableView: ImageView? = null
    protected var mContainer: ConstraintLayout? = null

    var title: String
        set(s) {
            mTitleView!!.text = s
        }
        get() {
            return mTitleView!!.text.toString()
        }

    var subtitle: String
        set(s) {
            mSubtitleView!!.text = s
        }
        get() {
            return mTitleView!!.text.toString()
        }

    var drawable: Drawable?
        set(d) {
            mDrawableView!!.setImageDrawable(d)
        }
        get() {
            return mDrawableView!!.drawable
        }

    protected var mCurrentValue: String? = null

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

    private fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        mTitleView = findViewById(R.id.field_input_text_title)
        mSubtitleView = findViewById(R.id.field_input_text_subtitle)
        mDrawableView = findViewById(R.id.field_input_text_image)
        mContainer = findViewById(R.id.field_input_container)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldInputText) as TypedArray
        val tempTitle = t.getString(R.styleable.FieldInputText_title)
        if (tempTitle == null) throw RuntimeException("title cannot be empty!") else title = tempTitle

        val tempSubtitle = t.getString(R.styleable.FieldInputText_subtitle)
        if (tempSubtitle == null) mSubtitleView!!.visibility = View.GONE else subtitle = tempSubtitle

        drawable = t.getDrawable(R.styleable.FieldInputText_drawable)
        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

    }

    override fun setOnClickListener(l: OnClickListener?) {
        mContainer!!.setOnClickListener(l)
    }


}