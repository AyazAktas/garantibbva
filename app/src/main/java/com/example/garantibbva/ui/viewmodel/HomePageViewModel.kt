package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CustomerRepository
import com.example.garantibbva.ui.fragment.CustomerPageFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomePageViewModel @Inject constructor(var customerRepository: CustomerRepository)    : ViewModel() {
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