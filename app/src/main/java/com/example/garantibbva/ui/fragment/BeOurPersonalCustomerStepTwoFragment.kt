package com.example.garantibbva.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurPersonalCustomerStepTwoBinding
import com.example.garantibbva.ui.viewmodel.PersonalCustomerRegisterViewModel
import com.example.garantibbva.ui.viewmodel.PersonalRegistrationStepTwoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeOurPersonalCustomerStepTwoFragment : Fragment() {
    private lateinit var binding: FragmentBeOurPersonalCustomerStepTwoBinding
    private val registerViewModel: PersonalRegistrationStepTwoViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_be_our_personal_customer_step_two,container,false)
        binding.beOurCustomerStepTwoFragment=this
        val bundle:BeOurPersonalCustomerStepTwoFragmentArgs by navArgs()
        val newCustomerInfo=bundle.customer
        binding.customer=newCustomerInfo
        binding.editTextCustomerNo.setText(newCustomerInfo.customerNo)
        binding.toolbarImage.setOnClickListener {
            Snackbar.make(it," İşlemi iptal etmek istediğinizden emin misiniz?",Snackbar.LENGTH_SHORT)
                .setAction("Evet"){
                    registerViewModel.cancelRegistration(newCustomerInfo.customerId.toString())
                    findNavController().navigate(R.id.action_beOurPersonalCustomerStepTwoFragment_to_beOurCustomerFragment)
                }
                .show()
        }


        return binding.root
    }


    fun buttonSaveCustomer(customerId:String,customerName: String, customerTc: String, customerBirthDate: String, customerPhoneNumber: String, customerPassword: String, accountLocation: String){
        registerViewModel.customerRegisterOtherInfos(customerId, customerName, customerTc, customerBirthDate, customerPhoneNumber, customerPassword, accountLocation)
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Tebrikler, kaydınız başarıyla tamamlandı. Belirlediğiniz şifre ile müşteri numaranız veya TC Kimlik numaranızı kullanarak giriş yapabilirsiniz. Parolanızı unutursanız giriş sayfasında 'Parolamı Unuttum' seçeneği ile yeni parola alabilirsiniz. Hoşgeldiniz.")
            .setPositiveButton("Tamam") { dialog, id ->
                findNavController().navigate(R.id.action_beOurPersonalCustomerStepTwoFragment_to_anasayfaFragment)
            }
        builder.create().show()
    }

}