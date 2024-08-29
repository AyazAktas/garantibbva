package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val customerDataSource = CustomerDataSource()

    var enteredNumber: String = ""
    var enteredPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.costumerLoginFragment = this
        return binding.root
    }

    fun onLoginClicked() {
        viewLifecycleOwner.lifecycleScope.launch {
            val customer = customerDataSource.login(enteredNumber, enteredPassword)
            if (customer != null) {
                    Toast.makeText(context, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                    val action = LoginFragmentDirections.actionLoginFragmentToCustomerPageFragment(customer)
                    findNavController().navigate(action)
            } else {
                Snackbar.make(binding.root, "Hatalı Numara veya Parola", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}


