package com.cybergod.oyeetaxi.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast


object Intents {


    private fun Context.getFacebookPageURLFromPageID(pageID:String?): String {

        val url = "https://www.facebook.com/$pageID"
        return try {

            val packageManager: PackageManager = this.packageManager
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo("com.facebook.katana", 0).longVersionCode.toInt()
            } else {
                packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            }

            if (versionCode >= 3002850) { //versiones nuevas de facebook
                "fb://facewebmodal/f?href=$url"
            } else { //versiones antiguas de fb
                "fb://page/$pageID"
            }

        } catch (e: PackageManager.NameNotFoundException) {
            url //normal web url
        }
    }


    fun Context.launchIntentToOpenFacebook(pageID:String){
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        val facebookUrl: String = this.getFacebookPageURLFromPageID(pageID)
        facebookIntent.data = Uri.parse(facebookUrl)
        this.startActivity(facebookIntent)
    }

    fun Context.launchIntentOpenWhatsapp(phone:String){
       val intent = Intent(Intent.ACTION_VIEW)
       val uri = "whatsapp://send?phone=$phone&text="   //val uri = "whatsapp://send?phone=" + "+5353208579" + "&text=" + ""
       intent.data = Uri.parse(uri)
       this.startActivity(intent)
    }

    fun Context.launchIntentOpenWebURL(url:String){
        val uri : Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW,uri)
        this.startActivity(intent)
    }


    fun Context.launchIntentOpenYoutube(channel_id:String){
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(url)
//        this.startActivity(intent)

        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://user/channel/$channel_id"));
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://c/$channel_id"));

        this.startActivity(intent);

    }

    fun Context.launchIntentOpenInstagram(userId:String){
        val uri = Uri.parse("https://instagram.com/_u/$userId")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.instagram.android")

        try {
            this.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //No encontró la aplicación, abre la versión web.
            this.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://instagram.com/$userId")
                )
            )
        }

    }

    fun Context.launchIntentOpenTwitter(userId:String){
        var intent: Intent? = null
        try {
            this.packageManager.getPackageInfo("com.Twitter.Android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("Twitter://user?user_id=$userId"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, " Twitter no esta instalado en tu telefono ", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$userId"))
            this.startActivity(intent)
        }
    }

    fun Context.launchIntentOpenLinkedIn(userId:String){
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://$userId"))
        val packageManager: PackageManager = this.packageManager
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.isEmpty()) {
            intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/in/$userId"))
                //Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=you"))
        }
        this.startActivity(intent)
    }

    fun Context.launchIntentOpenSendEmailTo(email:String){

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email"))
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //intent.putExtra(Intent.EXTRA_TEXT, message)
        this.startActivity(intent)

    }

    fun Context.launchIntentCallPhone(phoneNumber:String){
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${phoneNumber}")
        this.startActivity(intent)

    }



}