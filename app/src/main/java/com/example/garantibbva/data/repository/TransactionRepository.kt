package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.TransactionDataSource
import com.example.garantibbva.data.entity.Transaction

class TransactionRepository(private val transactionDataSource: TransactionDataSource) {

    suspend fun addTransaction(transaction: Transaction) {
        transactionDataSource.addTransaction(transaction)
    }

    suspend fun getTransactions(): List<Transaction> {
        return transactionDataSource.getTransactions()
    }

    suspend fun getTransactionById(transactionId: String): Transaction? {
        return transactionDataSource.getTransactionById(transactionId)
    }

    suspend fun transferMoney(
        sender: Any,
        receiver: Any,
        amount: Double,
        description: String
    ): Boolean {
        return transactionDataSource.transferMoney(sender, receiver, amount, description)
    }


}
