package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageViewModel: ViewModel() {
    val customerRepository=CustomerRepository()
    var customerList= MutableLiveData<List<Customer>>()
    init {
        customerInit()
    }
    fun customerInit(){
        CoroutineScope(Dispatchers.Main).launch {
            customerList.value=customerRepository.customerInit()
        }
    }
}