package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentLoginBinding
import com.example.garantibbva.ui.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    var enteredNumber: String = ""
    var enteredPassword: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.costumerLoginFragment = this
        binding.buttonForgetPassword.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_forgotMyPasswordFragment)
        }
        return binding.root
    }

    fun onLoginClicked() {
        viewLifecycleOwner.lifecycleScope.launch {
            val customer = loginViewModel.login(enteredNumber, enteredPassword)
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
