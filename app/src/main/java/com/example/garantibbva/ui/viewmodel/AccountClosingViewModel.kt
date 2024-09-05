package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class AccountClosingViewModel @Inject constructor(
    private val corpRepository: CorpRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {

   suspend fun isCorpsPasswordCorrect(corp: Corp?, enteredPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            corp?.let {
                corpRepository.isCorpsPasswordCorrect(it, enteredPassword)
            } ?: false
        }
    }

    suspend fun isCustomersPasswordCorrect(customer: Customer?, enteredPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            customer?.let {
                customerRepository.isCustomersPasswordCorrect(it, enteredPassword)
            } ?: false
        }
    }
}
