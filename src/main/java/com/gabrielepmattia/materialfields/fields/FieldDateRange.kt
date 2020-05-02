/*
 * Materialfields
 * Copyright (c) 2020 by gabrielepmattia, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.gabrielepmattia.materialfields.fields

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.gabrielepmattia.materialfields.R
import com.gabrielepmattia.materialfields.fields.FieldDateRange.Type.DATE_RANGE
import com.gabrielepmattia.materialfields.fields.FieldDateRange.Type.ONLY_START
import java.util.*


class FieldDateRange : Field {

    /**
     * Define the kind of the Data Range field.
     * - [DATE_RANGE] allows the user to set a range of dates
     * - [ONLY_START] allows the user to set only a start date
     */
    enum class Type {
        DATE_RANGE, ONLY_START
    }

    protected var mSwitch: Switch? = null
    protected var mStartDateView: ConstraintLayout? = null
    protected var mStartDateDate: TextView? = null
    protected var mStartDateTime: TextView? = null
    protected var mEndDateView: ConstraintLayout? = null
    protected var mEndDateDate: TextView? = null
    protected var mEndDateTime: TextView? = null
    protected var mDatesContainerView: LinearLayout? = null

    var dateStart: Date? = null
        set(d) {
            if (d == field) return
            field = d
            val dateFormat = DateFormat.getDateFormat(context)
            mStartDateDate!!.text = dateFormat.format(d)
            val timeFormat = DateFormat.getTimeFormat(context)
            mStartDateTime!!.text = timeFormat.format(d)
        }

    var dateEnd: Date? = null
        set(d) {
            if (d == field) return
            field = d
            val dateFormat = DateFormat.getDateFormat(context)
            mEndDateDate!!.text = dateFormat.format(d)
            val timeFormat = DateFormat.getTimeFormat(context)
            mEndDateTime!!.text = timeFormat.format(d)
        }

    var checked: Boolean
        set(b) {
            mSwitch!!.isChecked = b
            if (b) mDatesContainerView!!.visibility = View.VISIBLE
            else mDatesContainerView!!.visibility = GONE
        }
        get() {
            return mSwitch!!.isChecked
        }

    var dateStartDisabled: Boolean = false
    var dateEndDisabled: Boolean = false

    var type: Type = Type.DATE_RANGE

    /*
* Constructors
*/

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defAttr: Int) : super(context, attrs, defAttr)

    constructor(context: Context, attrs: AttributeSet, defAttr: Int, defRes: Int) : super(
        context,
        attrs,
        defAttr,
        defRes
    )

    /*
 * Helpers
 */
    override fun initView(context: Context) {
        val i: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        i.inflate(R.layout.component_field_daterange, this, true)
    }

    override fun initAttrs(attrs: AttributeSet) {
        mSwitch = findViewById(R.id.field_switch)
        mStartDateView = findViewById(R.id.field_daterange_startdate)
        mStartDateDate = findViewById(R.id.field_daterange_startdate_date)
        mStartDateTime = findViewById(R.id.field_daterange_startdate_time)
        mEndDateView = findViewById(R.id.field_daterange_enddate)
        mEndDateDate = findViewById(R.id.field_daterange_enddate_date)
        mEndDateTime = findViewById(R.id.field_daterange_enddate_time)
        mDatesContainerView = findViewById(R.id.field_daterange_dates_container)

        // get attrs
        val t: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldDateRange) as TypedArray
        val tempStartUnixDate = t.getFloat(R.styleable.FieldDateRange_materialfieldsDefaultStartDateUnixTimestamp, -1f)
        val tempEndUnixDate = t.getFloat(R.styleable.FieldDateRange_materialfieldsDefaultEndDateUnixTimestamp, -1f)
        val tempDateLimitRangeStartUnixDate =
            t.getFloat(R.styleable.FieldDateRange_materialfieldsDateLimitRangeStartUnixTimestamp, -1f)
        val tempDateLimitRangeEndUnixDate =
            t.getFloat(R.styleable.FieldDateRange_materialfieldsDateLimitRangeEndUnixTimestamp, -1f)
        val tempChecked = t.getBoolean(R.styleable.FieldDateRange_materialfieldsChecked, false)
        t.recycle()

        // init all base attrs
        super.initAttrs(attrs)

        // init other attributes
        dateStart = if (tempStartUnixDate > 0) Date(tempStartUnixDate.toLong()) else Date()
        dateEnd = if (tempEndUnixDate > 0) Date(tempEndUnixDate.toLong()) else Date()
        minDate = if (tempDateLimitRangeStartUnixDate > 0) Date(tempDateLimitRangeStartUnixDate.toLong()) else null
        maxDate = if (tempDateLimitRangeEndUnixDate > 0) Date(tempDateLimitRangeEndUnixDate.toLong()) else null
        checked = tempChecked

        // Hide dates container
        mDatesContainerView!!.visibility = GONE
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mContainer!!.setOnClickListener({ _: View ->
            if (!disabled) mSwitch!!.toggle()
        })

        mContainer!!.setOnLongClickListener(OnLongClickListener {
            if (!checked) return@OnLongClickListener false
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            val dialogInputView = LayoutInflater.from(context).inflate(R.layout.dialog_daterange_type, null)

            // Set the correct checked value in the dialog
            val radioGroup: RadioGroup =
                dialogInputView.findViewById<RadioGroup>(R.id.select_daterange_type_radio_group)
            when (type) {
                Type.DATE_RANGE -> radioGroup.check(R.id.select_daterange_type_daterange)
                Type.ONLY_START -> radioGroup.check(R.id.select_daterange_type_only_start)
            }

            builder.setView(dialogInputView)
            builder.setPositiveButton(context.getString(R.string.dialog_action_ok), SetTypeDialogOKAction())
            builder.setNegativeButton(context.getString(R.string.dialog_action_cancel), SetTypeDialogCancelAction())
            builder.show()
            true
        })

        mStartDateView!!.setOnClickListener({ _ ->
            showDatePicker(dateStartDisabled, dateStart, StartDateSetListener())
        })

        mStartDateTime!!.setOnClickListener({ _ ->
            showTimePicker(dateStartDisabled, dateStart, StartTimeSetListener())
        })

        mEndDateView!!.setOnClickListener({ _ ->
            showDatePicker(dateEndDisabled, dateEnd, EndDateSetListener())
        })

        mEndDateTime!!.setOnClickListener({ _ ->
            showTimePicker(dateEndDisabled, dateEnd, EndTimeSetListener())
        })

        mSwitch!!.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->
            run {
                mDatesContainerView?.visibility = if (b) View.VISIBLE else GONE
                mEndDateView?.visibility = if (type == Type.DATE_RANGE) View.VISIBLE else GONE
            }
        })
    }


    private inner class StartDateSetListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val c = Calendar.getInstance()
            c.time = dateStart
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            dateStart = c.time
        }

    }

    private inner class StartTimeSetListener : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            val c = Calendar.getInstance()
            c.time = dateStart
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minute)
            dateStart = c.time
        }

    }

    private inner class EndDateSetListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val c = Calendar.getInstance()
            c.time = dateEnd
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            dateEnd = c.time
        }

    }

    private inner class EndTimeSetListener : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            val c = Calendar.getInstance()
            c.time = dateEnd
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minute)
            dateEnd = c.time
        }

    }

    /*
     * Utils
     */

    private fun showTimePicker(
        disabled: Boolean,
        defaultDate: Date?,
        timeSetListener: TimePickerDialog.OnTimeSetListener
    ) {
        if (disabled) return
        // get end date and init the date picker
        val c = Calendar.getInstance()
        if (defaultDate != null) c.time = defaultDate
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog
        TimePickerDialog(
            context, timeSetListener, hour, minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showDatePicker(
        disabled: Boolean,
        defaultDate: Date?,
        dateSetListener: DatePickerDialog.OnDateSetListener
    ) {
        if (disabled) return
        // get start date and init the date picker
        val c = Calendar.getInstance()
        if (defaultDate != null) c.time = defaultDate
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog
        val d = DatePickerDialog(context, dateSetListener, year, month, day)
        if (minDate != null) d.datePicker.minDate = minDate!!.time
        if (maxDate != null) d.datePicker.maxDate = maxDate!!.time
        d.show()
    }

    private inner class SetTypeDialogOKAction : DialogInterface.OnClickListener {
        override fun onClick(p0: DialogInterface?, which: Int) {
            val dialog = p0 as AlertDialog
            val radioGroup: RadioGroup = dialog.findViewById(R.id.select_daterange_type_radio_group)
            val checked = radioGroup.checkedRadioButtonId
            when (checked) {
                R.id.select_daterange_type_daterange -> {
                    type = Type.DATE_RANGE
                    mEndDateView?.visibility = View.VISIBLE
                }
                R.id.select_daterange_type_only_start -> {
                    type = Type.ONLY_START
                    mEndDateView?.visibility = GONE
                }
            }
        }

    }

    private inner class SetTypeDialogCancelAction : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            dialog?.dismiss()
        }

    }

    /*
     * Validation
     */

    private var minDate: Date? = null
    private var maxDate: Date? = null

    /**
     * Set the range defined by [startDate] and [endDate] for validation of the field. By default
     * is always validated that startDate is before endDate
     */
    fun setValidation(startDate: Date?, endDate: Date?) {
        minDate = startDate
        maxDate = endDate
    }

    /**
     * Remove the current date limit range for validation
     */
    fun removeValidation() {
        minDate = null
        maxDate = null
    }

    /**
     * Perform the validation of the field
     */
    fun validate(): Boolean {
        if (!checked) return true
        var res = true
        if (type == DATE_RANGE) res = dateStart!!.before(dateEnd)
        if (minDate != null) res = res && minDate!!.before(dateStart)
        if (maxDate != null) res = res && dateEnd!!.before(maxDate)

        if (res) setAlertState(false)
        else {
            setAlertState(true)
            val t: Toast? = Toast.makeText(context, context.getString(R.string.date_not_valid), Toast.LENGTH_SHORT)
            t!!.show()
        }

        return res
    }

}