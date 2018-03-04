package com.gabrielepmattia.materialfields.fields

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.TypedArray
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.gabrielepmattia.materialfields.R
import java.util.*
import android.text.format.DateFormat.is24HourFormat
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.text.format.DateFormat
import android.app.DatePickerDialog
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.*
import kotlin.math.min


/**
 * Created by gabry3795 on 03/03/2018.
 */
class FieldDateRange : Field {

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
    override fun initView(c: Context) {
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
        val tempStartUnixDate = t.getFloat(R.styleable.FieldDateRange_defaultStartDateUnixTimestamp, -1f)
        val tempEndUnixDate = t.getFloat(R.styleable.FieldDateRange_defaultEndDateUnixTimestamp, -1f)
        val tempDateLimitRangeStartUnixDate = t.getFloat(R.styleable.FieldDateRange_dateLimitRangeStartUnixTimestamp, -1f)
        val tempDateLimitRangeEndUnixDate = t.getFloat(R.styleable.FieldDateRange_dateLimitRangeEndUnixTimestamp, -1f)
        val tempChecked = t.getBoolean(R.styleable.FieldDateRange_checked, false)
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

        mContainer!!.setOnClickListener({ v: View ->
            if (!disabled) mSwitch!!.toggle()
        })

        mStartDateView!!.setOnClickListener({ v ->
            showDatePicker(dateStartDisabled, dateStart, StartDateSetListener())
        })

        mStartDateTime!!.setOnClickListener({ v ->
            showTimePicker(dateStartDisabled, dateStart, StartTimeSetListener())
        })

        mEndDateView!!.setOnClickListener({ v ->
            showDatePicker(dateEndDisabled, dateEnd, EndDateSetListener())
        })

        mEndDateTime!!.setOnClickListener({ v ->
            showTimePicker(dateEndDisabled, dateEnd, EndTimeSetListener())
        })

        mSwitch!!.setOnCheckedChangeListener({ v: CompoundButton, b: Boolean ->
            run {
                mDatesContainerView!!.visibility = if (b) View.VISIBLE else GONE
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

    private fun showTimePicker(disabled: Boolean, defaultDate: Date?, timeSetListener: TimePickerDialog.OnTimeSetListener) {
        if (disabled) return
        // get end date and init the date picker
        val c = Calendar.getInstance()
        if (defaultDate != null) c.time = defaultDate
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog
        TimePickerDialog(context, timeSetListener, hour, minute,
                DateFormat.is24HourFormat(context)).show()
    }

    private fun showDatePicker(disabled: Boolean, defaultDate: Date?, dateSetListener: DatePickerDialog.OnDateSetListener) {
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
        var res = dateStart!!.before(dateEnd)
        if (minDate != null) res = res && minDate!!.before(dateStart)
        if (maxDate != null) res = res && dateEnd!!.before(maxDate)

        if (res) setAlertState(true)
        else {
            setAlertState(false)
            val t: Toast? = Toast.makeText(context, context.getString(R.string.date_not_valid), Toast.LENGTH_SHORT)
            t!!.show()
        }

        return res
    }

}