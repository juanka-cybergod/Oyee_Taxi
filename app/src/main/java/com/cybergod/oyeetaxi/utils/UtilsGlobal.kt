package com.cybergod.oyeetaxi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.cybergod.oyeetaxi.BuildConfig
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.utils.Constants.URL_BASE
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.material.textfield.TextInputLayout
import java.lang.reflect.Method
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object UtilsGlobal {

    private const val TAG = "UtilsGlobal"

    fun AutoCompleteTextView.showDropDownMenuFix(adapter: ArrayAdapter<String>?) {
        if (this.text.isNotEmpty()){
            adapter?.filter?.filter(null)
        }
    }

    fun Context.isGooglePlayServicesAvailable(activity: Activity): Boolean {
        val GPS_ERROR_DIALOG_REQUEST = 9001
        val isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            val dialog: Dialog? = GooglePlayServicesUtil.getErrorDialog(
                isAvailable,
                activity, GPS_ERROR_DIALOG_REQUEST
            )
            dialog!!.show()
        } else {
            Toast.makeText(this, "No se pudo conectar con los Servicios de Google Play", Toast.LENGTH_SHORT)
                .show()
        }
        return false
    }

    fun getAppVersionInt(): Int {
        return BuildConfig.VERSION_CODE
    }

    fun getAppVersionString(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getCurrentDate():String{
        val calendar: Calendar = Calendar.getInstance()
        val lDay = calendar.get(Calendar.DAY_OF_MONTH)
        val lMonth = calendar.get(Calendar.MONTH+1)
        val lYear = calendar.get(Calendar.YEAR)
        return onDateSelected(lDay,lMonth,lYear)
    }

    fun onDateSelected(day: Int, month: Int, year: Int): String {

        var mes = month.toString()
        var dia = day.toString()
        val ano = year.toString()

        if (mes.length == 1) {
            mes = "0$month"
        }
        if (dia.length == 1) {
            dia = "0$dia"
        }

        return "${ano}-${mes}-${dia}"


    }

    fun onTimeSelected(hours: Int, minutes: Int): String {

        var tt:String = "AM"
        var hour = hours


        if (hours < 12){
            tt = "AM"

        }
        if (hours > 12){
            tt = "PM"
            hour -= 12
        }

        if (hours == 12){
            tt = "PM"
            hour = 12
        }
        if (hours == 0){
            tt = "AM"
            hour = 12
        }

        var horaOK:String = hour.toString()
        if (horaOK.length == 1) {
            horaOK = "0$horaOK"
        }
        var minutosOK:String = minutes.toString()
        if (minutosOK.length == 1) {
            minutosOK = "0$minutosOK"
        }



        return "${horaOK}:${minutosOK} ${tt}"


    }

    fun View.setOnDateSelected(day:Int, month:Int, year:Int){

        var mes = month.toString()
        var dia = day.toString()
        val ano = year.toString()

        if (mes.length == 1 ) {
            mes = "0$month"
        }
        if (dia.length == 1 ) {
            dia = "0$dia"
        }

        val fechaEntrada = "${ano}-${mes}-${dia}"


        (this as TextInputLayout).editText?.setText(fechaEntrada)
       // binding.tvFechaNacimiento.editText!!.setText(fechaEntrada)

    }

    fun Activity.pasteFromClipBoard():String{

        var textToPaste: String? = null
        val clipboardManager : ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // val clipboardManager : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (clipboardManager.hasPrimaryClip()) {
            val clip = clipboardManager.primaryClip

            if (clip!!.description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                textToPaste = clip.getItemAt(0).text.toString()
            } else {
                textToPaste = clip.getItemAt(0).coerceToText(this).toString()
            }

        }

        return textToPaste!!

    }

    fun getFullURL(urlRelativa:String):String {
        return URL_BASE+urlRelativa
    }

    fun getOyeeTaxiApiKeyEncoded(): String {
        val typeEncode = "Basic "
        val BASE_API_KEY = "${BuildConfig.USER_API_KEY}:${BuildConfig.PASS_API_KEY}"
        return typeEncode + Base64.encodeToString(BASE_API_KEY.toByteArray(), Base64.NO_WRAP)

    }

    fun passwordEncode(password:String): String {
        return Base64.encodeToString(password.toByteArray(),Base64.NO_WRAP)
    }

    fun passwordDecode(password:String): String {
        val decodedBytes: ByteArray = Base64.decode(password,Base64.NO_WRAP)
        return String(decodedBytes)
    }

    fun String.wordCount(): Int {
        val trimmedStr = this.trim()
        return if (trimmedStr.isEmpty()) {
            0
        } else {
            trimmedStr.split("\\s+".toRegex()).size
        }
    }

    fun String.isValidEmail(): Boolean {
        if (TextUtils.isEmpty(this.trim { it <= ' ' })) return false
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(this).matches()
    }

    fun  GetLocalTimeinMillis() : Long {
        return System.currentTimeMillis().toLong()
    }

    fun getCurrentYear():Int {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        return year
    }

    fun stringTimeToTime(stringTime: String?): Date? {
        if (stringTime.contentEquals("PM")) {
            stringTime?.replace(" PM","",true)
        }
        if (stringTime.contentEquals("AM")) {
            stringTime?.replace(" AM","",true)
        }
        val inputSDF = SimpleDateFormat("hh:mm", Locale.getDefault())
        var date: Date? = null
        date = try {
            //here you get Date object from string
            inputSDF.parse(stringTime!!)
        } catch (e: ParseException) {
            return null
        }
        //after changing date format again you can change to string with changed format
        Log.d(TAG, "stringTimeToTime = ${date}")
        return date
    }

    fun stringDateToDate(stringDate: String?): Date? {
        val inputSDF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date: Date? = null
        date = try {
            //here you get Date object from string
            inputSDF.parse(stringDate!!)
        } catch (e: ParseException) {
            return null
        }
        //after changing date format again you can change to string with changed format
        Log.d(TAG, "stringDateToDate = ${date}")
        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun getAge(dobString: String): Int {


        var date: Date? = null
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            date = sdf.parse(dobString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date == null) return 0
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.time = date
        val year = dob[Calendar.YEAR]
        val month = dob[Calendar.MONTH]
        val day = dob[Calendar.DAY_OF_MONTH]
        dob[year, month + 1] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }


        return age
    }

    @SuppressLint("SimpleDateFormat")
    fun timePassedFromDateString(dateString: String): String {

        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd"
        )

            val oldDate = dateFormat.parse(dateString)
            val currentDate = Date()
            val diff = currentDate.time - oldDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val month = days / 30
            val years = month / 12


        return when (days.toInt()) {
            0 -> {
                "Hoy se unió a Oyee Taxi"
            }
            1 -> {
                "Un día en Oyee Taxi"
            }
            else -> {
                "${days.toInt()} días en Oyee Taxi"
            }
        }


    }

    fun logD(activity: Activity,text: String){
        Log.d(activity.getString(R.string.apklog)+"-"+activity.javaClass.name+"=",text)
    }

    fun getRamdomUUID(): String {
        return UUID.randomUUID().toString().apply {
            replace("-","",true)
            lowercase()
        }
    }

    fun logGlobal(className:String, metodo: Method?, logText :String){
        Log.d(className, "${metodo?.name?: "MetodoDesconocido"}: $logText")
    }






}

