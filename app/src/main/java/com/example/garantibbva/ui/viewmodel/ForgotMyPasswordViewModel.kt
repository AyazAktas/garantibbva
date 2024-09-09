package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ForgotMyPasswordViewModel@Inject constructor(var customerRepository: CustomerRepository,var corpRepository: CorpRepository) : ViewModel()  {
    suspend fun passwordValidation(enteredCustomerNo:String,enteredTC:String): Customer?{
        return customerRepository.passwordValidation(enteredCustomerNo, enteredTC)
    }

    suspend fun updatePassword(customerId:String,customerPassword:String){
        return customerRepository.updatePassword(customerId, customerPassword)
    }



    suspend fun updateCorpPassword(corpId:String,corpPassword:String){
        return corpRepository.updateCorpPassword(corpId, corpPassword)
    }


    suspend fun passwordValidationCorp(enteredCorpCustomerNo:String,enteredCorpTC:String): Corp?{
        return corpRepository.passwordValidationCorp(enteredCorpCustomerNo, enteredCorpTC)
    }
}