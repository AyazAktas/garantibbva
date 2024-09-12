package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Transaction
import com.example.garantibbva.data.repository.TransactionRepository
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
    fun cancelTransfer(transactionId: String){
        return transactionRepository.cancelTransfer(transactionId)
    }
    fun makeTransfer(iban: String, amount: Double?, senderId:String,totalAmount:Double){
        return transactionRepository.makeTransfer(iban, amount, senderId,totalAmount)
    }
}
