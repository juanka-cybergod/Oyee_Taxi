package com.cybergod.oyeetaxi.ui.controlPanel.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.file.model.types.TipoFichero
import com.cybergod.oyeetaxi.api.futures.user.model.Usuario
import com.cybergod.oyeetaxi.databinding.UserControlPanelFragmentMainBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.UserControlPanelViewModel
import com.cybergod.oyeetaxi.ui.controlPanel.viewmodel.VehicleControlPanelViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.vehicleRegistration.activity.VehicleRegistrationActivity
import com.cybergod.oyeetaxi.utils.Constants.KEY_VEHICLE_PARCELABLE
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentUserActive
import com.cybergod.oyeetaxi.utils.GlobalVariables.currentVehicleActive
import com.cybergod.oyeetaxi.utils.UtilsGlobal.getAge
import com.cybergod.oyeetaxi.utils.UtilsGlobal.timePassedFromDateString
import com.cybergod.oyeetaxi.utils.FileManager.prepareImageCompressAndGetFile
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImagePerfilFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageVehiculoFrontalFromURL
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setDetallesVehiculos
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setUsuarioVerificacionButton
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setUsuarioVerificacionImage
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoClimatizado
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoMatricula
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setVehiculoVerificacionImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ControlPanelFragmentMain : BaseFragment() {



    private var _binding: UserControlPanelFragmentMainBinding? = null
    private val binding get() = _binding!!


    private val userControlViewModel: UserControlPanelViewModel by activityViewModels()
    private val vehicleControlViewModel: VehicleControlPanelViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UserControlPanelFragmentMainBinding.inflate(inflater, container, false)


        setupObservers()

        setupOnClickListener()


        return  binding.root
    }

    private fun setupObservers() {

        setupUserDetailsObserver()

        setupVehicleDetailsObserver()

        setupUserUpdatedSussesObserver()

        setupVehicleUpdatedSussesObserver()

        setupImagenPerfilURLUpdatedSussessObserver()

       // setupImagenVehiculoURLUpdatedSussessObserver()

    }


    private fun goToProvincesFragment(){
        findNavController().navigate(R.id.action_userControlPanelFragmentMain_to_provincesFragment3)
    }

    private fun setupOnClickListener(){

        binding.buttonSolicitarModoCondutor.setOnClickListener {
            launchVehicleRegisterActivity()
        }

        binding.imageViewSelect.setOnClickListener {
            openImageChooser(SelectImageFor.ImagenPerfil)
        }

        binding.imageViewSelect2.setOnClickListener {
            openImageChooser(SelectImageFor.ImagenFrontalVehiculo)
        }

        binding.buttonEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_userControlPanelFragmentMain_to_userControlPanelFragmentEditProfile,Bundle().apply {
                //putString(Constants.KEY_VEHICLE_ID,vehicleId)
            })

        }

        binding.buttonUsuarioVerificacion.setOnClickListener {
            findNavController().navigate(R.id.action_userControlPanelFragmentMain_to_userControlPanelFragmentEditVerification,Bundle().apply {
                //putString(Constants.KEY_VEHICLE_ID,vehicleId)
            })
        }

        binding.buttonSeguridad.setOnClickListener {
            findNavController().navigate(R.id.action_userControlPanelFragmentMain_to_userControlPanelFragmentEditSecurity,Bundle().apply {
                //putString(Constants.KEY_VEHICLE_ID,vehicleId)
            })

        }

        binding.buttonProvincia.setOnClickListener {
            goToProvincesFragment()
        }

        binding.buttonModoConductor.setOnClickListener {
            binding.buttonModoPasajero.isChecked = true
                showUserModeDialog(true)
        }

        binding.buttonModoPasajero.setOnClickListener {
            binding.buttonModoConductor.isChecked = true
                showUserModeDialog(false)
        }

        binding.buttonEditarVehiculo.setOnClickListener {

            if (!currentVehicleActive.value?.id.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_userControlPanelFragmentMain_to_vehicleControlPanelFragmentEdit,Bundle().apply {
                    putParcelable(KEY_VEHICLE_PARCELABLE, currentVehicleActive.value)
                })
            } else {
                showSnackBar(
                    getString(R.string.no_vehicles_actives),
                    true
                )
            }



        }

        binding.buttonAdministrarVehiculos.setOnClickListener {
            findNavController().navigate(R.id.action_userControlPanelFragmentMain_to_vehicleListControlPanelFragment,Bundle().apply {
                //putString(Constants.KEY_VEHICLE_ID,vehicleId)
            })
        }

    }

    enum class SelectImageFor{
        ImagenPerfil,ImagenFrontalVehiculo
    }
    private lateinit var selectImageFor :SelectImageFor

    private fun openImageChooser(imageFor: SelectImageFor) {
        requireView().hideKeyboard()
        selectImageFor = imageFor
        startActivityForResult.launch("image/jpeg")
    }

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            it?.let { uri ->

                if (selectImageFor == SelectImageFor.ImagenPerfil) {

                    (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))

                    userControlViewModel.uploadFile(
                        file =  requireContext().prepareImageCompressAndGetFile(uri),
                        id = currentUserActive.value?.id!!,
                        tipoFichero = TipoFichero.USUARIO_PERFIL

                    )
                }

        }
        }
    )



    private fun showUserModeDialog(modoConductor:Boolean) {

        var title  = "Activar el modo "
        val text :String
        val icon:Int
        if (modoConductor) {
            icon = R.drawable.ic_taxi
            title += "Conductor"
            text = "Al usar el modo \"Condutor\" su vehículo activo sera visible en el mapa para todos los pasajeros y podra recibir todas las solicitudes de viajes. Podrá volver al modo Pasajero en cuaquier momento"
        } else {
            icon = R.drawable.ic_person_32
            title += "Pasajero"
            text = "Al usar el modo \"Pasajero\" su vehiculo dejará de ser visible en el mapa y podrá realizar solicitudes de viajes a otros condutores. Podrá volver al modo Condutor en cuaquier momento"
        }

        val builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(title)
            .setMessage(text)
            .setIcon(icon)
            .setPositiveButton("Continuar"){ dialogInterface , _ ->
                dialogInterface.dismiss()
                changeUserMode(modoConductor)
            }
            .setNegativeButton("Cancelar"){ dialogInterface, _ ->
                dialogInterface.cancel()



            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    private fun changeUserMode(modeConductor:Boolean){
        (requireActivity() as BaseActivity).showProgressDialog(getString(R.string.updating_user))
        userControlViewModel.updateUser(Usuario(modoCondutor = modeConductor))
    }


    private fun setupUserUpdatedSussesObserver(){
        userControlViewModel.userUpdatedSusses.observe(viewLifecycleOwner, Observer { updatedOK ->

           (requireActivity() as BaseActivity).hideProgressDialog()

           if (updatedOK != null){

               if (updatedOK) {

                   userControlViewModel.updateCurrentUserAndActiveVehicleGlobalVariables()

               } else {
                   showSnackBar(
                       getString(R.string.user_update_fail),
                       true
                   )
               }

           } else {
               showSnackBar(
                   getString(R.string.fail_server_comunication),
                   true,
               )
           }



        })
    }


    private fun setupVehicleUpdatedSussesObserver(){
        vehicleControlViewModel.vehicleUpdatedSusses.observe(viewLifecycleOwner, Observer { updatedOK ->

            (requireActivity() as BaseActivity).hideProgressDialog()

            if (updatedOK != null){

                if (updatedOK) {

                    vehicleControlViewModel.updateCurrentUserAndActiveVehicleGlobalVariables()


                } else {
                    showSnackBar(
                        getString(R.string.error_al_actualizar_vehiculo),
                        true
                    )
                }

            } else {
                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true,
                )
            }



        })
    }

    private fun setupImagenPerfilURLUpdatedSussessObserver(){

        userControlViewModel.imagenPerfilURL.observe(viewLifecycleOwner, Observer { imagenPerfilURL ->
            if (!imagenPerfilURL.isNullOrEmpty()) {
                userControlViewModel.updateUser(
                    Usuario(imagenPerfilURL = imagenPerfilURL)
                )
            } else {

                (requireActivity() as BaseActivity).hideProgressDialog()

                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )


            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun setupVehicleDetailsObserver(){

        currentVehicleActive.observe(viewLifecycleOwner, Observer { vehicleDetails ->

            if (vehicleDetails!= null) {

                binding.clVehiculoActivo.visibility = View.VISIBLE

                vehicleDetails.let { vehiculo ->

                    //Imagen Frontal
                    binding.imageVehiculo.loadImageVehiculoFrontalFromURL(currentVehicleActive.value?.imagenFrontalPublicaURL)

                    //Verificacion
                    binding.imageVehiculoVerificado.setVehiculoVerificacionImage(vehiculo)

                    //Matricula
                    binding.tvMatricula.setVehiculoMatricula(vehiculo)

                    //Detalles del Vehiculo
                    binding.tvDetalles.setDetallesVehiculos(vehiculo)

                    //Climatizado
                    binding.imageVehiculoClimatizado.setVehiculoClimatizado(vehiculo)

                    //Btn Climatizado
                    binding.imageVehiculoClimatizado.setOnClickListener {
                        Toast.makeText(requireContext(),"Climatizado",Toast.LENGTH_LONG).show()
                        it.isEnabled=false
                    }

                }
            } else {
               binding.clVehiculoActivo.visibility = View.INVISIBLE
            }




        })


    }


    @SuppressLint("SetTextI18n")
    private fun setupUserDetailsObserver() {
        currentUserActive.observe(viewLifecycleOwner, Observer { userDetails ->


            userDetails?.let { usuario ->

                with(binding) {

                    imagePerfil.loadImagePerfilFromURL(currentUserActive.value?.imagenPerfilURL)

                    imageUsuarioVerificado.setUsuarioVerificacionImage(usuario)
                    buttonUsuarioVerificacion.setUsuarioVerificacionButton(usuario)

                    tvNombreUsuario.text = "${usuario.nombre} ${usuario.apellidos}"
                    tvTelefonoMovil.text = usuario.telefonoMovil
                    userRatingBar.rating = usuario.valoracion?:0f

                    var tipoUsuario  = "Registrado como"
                    usuario.administrador?.let { if (it) {tipoUsuario = "Administrador"} }
                    usuario.superAdministrador?.let { if (it) {tipoUsuario = "Super Administrador"} }

                    tvEdad.text = "Edad ${getAge(usuario.fechaDeNacimiento!!)} años"
                    tvAntiguedad.text = timePassedFromDateString(usuario.fechaDeRegistro!!)
                    buttonProvincia.text = "Provincia\n${usuario.provincia?.nombre}"

                    tvTipoUsuario.text = tipoUsuario
                    if (usuario.conductor == true) {
                        tvCondutorOPasajero.text = "Conductor"
                        buttonSolicitarModoCondutor.visibility = View.GONE
                        clDriverPanel.visibility = View.VISIBLE

                        usuario.modoCondutor?.let {
                            if (it) {
                                buttonModoConductor.isClickable = false
                                buttonModoConductor.isChecked = true
                                buttonModoPasajero.isChecked = false
                                buttonModoPasajero.isClickable = true
                            } else {
                                buttonModoConductor.isClickable = true
                                buttonModoConductor.isChecked = false
                                buttonModoPasajero.isClickable = false
                                buttonModoPasajero.isChecked = true
                            }
                        }


                    } else {
                        tvCondutorOPasajero.text = "Pasajero"
                        buttonSolicitarModoCondutor.visibility = View.VISIBLE
                        clDriverPanel.visibility = View.GONE
                    }



                }

            }

        })


    }




    private fun launchVehicleRegisterActivity() {
        lifecycleScope.launch {
            startActivity(
                Intent(requireActivity(),
                    VehicleRegistrationActivity::class.java)
            )
        }
    }





}