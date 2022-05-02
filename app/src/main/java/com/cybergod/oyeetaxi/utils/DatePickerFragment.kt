package com.cybergod.oyeetaxi.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.cybergod.oyeetaxi.utils.UtilsGlobal.stringDateToDate
import java.util.*
//class DatePickerFragment(val listener: (day:Int,month:Int,year:Int)  -> Unit, val initialYear:Int?,val initialMonth: Int?,val initialDay: Int? ): DialogFragment(), DatePickerDialog.OnDateSetListener {

class DatePickerFragment(val listener: (day:Int,month:Int,year:Int)  -> Unit,
                         private val initialDate:String?,
                         private val setMaxDate:Boolean?=false,
                         private val setMinDate:Boolean?=false,
                         ): DialogFragment(),
    DatePickerDialog.OnDateSetListener

{


    override fun onDateSet(p0: DatePicker?, yearOf: Int, monthOfYear: Int, dayOfMonth: Int) {

        listener(dayOfMonth,monthOfYear+1,yearOf)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = Calendar.getInstance()
        val lDay = calendar.get(Calendar.DAY_OF_MONTH)
        val lMonth = calendar.get(Calendar.MONTH+1)
        val lYear = calendar.get(Calendar.YEAR)

        val picker = DatePickerDialog(activity as Context, this, lYear,lMonth,lDay)


        return picker.apply {

            if (!initialDate.isNullOrEmpty()) {
                    stringDateToDate(initialDate)?.let { dateOK ->
                        val calendarInstance = Calendar.getInstance()
                        calendarInstance.time = dateOK
                        val year = calendarInstance[Calendar.YEAR]
                        val month = calendarInstance[Calendar.MONTH]
                        val day = calendarInstance[Calendar.DAY_OF_MONTH]
                        this.updateDate(
                            year,
                            month,
                            day)

                    }

                } else {
                    val calendarInstance = Calendar.getInstance()
                    val year = calendarInstance[Calendar.YEAR]
                    val month = calendarInstance[Calendar.MONTH]
                    val day = calendarInstance[Calendar.DAY_OF_MONTH]
                    this.updateDate(
                        year,
                        month,
                        day)
                }


            //fecha maxima
            if (setMaxDate == true) {
                this.datePicker.maxDate = calendar.timeInMillis
            }

            //fecha minima
            if (setMinDate == true) {
                this.datePicker.minDate = calendar.timeInMillis
            }


        }






        }





}