package com.example.garantibbva.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Transaction
import com.example.garantibbva.data.repository.TransactionRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IbanTransferViewModel @Inject constructor(var transactionRepository: TransactionRepository) : ViewModel() {
    suspend fun getReceiverNameByIban(iban: String):String{
        return transactionRepository.getReceiverNameByIban(iban)
    }
    suspend fun saveTransaction(transaction: Transaction){
        return transactionRepository.saveTransaction(transaction)
    }
}
