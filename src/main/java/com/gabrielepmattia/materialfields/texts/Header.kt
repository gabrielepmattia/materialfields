package com.gabrielepmattia.materialfields.texts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.gabrielepmattia.materialfields.R

/**
* @Project aj-android
* @Author gabry3795
* @Date 25/02/2018 21:16
*/

class Header : Text {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr)
    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes)

    /*
     * Helpers
     */

    override fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_header, this, true)
    }
}
