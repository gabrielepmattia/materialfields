package com.gabrielepmattia.materialfields

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

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
    protected var mAlertDrawableView: ImageView? = null

    var title: String?
        set(s) {
            if (s == null) {
                mTitleView!!.text = Field::class.java.simpleName
            } else {
                mTitleView!!.text = s
            }
        }
        get() {
            return mTitleView!!.text.toString()
        }

    var value: String? = ""
        set(s) {
            if (s == null || s.isEmpty()) mSubtitleView!!.text = context.getString(R.string.no_value)
            else mSubtitleView!!.text = s
            field = s
        }

    var drawable: Drawable?
        set(d) {
            if (d == null) {
                mDrawableView!!.visibility = GONE
            } else {
                mSubtitleView!!.visibility = VISIBLE
                mDrawableView!!.setImageDrawable(d)
            }
        }
        get() {
            return mDrawableView!!.drawable
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

    private fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        mTitleView = findViewById(R.id.field_title)
        mSubtitleView = findViewById(R.id.field_subtitle)
        mDrawableView = findViewById(R.id.field_image)
        mContainer = findViewById(R.id.field_container)
        mAlertDrawableView = findViewById(R.id.field_alert_image)
        mAlertDrawableView!!.visibility = GONE

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.Field) as TypedArray
        val tempTitle = t.getString(R.styleable.Field_title)
        if (tempTitle != null) title = tempTitle

        val tempSubtitle = t.getString(R.styleable.Field_value)
        value = tempSubtitle

        drawable = t.getDrawable(R.styleable.FieldInputText_drawable)
        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

    }

    override fun setOnClickListener(l: OnClickListener?) {
        mContainer!!.setOnClickListener(l)
    }

    /*
     * Validation
     */

    protected var validator: ((_: String?) -> Boolean)? = null
    protected var errorMessage: String? = null

    /**
     * Set the [validator] for the current field. A validator is composed by an [errorMessage],
     * to display when the validation is not passing and a validator, a function that takes as
     * input a string and returns a Boolean, true or false according to the fact that validation
     * passes or not
     */
    fun setValidator(errorMessage: String, validator: ((_: String?) -> Boolean)?) {
        this.validator = validator
        this.errorMessage = errorMessage
    }

    /**
     * Remove the current validator, if any
     */
    fun removeValidator() {
        this.validator = null
        this.errorMessage = null
    }

    /**
     * Validate the field by calling the set validator
     *
     * @return Response of validation
     */
    fun validate(): Boolean {
        if (validator == null) {
            Log.w(TAG, "No validator set! Validation returns always true")
            return true
        }
        val res = validator!!(value)
        if (res) mAlertDrawableView!!.visibility = View.GONE
        else {
            mAlertDrawableView!!.visibility = View.VISIBLE
            val t: Toast? = Toast.makeText(context, errorMessage, LENGTH_SHORT)
            t!!.show()
        }
        return res
    }

    companion object {
        private var TAG = Field::class.java.simpleName
    }

}