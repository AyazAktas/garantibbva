package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    var enteredNumber: String = ""
    var enteredPassword: String = ""
    private val testCustomer = Customer(
        customerId = "1",
        customerName = "Ayaz",
        customerTc = "12345678901",
        customerPassword = "1234",
        customersBalance = 1000,
        customerNo = "10001",
        accountType = "Savings",
        accountInfo = "Regular savings account"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.costumerLoginFragment = this
        return binding.root
    }

    fun onLoginClicked() {
        if ((enteredNumber == testCustomer.customerTc || enteredNumber == testCustomer.customerNo)
            && enteredPassword == testCustomer.customerPassword) {
            Toast.makeText(context, "Giriş başarılı", Toast.LENGTH_SHORT).show()
            val action=LoginFragmentDirections.actionLoginFragmentToCustomerPageFragment(testCustomer)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, "Hatalı Numara veya Parola", Toast.LENGTH_SHORT).show()
        }
    }

    fun onForgetPasswordClicked() {
        Toast.makeText(context, "Parola sıfırlama işlemi başlatıldı", Toast.LENGTH_SHORT).show()
    }
}
