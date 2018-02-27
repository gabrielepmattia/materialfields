package com.gabrielepmattia.materialfields

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.Typeface.ITALIC
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gabrielepmattia.materialfields.utils.Dialogs

/**
 * Created by gabry3795 on 26/02/2018.
 */

/**
 * Build a fully functional shopping list control. Properties of the view:
 * - addItemPlaceholder Placeholder to see for add item button (mandatory)
 */
class FieldShoppingList : LinearLayout {

    private var mRecyclerView: RecyclerView? = null

    private var mAddItemPlaceHolder: String? = null

    /*
* Constructors
*/

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)

        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldShoppingList) as TypedArray
        if(!t.hasValue(R.styleable.FieldShoppingList_addItemPlaceHolder)) throw RuntimeException("addItemPlaceholder is mandatory")
        mAddItemPlaceHolder = t.getString(R.styleable.FieldShoppingList_addItemPlaceHolder)
        t.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(context, attrs, defAttr, defRes) {
        initView(context)
    }

    private fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field_shopping_list, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mRecyclerView = findViewById(R.id.field_shopping_list_recycler)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.adapter = FieldShoppingListRecyclerAdapter()
    }

    /*
     * Adapter
     */
    inner class FieldShoppingListRecyclerAdapter : RecyclerView.Adapter<FieldShoppingListRecyclerAdapter.ViewHolder>() {

        val TAG: String = FieldShoppingListRecyclerAdapter::class.java.simpleName

        private var items: ArrayList<String> = ArrayList()

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mIcon: ImageView? = null
            var mContent: TextView? = null

            init {
                mIcon = itemView.findViewById(R.id.field_shopping_list_item_image)
                mContent = itemView.findViewById(R.id.field_shopping_list_item_content)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.component_field_shopping_list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return items.size + 1
        }

        override fun onBindViewHolder(holder: FieldShoppingListRecyclerAdapter.ViewHolder, position: Int) {
            Log.d(TAG, "Currently binding position $position")
            // Set styles of last item and others
            if (holder.adapterPosition == itemCount - 1) {
                // Add item entry
                holder.mIcon!!.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.pencil))
                holder.mContent!!.text = mAddItemPlaceHolder
                holder.mContent!!.setTypeface(null, Typeface.ITALIC)
                holder.mContent!!.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorGrey600))
                holder.mIcon!!.setOnClickListener(null)
            } else {
                // Standard entry
                holder.mContent!!.text = items[holder.adapterPosition]
                holder.mIcon!!.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.close))
                holder.mContent!!.setTypeface(null, Typeface.NORMAL)
                holder.mContent!!.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorBlack))

                holder.mIcon!!.setOnClickListener { v:View ->
                    Dialogs.showDialogWithPNButton(
                            holder.itemView.context,
                            holder.itemView.context.getString(R.string.dialog_delete_header),
                            holder.itemView.context.getString(R.string.dialog_delete_description, holder.mContent!!.text),
                            holder.itemView.context.getString(R.string.dialog_action_ok),
                            holder.itemView.context.getString(R.string.dialog_action_cancel),
                            DeleteItemOKAction(holder.adapterPosition),
                            AddItemCancelAction(holder.adapterPosition)
                    )
                }
            }
            // Set behavior
            holder.itemView.setOnClickListener { v: View ->
                Dialogs.showDialogWithInputAndPNButtons(
                        holder.itemView.context,
                        LayoutInflater.from(holder.itemView.context),
                        mAddItemPlaceHolder!!,
                        holder.itemView.context.getString(R.string.dialog_action_ok),
                        holder.itemView.context.getString(R.string.dialog_action_cancel),
                        AddItemOKAction(holder.adapterPosition),
                        AddItemCancelAction(holder.adapterPosition),
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
                    mRecyclerView!!.adapter.notifyItemInserted(mPosition)
                }
                else {
                    items[mPosition] = editText.text.toString()
                    mRecyclerView!!.adapter.notifyItemChanged(mPosition)
                }

            }
        }

        inner class AddItemCancelAction(position: Int) : DialogInterface.OnClickListener {
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
                mRecyclerView!!.adapter.notifyDataSetChanged()
            }
        }

    }
}