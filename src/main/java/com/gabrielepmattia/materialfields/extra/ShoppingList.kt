@file:Suppress("MemberVisibilityCanBePrivate")

package com.gabrielepmattia.materialfields.extra

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.Typeface
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.R
import com.gabrielepmattia.materialfields.utils.Dialogs

/**
 * @Project aj-android
 * @Author gabry3795
 * @Date 26/02/2018 21:16
 */

/**
 * Build a fully functional shopping list control. Properties of the view:
 * - addItemPlaceholder Placeholder to see for add item button (mandatory)
 */
class ShoppingList : LinearLayout {

    private var mRecyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var mRecyclerViewAdapter: ShoppingListRecyclerAdapter? = null
    private var mRecyclerViewLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mAddItemPlaceHolder: String? = null

    var items: ArrayList<String> = ArrayList()

    var disabledAdd: Boolean = false
        set(b) {
            if (b == field) return
            field = b
            // update only if different
            if (mRecyclerViewAdapter!!.disabledAdd != b) mRecyclerViewAdapter!!.disabledAdd = b
        }

    var disabledEntries: Boolean = false
        set(b) {
            if (b == field) return
            field = b
            // update only if different
            if (mRecyclerViewAdapter!!.disabledEntries != b) mRecyclerViewAdapter!!.disabledEntries = b
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

    private fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field_list, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        mRecyclerView = findViewById(R.id.field_list_recycler)
        mRecyclerViewAdapter = ShoppingListRecyclerAdapter()
        mRecyclerViewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShoppingList) as TypedArray
        mAddItemPlaceHolder = t.getString(R.styleable.ShoppingList_materialfieldsAddItemPlaceHolder)
        disabledAdd = t.getBoolean(R.styleable.ShoppingList_materialfieldsDisabledAdd, false)
        disabledEntries = t.getBoolean(R.styleable.List_materialfieldsDisabledEntries, false)
        t.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = mRecyclerViewLayoutManager
        mRecyclerView!!.adapter = mRecyclerViewAdapter
    }

    /*
     * Adapter
     */
    private inner class ShoppingListRecyclerAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ShoppingListRecyclerAdapter.ViewHolder>() {

        val TAG: String = ShoppingListRecyclerAdapter::class.java.simpleName

        var disabledAdd: Boolean = false
            set(b) {
                if (b == field) return
                field = b
                // update only the last entry and check if it's null. It's null when position has not yet
                // calculated and in this case onBindViewHolder will do the job
                val holderTemp: androidx.recyclerview.widget.RecyclerView.ViewHolder =
                        mRecyclerView!!.findViewHolderForAdapterPosition(itemCount - 1) ?: return
                val holder = holderTemp as ShoppingListRecyclerAdapter.ViewHolder
                if (b) holder.setDisabledElement() else holder.setAsAddElement()
            }

        var disabledEntries: Boolean = false
            set(b) {
                if (field == b) return
                field = b
                var holderTemp: androidx.recyclerview.widget.RecyclerView.ViewHolder
                var holder: ShoppingListRecyclerAdapter.ViewHolder
                // update all view holders of entries
                if (itemCount < 2) return // no entries available
                for (i in 0..mRecyclerViewAdapter!!.itemCount - 2) {
                    // check if current holder is null -- meaning not yet calculated the position
                    holderTemp = mRecyclerView!!.findViewHolderForAdapterPosition(i) ?: continue
                    holder = holderTemp as ShoppingListRecyclerAdapter.ViewHolder
                    if (b) holder.setDisabledElement() else holder.setAsNormalElement()
                }
            }

        inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            var mIcon: ImageView = itemView.findViewById(R.id.field_list_item_image)
            var mContent: TextView = itemView.findViewById(R.id.field_list_item_content)
            var mContainer: ConstraintLayout = itemView.findViewById(R.id.field_list_item_container)
            var mBottomLineSeparator: View = itemView.findViewById(R.id.field_list_item_bottom_line_separator)

            /**
             * Set the given view holder as a normal entry, with delete action
             */
            fun setAsNormalElement() {
                // Standard entry
                mContent.text = items[adapterPosition]
                mIcon.setImageDrawable(itemView.context.getDrawable(R.drawable.close))
                mContent.setTypeface(null, Typeface.NORMAL)
                mContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                mContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                mIcon.setColorFilter(ContextCompat.getColor(context, R.color.grey700), PorterDuff.Mode.SRC_IN)
                mIcon.setOnClickListener { _: View ->
                    if (disabledEntries) return@setOnClickListener
                    Dialogs.showDialogWithPNButton(
                            itemView.context,
                            itemView.context.getString(R.string.dialog_delete_header),
                            itemView.context.getString(R.string.dialog_delete_description, mContent.text),
                            itemView.context.getString(R.string.dialog_action_ok),
                            itemView.context.getString(R.string.dialog_action_cancel),
                            DeleteItemOKAction(adapterPosition),
                            AddItemCancelAction()
                    )
                }
                mBottomLineSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
            }

            /**
             * Set the current view holder as the adding entry
             */
            fun setAsAddElement() {
                mIcon.setImageDrawable(itemView.context.getDrawable(R.drawable.pencil))
                mContent.text = mAddItemPlaceHolder
                mContent.setTypeface(null, Typeface.ITALIC)
                mContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey600))
                mContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                mIcon.setColorFilter(ContextCompat.getColor(context, R.color.grey700), PorterDuff.Mode.SRC_IN)
                mIcon.setOnClickListener(null)
                mBottomLineSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
            }

            /**
             * Set the current element as disabled
             */
            fun setDisabledElement() {
                mContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.grey300))
                mContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey500))
                mIcon.setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.SRC_IN)
                mBottomLineSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.grey400))
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.component_field_shopping_list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return items.size + 1
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Set styles of last item and others
            if (holder.adapterPosition == itemCount - 1) {
                holder.setAsAddElement()
                if (disabledAdd) holder.setDisabledElement()
            } else {
                holder.setAsNormalElement()
                if (disabledEntries) holder.setDisabledElement()
            }

            // Set behavior for all elements
            holder.itemView.setOnClickListener { _: View ->
                if ((holder.adapterPosition == itemCount - 1 && disabledAdd) ||
                        (holder.adapterPosition != itemCount - 1 && disabledEntries)) return@setOnClickListener

                Dialogs.showDialogWithInputAndPNButtons(
                        holder.itemView.context,
                        LayoutInflater.from(holder.itemView.context),
                        mAddItemPlaceHolder!!,
                        holder.itemView.context.getString(R.string.dialog_action_ok),
                        holder.itemView.context.getString(R.string.dialog_action_cancel),
                        AddItemOKAction(holder.adapterPosition),
                        AddItemCancelAction(),
                        if (holder.adapterPosition < items.size) items[holder.adapterPosition] else ""
                )
            }
        }

        /*
         * Dialog actions
         */

        inner class AddItemOKAction(position: Int) : DialogInterface.OnClickListener {

            private var mPosition: Int = 0

            init {
                mPosition = position
            }

            override fun onClick(p0: DialogInterface?, p1: Int) {
                val alertDialog: AlertDialog = p0!! as AlertDialog
                val editText: EditText = alertDialog.findViewById(R.id.dialog_input_edittext)
                if (mPosition > items.size - 1) {
                    items.add(editText.text.toString())
                    mRecyclerView!!.adapter?.notifyItemInserted(mPosition)
                } else {
                    items[mPosition] = editText.text.toString()
                    mRecyclerView!!.adapter?.notifyItemChanged(mPosition)
                }

            }
        }

        inner class AddItemCancelAction() : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.cancel()
            }
        }

        inner class DeleteItemOKAction(position: Int) : DialogInterface.OnClickListener {

            private var mPosition: Int = 0

            init {
                mPosition = position
            }

            override fun onClick(p0: DialogInterface?, p1: Int) {
                items.removeAt(mPosition)
                //mRecyclerView!!.adapter.notifyItemRemoved(mPosition)
                mRecyclerView!!.adapter?.notifyDataSetChanged()
            }
        }

    }
}