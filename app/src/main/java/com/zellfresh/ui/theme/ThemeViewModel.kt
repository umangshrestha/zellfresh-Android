package com.zellfresh.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zellfresh.ui.store.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThemeViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository) :
    ViewModel() {
    val isDarkTheme = dataStoreRepository.isDarkTheme

    fun setTheme(value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setTheme(value)
        }
    }
}