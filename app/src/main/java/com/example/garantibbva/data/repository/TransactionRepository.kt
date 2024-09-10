package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.TransactionDataSource
import com.example.garantibbva.data.entity.Transaction


class TransactionRepository(private val transactionDataSource: TransactionDataSource) {
    suspend fun getReceiverNameByIban(iban: String)=transactionDataSource.getReceiverNameByIban(iban)
    suspend fun saveTransaction(transaction: Transaction)=transactionDataSource.saveTransaction(transaction)
}
