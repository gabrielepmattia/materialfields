package com.gabrielepmattia.materialfields.texts

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.R

/**
 * @Project aj-android
 * @Author gabry3795
 * @Date 03/03/2018 21:16
 */
open class Text : LinearLayout {

    var mTextView: TextView? = null

    var text: String
        set(s) {
            mTextView!!.text = s
        }
        get() {
            return mTextView!!.text.toString()
        }

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

    protected open fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_text, this, true)
    }

    protected fun initAttrs(attrs: AttributeSet) {
        mTextView = findViewById(R.id.text_text)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.Text) as TypedArray
        val tempText = t.getString(R.styleable.Text_materialfieldsText) ?: ""
        val tempTextAlignement = t.getString(R.styleable.Text_materialfieldsTextAlignment) ?: ""
        //val tempTextSize = t.getDimension(R.styleable.Text_textSize, )
        t.recycle()

        // set attributes
        text = tempText
        //mTextView?.textSize = tempTextSize
        mTextView?.textAlignment = when (tempTextAlignement) {
            "center" -> View.TEXT_ALIGNMENT_CENTER
            else -> View.TEXT_ALIGNMENT_TEXT_START
        }
    }
}