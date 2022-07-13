package com.cybergod.oyeetaxi.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileManager {

    private const val MAX_SIZE = 2000000
    private const val QUALITY = 90

    private fun compressFile(file: File) : File {

        val fullSizeBitmap: Bitmap = getBitmapFromFile(file)
        val reducedSizeBitmap: Bitmap = ImageResizer.reduceBitmapSize(fullSizeBitmap,MAX_SIZE) //Varia la Resolucion Final de la Imagen
        val myFile  = File(file.absolutePath)
        //Omitir Crear Fichero pq se Utilizara la misa direccion q el Original Temporal
        //file.createNewFile()
        val compress = ByteArrayOutputStream()
        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG,QUALITY,compress) //Varia la Calidad de la Imagen
        val bitmap = compress.toByteArray()
        val output = FileOutputStream(myFile)
        output.write(bitmap)
        output.flush()
        output.close()

        return file

    }

    private fun getBitmapFromFile(file: File) = BitmapFactory.decodeFile(file.absolutePath)


    fun Context.prepareImageCompressAndGetFile(selectedImageURI: Uri): File?{
        val parcelFileDescriptor = this.contentResolver.openAssetFileDescriptor(selectedImageURI,"r", null)
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val file = File(this.cacheDir,this.contentResolver.getFileName(selectedImageURI))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        return try {
            compressFile(file)
        } catch (e: Exception) {
            null
        }
    }


    fun Context.loadFile(selectedFileURI: Uri): File?{
        val parcelFileDescriptor = this.contentResolver.openAssetFileDescriptor(selectedFileURI,"r", null)
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val file = File(this.cacheDir,this.contentResolver.getFileName(selectedFileURI))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        return try {
            file
        } catch (e: Exception) {
            null
        } finally {
            //file.delete()
        }
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri:Uri) :String{
        var name :String = ""
        val cursor = query(uri, null,null,null,null)
        cursor?.use {
            it.moveToFirst()
            name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }

        return name

    }


}