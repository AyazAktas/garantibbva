package com.example.garantibbva.data.entity

import java.io.Serializable

data class Transaction(
    var transactionId: String? = "",      // İşlem ID'si
    var senderIban: String? = "",         // Gönderenin IBAN numarası
    var receiverIban: String? = "",       // Alıcının IBAN numarası
    var amount: Double? = 0.0,            // Transfer edilen tutar
    var date: String? = "",               // İşlem tarihi
    var description: String? = "",        // İşlem açıklaması
    var senderName: String? = "",         // Gönderenin adı
    var receiverName: String? = "",// Alıcının adı
    var senderId: String?
) : Serializable
