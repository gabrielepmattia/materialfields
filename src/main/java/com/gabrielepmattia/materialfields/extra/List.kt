package com.gabrielepmattia.materialfields.extra

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.R

/**
 * @Project aj-android
 * @Author gabry3795
 * @Date 07/03/2018 15:54
 */

class List : LinearLayout {

    private var mRecyclerView: RecyclerView? = null
    private var mAddItemPlaceHolder: String? = null
    private var mRecyclerViewAdapter: ListItemRecyclerAdapter? = null
    private var mRecyclerViewLayoutManager: LinearLayoutManager? = null

    /*
     * Public vars
     */

    /**
     * Disabled state of the add entry of the List
     */
    var disabledAdd: Boolean = false

    /**
     * Disabled state of the entries of the list
     */
    var disabledEntries: Boolean = false

    /**
     * Programmatically set the display items of the list
     */
    var items: ArrayList<Pair<String, String>>?
        set(s) {
            mRecyclerViewAdapter?.items = s
        }
        get() {
            return mRecyclerViewAdapter?.items
        }

    /**
     * Action to be performed when clicking an entry of the list
     */
    var addAction: OnClickListener? = null
        set(o) {
            mRecyclerViewAdapter!!.addAction = o
        }

    /*
     * Constructor
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
        i.inflate(R.layout.component_field_list, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        mRecyclerView = findViewById(R.id.field_list_recycler)
        mRecyclerViewAdapter = ListItemRecyclerAdapter()
        mRecyclerViewLayoutManager = LinearLayoutManager(context)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShoppingList) as TypedArray
        mAddItemPlaceHolder = t.getString(R.styleable.List_addItemPlaceHolder)
        disabledAdd = t.getBoolean(R.styleable.List_disabledAdd, false)
        disabledEntries = t.getBoolean(R.styleable.List_disabledEntries, false)
        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = mRecyclerViewLayoutManager
        mRecyclerView!!.adapter = mRecyclerViewAdapter
    }

    private inner class ListItemRecyclerAdapter() : RecyclerView.Adapter<ListItemRecyclerAdapter.ViewHolder>() {

        var items: ArrayList<Pair<String, String>>? = null
            set(s) {
                field = s
                notifyDataSetChanged()
            }

        /**
         * The action performed when clicking on the add item of the list
         */
        var addAction: OnClickListener? = null
            set(o) {
                if (o == field) return
                field = o
                // check if the view is ready otherwise the listener will be set onBindView
                val tempAddEntryHolder = mRecyclerView!!
                        .findViewHolderForAdapterPosition(itemCount - 1) ?: return
                val addEntryHolder = tempAddEntryHolder as ViewHolder
                addEntryHolder.itemView.setOnClickListener(o)
            }

        constructor(items: ArrayList<Pair<String, String>>?) : this() {
            this.items = items
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mIcon: ImageView = itemView.findViewById(R.id.field_list_item_image)
            var mContent: TextView = itemView.findViewById(R.id.field_list_item_content)
            var mSubcontent: TextView = itemView.findViewById(R.id.field_list_item_subcontent)
            var mContainer: ConstraintLayout = itemView.findViewById(R.id.field_list_item_container)
            var mBottomLineSeparator: View = itemView.findViewById(R.id.field_list_item_bottom_line_separator)

            fun setAsAddElement() {
                mContent.text = mAddItemPlaceHolder
                mSubcontent.visibility = GONE
                mIcon.setImageDrawable(context.getDrawable(R.drawable.plus))

                mContent.setTypeface(null, Typeface.ITALIC)
                mContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey600))
                mContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                itemView.setOnClickListener(addAction)
            }

            fun setAsNormalElement() {
                mContent.text = items!![adapterPosition].first
                mSubcontent.text = items!![adapterPosition].second
                mSubcontent.visibility = View.VISIBLE
                mIcon.setImageDrawable(context.getDrawable(R.drawable.close))
                mIcon.setOnClickListener({ _ ->
                    items!!.removeAt(adapterPosition)
                    notifyDataSetChanged()
                })

                mContent.setTypeface(null, Typeface.NORMAL)
                mContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                mContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            fun setAsDisabledElement() {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.component_field_list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return if (items == null) 1 else items!!.size + 1
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (holder.adapterPosition == itemCount - 1) {
                holder.setAsAddElement()
                if (disabledAdd) holder.setAsDisabledElement()
            } else {
                holder.setAsNormalElement()
                if (disabledEntries) holder.setAsDisabledElement()
            }
        }
    }
}