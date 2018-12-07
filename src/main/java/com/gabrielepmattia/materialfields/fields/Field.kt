package com.gabrielepmattia.materialfields.fields

import android.content.Context
import android.content.res.TypedArray
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.transition.Fade
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.R

/**
 * @Project aj-android
 * @Author gabry3795
 * @Date 26/02/2018 21:16
 */

/**
 * Generic field control with title and subtitle. The subtitle represent the actual value of the field
 */
open class Field : LinearLayout {
    protected var mTitleView: TextView? = null
    protected var mSubtitleView: TextView? = null
    protected var mContainer: ConstraintLayout? = null
    protected var mAlertDrawableView: ImageView? = null
    protected var mBottomLineSeparator: View? = null

    /**
     * The title of the field
     */
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

    /**
     * The value of the field (the subtitle)
     */
    var value: String = ""
        set(s) {
            if (s.isEmpty()) mSubtitleView!!.text = context.getString(R.string.no_value)
            else mSubtitleView!!.text = s
            field = s
        }

    /**
     * Set decoration and interaction disabled of the field
     */
    open var disabled: Boolean = false
        set(b) {
            if (b == field) return
            field = b
            if (b) {
                mContainer?.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
                mContainer?.isClickable = false
                mContainer?.isFocusable = false
                mTitleView?.setTextColor(ContextCompat.getColor(context, R.color.grey600))
                mSubtitleView?.setTextColor(ContextCompat.getColor(context, R.color.grey500))
                mBottomLineSeparator?.setBackgroundColor(ContextCompat.getColor(context, R.color.grey400))
            } else {
                mContainer?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                mContainer?.isClickable = true
                mContainer?.isFocusable = true
                mTitleView?.setTextColor(ContextCompat.getColor(context, R.color.black))
                mSubtitleView?.setTextColor(ContextCompat.getColor(context, R.color.grey600))
                mBottomLineSeparator?.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
            }
        }


    /*
     * Constructors
     */

    constructor(context: Context) : super(context) {
        this.initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.initView(context)
        this.initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr) {
        this.initView(context)
        this.initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes) {
        this.initView(context)
        this.initAttrs(attrs)
    }

    /*
     * Helpers
     */

    protected open fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field, this, true)
    }

    protected open fun initAttrs(attrs: AttributeSet) {
        mTitleView = findViewById(R.id.field_title)
        mSubtitleView = findViewById(R.id.field_subtitle)
        mContainer = findViewById(R.id.field_container)
        mBottomLineSeparator = findViewById(R.id.field_bottom_line_separator)
        mAlertDrawableView = findViewById(R.id.field_alert_image)
        mAlertDrawableView!!.visibility = GONE

        // get attrs
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.Field) as TypedArray
        val tempTitle = t.getString(R.styleable.Field_title)
        val tempSubtitle = t.getString(R.styleable.Field_value)
        val tempDisabled = t.getBoolean(R.styleable.Field_disabled, false)
        t.recycle()

        // set attrs
        if (tempTitle != null) title = tempTitle
        value = tempSubtitle ?: ""
        disabled = tempDisabled
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        // Override the click listener because field can be disabled
        mContainer!!.setOnClickListener({ v -> if (!disabled) l?.onClick(v) })
    }

    /*
     * Validation
     */

    /**
     * This is the error message to display when validation is performed and does not success
     */
    protected open var errorMessage: String? = null

    /**
     * Set the [state] of the alert for the field. This function enables the alert icon
     */
    fun setAlertState(state: Boolean) {
        val t = Fade()
        t.duration = 300
        TransitionManager.beginDelayedTransition(this, t)

        if (state) mAlertDrawableView!!.visibility = View.VISIBLE
        else mAlertDrawableView!!.visibility = View.GONE
    }

    companion object {
        private var TAG = Field::class.java.simpleName
    }

}