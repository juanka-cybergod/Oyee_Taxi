package com.cybergod.oyeetaxi.ui.preferences.viewmodel


import com.cybergod.oyeetaxi.api.repository.ConfigurationRepository
import com.cybergod.oyeetaxi.data_storage.DataStorageRepository
import com.cybergod.oyeetaxi.ui.main.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AdministrationViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val dataStorageRepository: DataStorageRepository,
) : BaseViewModel() {





}
