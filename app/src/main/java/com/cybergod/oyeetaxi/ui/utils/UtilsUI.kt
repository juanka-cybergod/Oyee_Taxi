package com.cybergod.oyeetaxi.ui.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.travel.model.MetodoPago
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.api.futures.vehicle.model.response.VehiculoResponse
import com.cybergod.oyeetaxi.api.futures.user.model.verification.UsuarioVerificacion
import com.cybergod.oyeetaxi.api.futures.vehicle.model.verification.VehiculoVerificacion
import com.cybergod.oyeetaxi.databinding.DialogBottomProgressBinding
import com.cybergod.oyeetaxi.databinding.TextInputBinding
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURLNoCache
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonVehiculoActivo
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setEstadoVerificacionUsuario
import com.cybergod.oyeetaxi.utils.UtilsGlobal
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getCurrentYear
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.lang.Exception
import java.util.*


@SuppressLint("StaticFieldLeak")
object UtilsUI {


    fun RecyclerView.getItemCount():Int{
        return (this.layoutManager as LinearLayoutManager).itemCount
    }

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var BottomSheetProgressBinding: DialogBottomProgressBinding

    fun Activity.showBottomProgressDialog(message: String) {
        BottomSheetProgressBinding = DialogBottomProgressBinding.inflate(layoutInflater).apply {
            tvProgressText.text = message
        }
        mBottomSheetDialog = BottomSheetDialog(this).apply {
            setContentView(BottomSheetProgressBinding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        mBottomSheetDialog.show()
    }

    fun hideBottomProgressDialog() {
        try {
            mBottomSheetDialog.dismiss()
        } catch (e: Exception) {
        }

    }

    fun Activity.showSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarSuccess
                )
            )
        }

        //Cerrar Forzado el Teclado para que se puedan ver los mensages de error
        View(this).hideKeyboard()


        snackBar.show()
    }

    fun View.hideKeyboard() {
        val inputMethodManager = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }


    fun Context.showSimpleAlertDialog(title: String, text: String){
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle(title)
            .setMessage(text)
            .setIcon(R.mipmap.ic_launcher_foreground)
            .setPositiveButton("OK"){ dialogInterface , _ ->
                //Toast.makeText(this,"",Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }




    fun View.setTipoClienteConductor(conductor: Boolean?){

        val button = (this as MaterialButton)


        when (conductor) {
            true -> {
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_driver_user,null)
                //button.text = "Conductor"
            }
            else -> {
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_client_user,null)
                //button.text = "Pasajero"
            }
        }


    }

    fun View.setActualizacionFileExist(existe: Boolean?){

        val button = (this as MaterialButton)


        when (existe) {
            true -> {
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_cloud_ok_24,null)
                //button.text = "Conductor"
            }
            else -> {
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_cloud_no_ok_24,null)
                //button.text = "Pasajero"
            }
        }


    }


    fun View.setActualizacionForceUpdate(forceUpdate: Boolean?){

        val button = (this as MaterialButton)


        when (forceUpdate) {
            true -> {
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_check_circle_24,null)
                //button.text = "Conductor"
            }
            else -> {
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_unchecked_circle_24,null)
                //button.text = "Pasajero"
            }
        }


    }



    fun View.setFicheroActualizacionSeleccionado(fichero: File?){

        val button = (this as MaterialButton)

        if (fichero != null) {
            button.text = "${fichero.name} Seleccionado"
            button.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.casiAmarillo,null))
            button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_cloud_ok_24,null)
        } else {
            button.text =  resources.getText(R.string.select_apk_file_to_upload)
            button.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.casiRojo,null))
            button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_cloud_no_ok_24,null)
        }

    }

    fun View.setEstadoVerificacionUsuario(usuarioVerificacion: UsuarioVerificacion?){

        val button = (this as MaterialButton)


        if (usuarioVerificacion?.verificado == true) {
            //Verificado OK
            button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_verified,null)
            button.setIconTintResource(R.color.colorCasiVerde)

        } else {
            if (usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty() || usuarioVerificacion?.identificacion.isNullOrEmpty()) {
                //No Llenada
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_verified,null)
                button.setIconTintResource(R.color.casiBlancoObscuro)
            } else {
                //Pendiente a Aprobar
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_verified,null)
                button.setIconTintResource(R.color.casiRojo)
            }

        }
    }
    fun View.setEstadoVerificacionVehiculo(vehiculoVerificacion: VehiculoVerificacion?){

        val button = (this as MaterialButton)

        if (vehiculoVerificacion?.verificado == true) {
            //Verificado OK
            button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_verified,null)
            button.setIconTintResource(R.color.colorCasiVerde)

        } else {
            if (vehiculoVerificacion?.matricula.isNullOrEmpty() || vehiculoVerificacion?.circulacion.isNullOrEmpty() || vehiculoVerificacion?.imagenCirculacionURL.isNullOrEmpty()) {
                //No Llenada
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_verified,null)
                button.setIconTintResource(R.color.casiBlancoObscuro)
            } else {
                //Pendiente a Aprobar
                button.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_verified,null)
                button.setIconTintResource(R.color.casiRojo)
            }

        }
    }






    fun View.setMetodoPagoButton(metodoPago: MetodoPago){

        val buttonMetodoPago = (this as MaterialButton)


        when (metodoPago) {
            MetodoPago.EFECTIVO -> {
                buttonMetodoPago.text = "Forma de Pago\n$metodoPago"
                buttonMetodoPago.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_money_32, 0, 0)
                //buttonMetodoPago.background.setTint(ResourcesCompat.getColor(resources, R.color.casiBlanco, null))
            }
            MetodoPago.TARJETA -> {
                buttonMetodoPago.text = "Forma de Pago\n$metodoPago"
                buttonMetodoPago.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card_32, 0, 0)
                //buttonMetodoPago.background.setTint(ResourcesCompat.getColor(resources, R.color.casiBlanco, null))
            }
        }


    }


    fun View.setButtonActiveYellow(active:Boolean, iconResource: Int?= null){

        val button = (this as MaterialButton)

        iconResource?.let {  ico ->
            //button.setCompoundDrawablesWithIntrinsicBounds(ico, 0, 0, 0)
            button.setIconResource(iconResource)
        }


        when (active) {
            true -> {
                button.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.yellow_light,
                        null
                    )
                )
            }
            false -> {
                button.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            }
        }


    }


    fun View.setButtonVisibilityIcon(active:Boolean){

        val button = (this as MaterialButton)

        val ico = when (active){
            true -> R.drawable.ic_visibility_on_24
            false -> R.drawable.ic_visibility_off_24
        }

        button.setIconResource(ico)



        when (active) {
            true -> {
                button.setIconTintResource(R.color.casiBlancoObscuro)
//                button.setBackgroundColor(
//                    ResourcesCompat.getColor(
//                        resources,
//                        R.color.casiBlanco,
//                        null
//                    )
//                )
            }
            false -> {
                button.setIconTintResource(R.color.casiRojo)
//                button.setBackgroundColor(
//                    ResourcesCompat.getColor(
//                        resources,
//                        R.color.casiRojo,
//                        null
//                    )
//                )
            }
        }


    }


    fun View.setButtonActive(active:Boolean,text: String?){

        val button = (this as MaterialButton)


        if (text != null) {
            button.text = text
        }

        when (active) {
            true -> {
                button.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.casiBlanco,
                        null
                    )
                )
            }
            false -> {
                button.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.yellow_light,
                        null
                    )
                )
            }
        }





    }


    fun View.itemRecyclerViewSeleccionado(seleccionado:Boolean){

        val tipoVehiculo = (this as ConstraintLayout)
        when (seleccionado) {
            true -> {
                tipoVehiculo.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.casiAmarillo,
                        null
                    )
                )
            }
            false -> {
                tipoVehiculo.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            }
        }

    }

    fun View.setVehiculoClimatizado(vehiculo: VehiculoResponse){

        val imageVehiculoClimatizado = (this as MaterialButton)
        when (vehiculo.climatizado) {
            true -> imageVehiculoClimatizado.visibility = View.VISIBLE
            false -> imageVehiculoClimatizado.visibility = View.GONE
            else -> imageVehiculoClimatizado.visibility = View.GONE
        }

    }

    fun View.setVehiculoMatricula(matricula: String?){
        val tvMatricula = (this as TextView)

        if (matricula.isNullOrEmpty()){
            tvMatricula.text = "Sin Matrícula"
        } else {
            tvMatricula.text = matricula
        }
    }

    fun View.setUsuarioVerificacionImage(usuario: Usuario){
        val imageUsuarioVerificado = (this as AppCompatImageView)

        //Verificacion de Usuario
        if (usuario.usuarioVerificacion?.verificado == true) {
            //Verificado OK
            imageUsuarioVerificado.visibility = View.VISIBLE
            imageUsuarioVerificado.setImageResource(R.drawable.ic_verified)
            imageUsuarioVerificado.drawable.setTint(ResourcesCompat.getColor(resources, R.color.colorSnackBarSuccess, null))

        } else {
            if (usuario.usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty() || usuario.usuarioVerificacion?.identificacion.isNullOrEmpty()) {
                //Pendiente
                imageUsuarioVerificado.visibility = View.VISIBLE
                imageUsuarioVerificado.setImageResource(R.drawable.ic_alert_24)
                imageUsuarioVerificado.drawable.setTint(ResourcesCompat.getColor(resources, R.color.colorSnackBarError, null))

            } else {
                //En Curso
                imageUsuarioVerificado.visibility = View.INVISIBLE
            }

        }

    }

    fun View.setUsuarioVerificacionButton(usuario: Usuario){

        val buttonUsuarioVerificacion = (this as MaterialButton)

        //Verificacion de Usuario
        if (usuario.usuarioVerificacion?.verificado == true) {
            //Verificado OK
            buttonUsuarioVerificacion.text = "Verificación\nCompletada"
            buttonUsuarioVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.casiBlanco, null))
            buttonUsuarioVerificacion.isClickable = false
        } else {
            if (usuario.usuarioVerificacion?.imagenIdentificaionURL.isNullOrEmpty() || usuario.usuarioVerificacion?.identificacion.isNullOrEmpty()) {
                //Pendiente
                buttonUsuarioVerificacion.text = "Verificación\nPendiente"
                buttonUsuarioVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.casiRojo, null))
                buttonUsuarioVerificacion.isClickable = true
            } else {
                //En Curso
                buttonUsuarioVerificacion.text = "Verificación\nEn Curso"
                buttonUsuarioVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.casiAmarillo, null))
            }

        }

    }

    fun View.setVehiculoVerificacionButton(vehiculo: VehiculoResponse){

        val buttonVehiculoVerificacion = (this as MaterialButton)



        //Verificacion de Vehiculo
        if (vehiculo.vehiculoVerificacion?.verificado == true) {
            //Verificado OK
            buttonVehiculoVerificacion.text = "Verificación\nCompletada"
            buttonVehiculoVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.white, null))
            buttonVehiculoVerificacion.isClickable = false
        } else {
            if (vehiculo.vehiculoVerificacion?.imagenCirculacionURL.isNullOrEmpty() ||
                vehiculo.vehiculoVerificacion?.matricula.isNullOrEmpty() ||
                vehiculo.vehiculoVerificacion?.circulacion.isNullOrEmpty()) {

                if (vehiculo.tipoVehiculo?.requiereVerification == true) {
                    //Pendiente
                    buttonVehiculoVerificacion.text = "Verificación\nPendiente"
                    buttonVehiculoVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.casiRojo, null))
                    buttonVehiculoVerificacion.isClickable = true

                } else {
                    //Opcional
                    buttonVehiculoVerificacion.text = "Verificación\nOpcional"
                    buttonVehiculoVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.white, null))
                    buttonVehiculoVerificacion.isClickable = true
                }



            } else {
                //En Curso
                buttonVehiculoVerificacion.text = "Verificación\nEn Curso"
                buttonVehiculoVerificacion.background.setTint(ResourcesCompat.getColor(resources, R.color.casiAmarillo, null))
            }

        }





    }

    fun View.setVehiculoVerificacionImage(vehiculo: VehiculoResponse){
        val imageVehiculoVerificado = (this as AppCompatImageView)

        if (vehiculo.tipoVehiculo?.requiereVerification == true) {
            //Verificacion de Vahiculo
            if (vehiculo.vehiculoVerificacion?.verificado == true) {
                //Verificado OK
                imageVehiculoVerificado.visibility = View.VISIBLE
                imageVehiculoVerificado.setImageResource(R.drawable.ic_verified)
                imageVehiculoVerificado.drawable.setTint(ResourcesCompat.getColor(this.resources, R.color.colorSnackBarSuccess, null))

            } else {

                if (vehiculo.vehiculoVerificacion?.imagenCirculacionURL.isNullOrEmpty() ||
                    vehiculo.vehiculoVerificacion?.circulacion.isNullOrEmpty() ||
                    vehiculo.vehiculoVerificacion?.matricula.isNullOrEmpty())
                {
                    //Pendiente
                    imageVehiculoVerificado.visibility = View.VISIBLE
                    imageVehiculoVerificado.setImageResource(R.drawable.ic_alert_24)
                    imageVehiculoVerificado.drawable.setTint(ResourcesCompat.getColor(this.resources, R.color.colorSnackBarError, null))
                } else {
                    //en Curso
                    imageVehiculoVerificado.visibility = View.INVISIBLE
                }

            }

        } else {
            //No requiere
            imageVehiculoVerificado.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    fun View.setDetallesUsuario(vehiculo: VehiculoResponse){
        val tvDetallesUsuario = (this as TextView)

        //Estado Actual
        val estado = when {
            vehiculo.disponible!! -> {
                "Disponible"
            } else -> {
                "Ocupado"
            }

        }
        //Datos Condutor
        val edad :String = UtilsGlobal.getAge(vehiculo.usuario?.fechaDeNacimiento!!).toString()
        tvDetallesUsuario.text = "$estado\n" +
                "${vehiculo.usuario?.provincia?.nombre ?: "Cuba"} \n" +
                "$edad años "


    }

    @SuppressLint("SetTextI18n")
    fun View.setDetallesVehiculos(vehiculo: VehiculoResponse){
        val tvDetalles = (this as TextView)
        //Cuando es solo Transporte de Pasajero
        if (vehiculo.tipoVehiculo?.transportePasajeros!! && !vehiculo.tipoVehiculo?.transporteCarga!!) {
            tvDetalles.text = "${vehiculo.tipoVehiculo?.tipoVehiculo ?: ""}\n" +
                    "${vehiculo.marca} " +
                    "${vehiculo.modelo} " +
                    "${vehiculo.ano} " +
                    "\n${vehiculo.capacidadPasajeros} Pasajeros" +
                    "\nEquipaje ${vehiculo.capacidadEquipaje} Kg"
        }

        //Cuando es solo Transporte de Carga
        if (vehiculo.tipoVehiculo?.transporteCarga!! && !vehiculo.tipoVehiculo?.transportePasajeros!!) {
            tvDetalles.text = "${vehiculo.tipoVehiculo?.tipoVehiculo ?: ""}\n" +
                    "${vehiculo.marca} " +
                    "${vehiculo.modelo} " +
                    "${vehiculo.ano} " +
                    "\n${vehiculo.capacidadCarga} Kg de Carga"
        }

        //Cuando es solo Transporte de Pasajero y Carga
        if (vehiculo.tipoVehiculo?.transporteCarga!! && vehiculo.tipoVehiculo?.transportePasajeros!!) {
            tvDetalles.text = "${vehiculo.tipoVehiculo?.tipoVehiculo ?: ""}\n" +
                    "${vehiculo.marca} " +
                    "${vehiculo.modelo} " +
                    "${vehiculo.ano} " +
                    "\n${vehiculo.capacidadPasajeros} Pasajeros"+
                    "\n${vehiculo.capacidadCarga} Kg de Carga"
        }


    }

    fun View.setButtonVehiculoActivo(activo:Boolean?) {

        val buttonVehiculoActivo = (this as MaterialButton)
        when (activo) {
            true -> {
                buttonVehiculoActivo.text = this.resources.getString(R.string.boton_vehiculo_activo_true)
                buttonVehiculoActivo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_vehicle_active_32, 0, 0)
                buttonVehiculoActivo.isEnabled = false
            }
            false -> {
                buttonVehiculoActivo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_vehicle_inactive_32, 0, 0)
                buttonVehiculoActivo.text = this.resources.getString(R.string.boton_vehiculo_activo_false)
                buttonVehiculoActivo.isEnabled = true
            }
            else -> {buttonVehiculoActivo.visibility = View.INVISIBLE}

        }

    }

    fun View.setActualizacionActiva(activo:Boolean) {

        val buttonVehiculoActivo = (this as MaterialButton)
        when (activo) {
            true -> {
                buttonVehiculoActivo.text = this.resources.getString(R.string.boton_actualizacion_activa_true)
                buttonVehiculoActivo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_vehicle_active_32, 0, 0)
                buttonVehiculoActivo.isEnabled = true
            }
            false -> {
                buttonVehiculoActivo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_vehicle_inactive_32, 0, 0)
                buttonVehiculoActivo.text = this.resources.getString(R.string.boton_actualizacion_activa_false)
                buttonVehiculoActivo.isEnabled = true
            }

        }

    }

    fun View.loadImagePerfilFromURL(relativeURL:String?) {
        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .fitCenter()
                .circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_user)
                .into((this as ImageView))

        }

    }

    fun View.loadImageVehiculoFrontalFromURLNoCache(relativeURL:String?) {

        (this as ImageView).setImageResource(R.drawable.ic_front_vehicle)

        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .fitCenter()
                .circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_front_vehicle)
                .into((this as ImageView))
        }

    }

    fun View.loadImagePerfilFromURLNoCache(relativeURL:String?) {

        (this as ImageView).setImageResource(R.drawable.ic_user)

        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .fitCenter()
                .circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_user)
                .into((this as ImageView))

        }

    }

    fun View.loadImageVehiculoCirculacionFromURLNoCache(relativeURL: String?) {

        (this as ImageView).setImageResource(R.drawable.ic_licencia_circulacion)

        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                //.load(Constants.URL_BASE +url)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .centerCrop()
                //.circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_licencia_circulacion)
                .into((this as ImageView))
        }


    }

    fun View.loadImageUserVerificacionFromURLNoCache(relativeURL: String?,conductor: Boolean?) {


        val placeholder = if (conductor == true) {
            (this as ImageView).setImageResource(R.drawable.ic_driver_verification)
            R.drawable.ic_driver_verification
        } else {
            (this as ImageView).setImageResource(R.drawable.ic_user_verification)
            R.drawable.ic_user_verification
        }

        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .centerCrop()
                //.circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(placeholder)
                .into((this as ImageView))

        }

    }

    fun View.loadImageToZoomFromURL(relativeURL:String?) {
        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .fitCenter()
                //.circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.blank_background)
                .into((this as ImageView))

        }

    }

    fun View.loadImagePerfilFromURI(uri: Uri?) {

        if (uri != null )  {

            Glide.with(this)
                .load(uri)
                .fitCenter()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user)
                .into((this as ImageView))

        }

    }

    fun View.loadImageVehiculoFrontalFromURL(relativeURL:String?) {
        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .fitCenter()
                .circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_front_vehicle)
                .into((this as ImageView))
        }

    }

    fun View.loadImageRedSocialFromURL(relativeURL:String?) {
        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .fitCenter()
                .circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_empty_social_network)
                .into((this as ImageView))
        }

    }

    fun View.loadImageVehiculoFrontalFromURI(uri: Uri?){

        //     if (uri != null )  {
        Glide.with(this)
            //.load(Constants.URL_BASE +url)
            .load(uri)
            .centerCrop()
            //.circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_front_vehicle)
            .into((this as ImageView))
        //    }

    }

    fun View.loadImageVehiculoCirculacionFromURI(uri: Uri?){

        //     if (uri != null )  {
        Glide.with(this)
            //.load(Constants.URL_BASE +url)
            .load(uri)
            .centerCrop()
            //.circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_licencia_circulacion)
            .into((this as ImageView))
        //    }

    }

    fun View.loadImageVehiculoCirculacionFromURL(relativeURL: String?) {


        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                //.load(Constants.URL_BASE +url)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .centerCrop()
                //.circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(R.drawable.ic_licencia_circulacion)
                .into((this as ImageView))
        }


    }

    fun View.loadImageUserVerificacionFromURI(uri: Uri?, conductor: Boolean?) {

        val placeholder = if (conductor == true) {
            R.drawable.ic_driver_verification
        } else {
            R.drawable.ic_user_verification
        }

        Glide.with(this)
            .load(uri)
            .centerCrop()
            //.circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(placeholder)
            .into((this as ImageView))


    }

    fun View.loadImageUserVerificacionFromURL(relativeURL: String?,conductor: Boolean?) {

        val placeholder = if (conductor == true) {
            R.drawable.ic_driver_verification
        } else {
            R.drawable.ic_user_verification
        }

        if (!relativeURL.isNullOrEmpty()  )  {

            Glide.with(this)
                .load(UtilsGlobal.getFullURL(relativeURL))
                .centerCrop()
                //.circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .placeholder(placeholder)
                .into((this as ImageView))

        }

    }


    private lateinit var textInputBinding: TextInputBinding
    fun Activity.showInputTextMessage(funResult: (okSelected:Boolean, returnText:String?)  -> Unit, title: String?=null, hint: String?=null, helperText:String?=null, message :String?=null, icon:Int?=null, buttonConfirmText:String?="Continuar" ){

        val builder = MaterialAlertDialogBuilder(this).apply {

            textInputBinding = TextInputBinding.inflate(layoutInflater).apply {
                hint?.let {
                    this.textInputLayout.hint = it
                }
                helperText?.let {
                    this.textInputLayout.helperText = it
                }
                message?.let {
                    this.textInputLayout.editText?.setText(it)
                }
                icon?.let {
                    this.textInputLayout.setStartIconDrawable(it)}

            }

            this.setView(textInputBinding.root)

            title?.let {
               this.setTitle(it)
            }

            this.setPositiveButton(buttonConfirmText, DialogInterface.OnClickListener { _, _ ->
                    val text = textInputBinding.textInputLayout.editText?.text.toString().trim()
                    funResult(true,text)

                })
            this.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })



        }

        builder.show()

    }
    fun Context.showMessageDialogForResult( funResult: (okSelected:Boolean)  -> Unit   ,title: String?=null, message :String?=null, icon:Int?=null) {

        val alertDialog = AlertDialog.Builder(this).apply {
            icon?.let {
                this.setIcon(it)
            }
            title?.let {
                this.setTitle(it)
            }
            message?.let {
                this.setMessage(message)
            }
            this.setNegativeButton("Cancelar"){ dialogInterface, _ ->
                dialogInterface.cancel()
                funResult(false)

            }
            this.setPositiveButton("Continuar"){ dialogInterface , a ->
                dialogInterface.dismiss()
                funResult(true)
            }

            }.create()


        alertDialog.setCancelable(false)
        alertDialog.show()

    }



    fun Activity.showDialogYearPicker(funResult: (yearSelected:String)  -> Unit,initialYear:Int? = null) {


        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        val inflater = this.layoutInflater

        val cal = Calendar.getInstance()

        val dialog = inflater.inflate(R.layout.year_picker_dialog, null)
        val monthPicker = dialog.findViewById(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal.get(Calendar.MONTH) + 1

        val year = initialYear ?: cal.get(Calendar.YEAR)

        yearPicker.minValue = 1900
        yearPicker.maxValue = getCurrentYear()

        yearPicker.value = year

        builder.setView(dialog).setPositiveButton(Html.fromHtml("<font color='#FF4081'>Ok</font>")){ dialogInterface, which ->
            val value = yearPicker.value
            funResult(value.toString())
            dialogInterface.cancel()
        }

        builder.setNegativeButton(Html.fromHtml("<font color='#FF4081'>Cancel</font>")){ dialogInterface, which ->
            dialogInterface.cancel()
        }

        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }







}














/*



    private lateinit var mProgressDialog: Dialog
    private lateinit var ProgressDialogBinding: DialogProgressBinding

    fun showProgressDialog(message: String) {

        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        //mProgressDialog.setContentView(R.layout.dialog_progress)
        ProgressDialogBinding = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(ProgressDialogBinding.root)

        ProgressDialogBinding.tvProgressText.text = message

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        try {
            mProgressDialog.dismiss()
        } catch (e: Exception) {

        }

    }
 */