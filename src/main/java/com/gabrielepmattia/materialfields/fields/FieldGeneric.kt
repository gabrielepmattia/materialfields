package com.gabrielepmattia.materialfields.fields

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.gabrielepmattia.materialfields.R

/**
 * Created by gabry3795 on 02/03/2018.
 */
open class FieldGeneric : Field {
    protected var mDrawableView: ImageView? = null

    var drawable: Drawable?
        set(d) {
            if (d == null) {
                mDrawableView!!.visibility = LinearLayout.GONE
            } else {
                mSubtitleView!!.visibility = LinearLayout.VISIBLE
                mDrawableView!!.setImageDrawable(d)
            }
        }
        get() {
            return mDrawableView!!.drawable
        }

    override var disabled: Boolean
        set(b) {
            super.disabled = b
            if (b) mDrawableView?.setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.SRC_IN)
            else mDrawableView?.setColorFilter(ContextCompat.getColor(context, R.color.grey700), PorterDuff.Mode.SRC_IN)
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
        i.inflate(R.layout.component_field_generic, this, true)
    }

    override fun initAttrs(attrs: AttributeSet) {
        mDrawableView = findViewById(R.id.field_image)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldGeneric) as TypedArray
        val tempDrawable = t.getDrawable(R.styleable.FieldGeneric_drawable)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // init attrs
        drawable = tempDrawable
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }


    companion object {
        private var TAG = FieldGeneric::class.java.simpleName
    }

}