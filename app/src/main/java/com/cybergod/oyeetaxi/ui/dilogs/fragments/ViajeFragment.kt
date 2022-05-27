package com.cybergod.oyeetaxi.ui.dilogs.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.api.futures.travel.model.MetodoPago
import com.cybergod.oyeetaxi.databinding.FragmentViajeBinding
import com.cybergod.oyeetaxi.ui.dilogs.adapters.ImageVehicleTypeListAdapter
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.ViajeViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.hideKeyboard
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.showSnackBar
import com.cybergod.oyeetaxi.utils.DatePickerFragment
import com.cybergod.oyeetaxi.utils.TimePickerFragment
import com.cybergod.oyeetaxi.utils.UtilsGlobal.onDateSelected
import com.cybergod.oyeetaxi.utils.UtilsGlobal.onTimeSelected
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonActive
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setButtonActiveYellow
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.setMetodoPagoButton
import com.cybergod.oyeetaxi.utils.UtilsGlobal.isEmptyTrim
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ViajeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentViajeBinding? = null
    private val binding get() = _binding!!

    val viewModel: ViajeViewModel by activityViewModels()


    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: ImageVehicleTypeListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflar la Vista
        _binding = FragmentViajeBinding.inflate(inflater, container, false)



        //Inicializa el ReciclerView
        initRecyclerView()

        //Prepara los Observers
        setupObservers()

        //Preparalos OnclickListener
        setupOnLickListeners()


        //Escucha el Click sobre el Boton Para Cerrar el Dialogo
        binding.btnOK.setOnClickListener {
            dismiss()
        }


        return  binding.root
    }




    private fun setupOnLickListeners() {


        binding.btnViajarAhora.setOnClickListener {
            viewModel.viajeAgendado.postValue(null)
            viewModel.fecha.postValue(null)
            viewModel.hora.postValue(null)
        }


        binding.btnViajarAgendado.setOnClickListener {

            requireView().hideKeyboard()
            val datePicker: DatePickerFragment = DatePickerFragment({ day, month, year -> viewModel.fecha.value = onDateSelected(day, month, year)},viewModel.fecha.value,false,true)
            datePicker
                .show(parentFragmentManager,"datePicker")


        }

        binding.btnCantidadPasajeros.setOnClickListener {
            val dialogPersonas = PersonasSliderFragment()
            dialogPersonas.show(requireActivity().supportFragmentManager,"personasSliderFragment")
        }

        binding.btnMetodoPago.setOnClickListener {
            when (viewModel.metodoPago.value) {
                MetodoPago.EFECTIVO -> {
                    viewModel.metodoPago.postValue(MetodoPago.TARJETA)
                }
                MetodoPago.TARJETA -> {
                    viewModel.metodoPago.postValue(MetodoPago.EFECTIVO)
                }
                else -> {viewModel.metodoPago.postValue(MetodoPago.EFECTIVO)}
            }

        }

        binding.btnClimatizado.setOnClickListener {
            viewModel.requiereClimatizado.value = !viewModel.requiereClimatizado.value!!
            when (viewModel.requiereClimatizado.value){
                true -> Toast.makeText(requireContext(),"Requiero vehículo climatizado",Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(requireContext(),"No Requiero vehículo climatizado",Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }


        binding.btnIdaYRegreso.setOnClickListener {
            viewModel.viajeSoloIda.value = !viewModel.viajeSoloIda.value!!
            when (viewModel.viajeSoloIda.value){
                true -> Toast.makeText(requireContext(),"Viaje de solo ida",Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(requireContext(),"Viaje de Ida y Regreso",Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }

        binding.btnDetallesAdicionales.setOnClickListener {
            showDialogDetallseAdicionales()
        }


        binding.btnEquipaje.setOnClickListener {
            showDialogEquipaje()
        }

    }

    private fun setupObservers() {

        setupVehicleTypesObserver()

        setupViajeAgendadoAhoraObserver()

        setupMetodoPagoObserver()

        setupCantidadPasajerosObserver()

        setupRequiereClimatizadoObserver()

        setupDetallesAdicionalesObserver()

        setupPesoEquipajeObserver()

        setupViajeIdaYRegresoObserver()

    }
    private fun setupCantidadPasajerosObserver() {
        viewModel.cantidadPasajeros.observe(viewLifecycleOwner, Observer {
            if (it <= 1) {
                binding.btnCantidadPasajeros.setText("Pasajeros \nSolo Yo")
            } else {
                binding.btnCantidadPasajeros.setText("Pasajeros \n${it} Personas")
            }

        })

    }

    private fun setupViajeAgendadoAhoraObserver() {
        viewModel.viajeAgendado.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.btnViajarAhora.setButtonActive(true,null)
                binding.btnViajarAgendado.setButtonActive(false,getString(R.string.agendar_mi_viaje))
            } else {
                binding.btnViajarAhora.setButtonActive(false,null)
                binding.btnViajarAgendado.setButtonActive(true,getString(R.string.agendado)+ it)
            }

        })

        viewModel.fecha.observe(viewLifecycleOwner, Observer { fechaSeleccionada ->
            if (!fechaSeleccionada.isNullOrEmpty()) {

                val timePicker: TimePickerFragment = TimePickerFragment({ hour, minute -> viewModel.hora.value = onTimeSelected(hour, minute)},viewModel.hora.value)
                timePicker
                    .show(parentFragmentManager,"timePicker")

            }
        })

        viewModel.hora.observe(viewLifecycleOwner, Observer { horaSeleccionada ->
            if (!horaSeleccionada.isNullOrEmpty()){
                viewModel.viajeAgendado.postValue("  ${viewModel.fecha.value} ${viewModel.hora.value} ")
                viewModel.fecha.value = null
                viewModel.hora.value = null
            }
        })

    }

    private fun setupMetodoPagoObserver() {
        viewModel.metodoPago.observe(viewLifecycleOwner, Observer { metodoPago ->
            if (metodoPago != null) {
                binding.btnMetodoPago.setMetodoPagoButton(metodoPago)
            }
        })
    }

    private fun setupRequiereClimatizadoObserver(){
        viewModel.requiereClimatizado.observe(viewLifecycleOwner, Observer { requiereClimatizado ->
            if (requiereClimatizado != null) {
                binding.btnClimatizado.setButtonActiveYellow(requiereClimatizado)

            }

        })

    }

    private fun setupDetallesAdicionalesObserver(){
        viewModel.detallesAdicionales.observe(viewLifecycleOwner, Observer { detallesAdicionales ->
            if (!detallesAdicionales.isNullOrEmpty()) {
                binding.btnDetallesAdicionales.setButtonActiveYellow(true)
            } else  {
                binding.btnDetallesAdicionales.setButtonActiveYellow(false)
            }

        })

    }

    private fun setupPesoEquipajeObserver(){
        viewModel.pesoEquipaje.observe(viewLifecycleOwner, Observer { pesoEquipaje ->
            if (!pesoEquipaje.isNullOrEmpty()) {
                binding.btnEquipaje.setButtonActiveYellow(true)
            } else  {
                binding.btnEquipaje.setButtonActiveYellow(false)
            }

        })

    }

    private fun setupViajeIdaYRegresoObserver(){
        viewModel.viajeSoloIda.observe(viewLifecycleOwner, Observer { viajeSoloIda ->
            if (viajeSoloIda != null) {

                val ico = when (viajeSoloIda){
                    true -> R.drawable.ic_ida
                    false -> R.drawable.ic_ida_y_regreso
                }
                binding.btnIdaYRegreso.setButtonActiveYellow(!viajeSoloIda, ico)

            }

        })

    }


    private fun initRecyclerView(){

        // Recyclerview
        recyclerViewAdapter = ImageVehicleTypeListAdapter(this)
        recyclerView = binding.recylerViewTiposVehiculos
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)


    }

    private fun setupVehicleTypesObserver(){

        //Observar vehicleList
        //Otro Metodo ->viewModel.getAllVehiclesObserver()(this, Observer {
        viewModel.tipoVehiculoList.observe(viewLifecycleOwner, Observer {

            binding.animationView.visibility = View.VISIBLE

            if (it != null) {

                binding.animationView.visibility = View.INVISIBLE

                if (it.isNotEmpty()) {

                    it.plus(it)
                    recyclerViewAdapter.setTipoVehiculosList(it)
                    recyclerViewAdapter.notifyDataSetChanged()

                    binding.animationView.visibility = View.INVISIBLE


                } else {

                    dismiss()

                    requireActivity().showSnackBar(
                        getString(R.string.no_vehicles_type_available),
                        false
                    )

                }


            } else {
                dismiss()

                requireActivity().showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )
            }

        })

        lifecycleScope.launch {

           // delay(1000)
            viewModel.getAllVehicleTypes()

        }

    }




    private fun showDialogDetallseAdicionales(){



        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Detalles adicionales")

        val input = TextInputEditText(requireContext())

        input.textSize = 14f
        builder.setIcon(R.drawable.ic_note)


        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Detalles del viaje que debería conocer su condutor")

        viewModel.detallesAdicionales.value?.let {
            input.setText(it)
        }

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        // Set up the buttons
        //builder.setPositiveButton("Enviar", DialogInterface.OnClickListener { dialog, which ->
        builder.setPositiveButton("Aplicar", DialogInterface.OnClickListener { _, _ ->

            val text = input.text.toString()
            if (!text.isEmptyTrim()) {
                viewModel.detallesAdicionales.postValue(text)
            } else {
                viewModel.detallesAdicionales.postValue(null)
            }


        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        builder.setNeutralButton("No Usar", DialogInterface.OnClickListener { dialog, _ ->
            viewModel.detallesAdicionales.postValue(null)
            dialog.cancel()
           })
        builder.show()
    }

    private fun showDialogEquipaje(){



        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Peso del equipaje Kg")

        val input = TextInputEditText(requireContext())

        input.textSize = 16f
        builder.setIcon(R.drawable.ic_equipaje)


        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Peso aproximado de su equipaje en Kg")

        viewModel.pesoEquipaje.value?.let {
            input.setText(it)
        }

        input.inputType = InputType.TYPE_CLASS_NUMBER


        builder.setView(input)
        // Set up the buttons
        //builder.setPositiveButton("Enviar", DialogInterface.OnClickListener { dialog, which ->
        builder.setPositiveButton("Aplicar", DialogInterface.OnClickListener { _, _ ->

            val text = input.text.toString()
            if (!text.isEmptyTrim()) {
                viewModel.pesoEquipaje.postValue(text)
            } else {
                viewModel.pesoEquipaje.postValue(null)
            }


        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        builder.setNeutralButton("Sin Equipaje", DialogInterface.OnClickListener { dialog, _ ->
            viewModel.pesoEquipaje.postValue(null)
            dialog.cancel()
        })
        builder.show()
    }


    override fun dismiss() {
        super.dismiss()
    }


}