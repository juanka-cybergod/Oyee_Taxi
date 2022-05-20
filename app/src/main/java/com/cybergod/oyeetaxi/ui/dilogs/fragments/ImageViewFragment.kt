package com.cybergod.oyeetaxi.ui.dilogs.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cybergod.oyeetaxi.databinding.DialogImageViewBinding
import com.cybergod.oyeetaxi.ui.dilogs.viewmodel.ValoracionViewModel
import com.cybergod.oyeetaxi.ui.utils.UtilsUI.loadImageToZoomFromURL
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImageViewFragment : DialogFragment() {

    private var _binding: DialogImageViewBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ValoracionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogImageViewBinding.inflate(inflater, container, false)


        //Obtener el Imagen por Argumentos
        requireArguments().getString("imageURL")?.let { imageURL ->
            binding.imageView.loadImageToZoomFromURL(imageURL)
        }




        return  binding.root

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Full Screen DialogFragment in Android
         */
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

        /** OR
        setStyle(DialogFragment.STYLE_NORMAL,
        android.R.style.Theme_Black_NoTitleBar_Fullscreen);
         */
    }
}