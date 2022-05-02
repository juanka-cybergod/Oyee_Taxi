package com.cybergod.oyeetaxi.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.cybergod.oyeetaxi.utils.UtilsGlobal.stringTimeToTime
import java.util.*

class TimePickerFragment(val listener: (hour:Int, minute:Int)  -> Unit, private val initialTime:String?): DialogFragment(),
    TimePickerDialog.OnTimeSetListener
    
{


    override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {
        listener(hours,minutes)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = Calendar.getInstance()
        val lHour = calendar.get(Calendar.HOUR_OF_DAY)
        val lMinute = calendar.get(Calendar.MINUTE)


        val pickerTime = TimePickerDialog(activity as Context, this, lHour,lMinute,false)


        return pickerTime.apply {

            if (!initialTime.isNullOrEmpty()) {

                stringTimeToTime(initialTime)?.let { timeOK ->

                        val calendarInstance = Calendar.getInstance()
                        calendarInstance.time = timeOK
                        val hour = calendarInstance[Calendar.HOUR_OF_DAY]
                        val minute = calendarInstance[Calendar.MINUTE]


                        pickerTime.updateTime(
                            hour,
                            minute
                        )


                    }

                }


        }






        }



}