package com.example.garantibbva.data.entity

import java.io.Serializable

data class Corp(
    var corpId: String? = "", // Kurumsal müşteri ID'si
    var corpAccountNo:String?="",
    var corpName: String? = "",              // Şirket adı
    var taxIdNumber: String? = "",           // Vergi kimlik numarası
    var registrationNumber: String? = "",    // Ticaret sicil numarası
    var address: String? = "",               // Şirket adresi
    var corpPhoneNumber: String? = "",           // Şirket telefon numarası
    var corpEmail: String? = "",                 // Şirket e-posta adresi
    var contactPersonName: String? = "",     // İlgili kişi adı
    var contactPersonPhone: String? = "",    // İlgili kişinin telefon numarası
    var contactPersonEmail: String? = "",    // İlgili kişinin e-posta adresi
    var industry: String? = "",              // Şirketin faaliyet gösterdiği sektör
    var contactPersonTc: String? = "",       // Kullanıcı ID'si
    var contactPersonsCustomerNo: String? = "",        // Kullanıcı müşteri numarası
    var accountType: String?="", // Hesap türü (ana hesap, maaş hesabı vb.)
    var dateOfIncorporation: String? = "",   // Şirket kuruluş tarihi
    var annualRevenue: Double? = 0.0,        // Yıllık gelir (ciro)
    var numberOfEmployees: Int? = 0,         // Çalışan sayısı
    var accountBalance: Double? = 0.0,
    var accountPurpose: String?="",
    var iban: String? = ""                   // IBAN numarası
):Serializable{}
