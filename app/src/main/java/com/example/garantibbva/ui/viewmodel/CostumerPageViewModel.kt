package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CostumerPageViewModel @Inject constructor(var customerRepository: CustomerRepository)    : ViewModel() {
}