package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Transaction
import com.example.garantibbva.data.repository.CustomerRepository
import com.example.garantibbva.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CostumerPageViewModel @Inject constructor(var transactionRepository: TransactionRepository): ViewModel() {
    suspend fun getCustomerTransactions(customerId: String, customerIban: String): List<Transaction> {
        return transactionRepository.getCustomerTransactions(customerId, customerIban)
    }

}