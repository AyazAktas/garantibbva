package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAccountClosingBinding
import com.example.garantibbva.ui.viewmodel.AccountClosingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountClosingFragment : Fragment() {
    private lateinit var binding: FragmentAccountClosingBinding
    private val passwordViewModel: AccountClosingViewModel by viewModels()
    private var isPasswordValid = false
    private var isCheckboxChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_closing, container, false)
        binding.accountClosingFragment = this
        val args: AccountClosingFragmentArgs by navArgs()
        val transactionCustomer = args.customer
        val transactionCorp = args.corp

        binding.customer = transactionCustomer
        binding.corp = transactionCorp

        transactionCustomer?.let {
            binding.textViewAccountOwner.text = it.customerName
            val iban = formatIban(it.ibanNumber.toString())
            binding.textViewAccountNoClosing.text = "${it.accountNo} / ${it.accountLocation}"
            binding.textViewAccountIban.text = iban
            binding.editTextTextPassword3.addTextChangedListener {
                customerPasswordValidation(it.toString())
            }
            binding.button3.setOnClickListener {
                Log.e("işlem", "başarılı")
            }
        }

        transactionCorp?.let {
            binding.textViewAccountOwner.text = it.contactPersonName
            val iban = formatIban(it.iban.toString())
            binding.textViewAccountNoClosing.text = "${it.corpAccountNo} / ${it.address}"
            binding.textViewAccountIban.text = iban
            binding.editTextTextPassword3.addTextChangedListener {
                corpPasswordValidation(it.toString())
            }
            binding.button3.setOnClickListener {
                Log.e("işlem", "başarılı")
            }
        }


        binding.checkboxAgreement.setOnCheckedChangeListener { _, isChecked ->
            isCheckboxChecked = isChecked
            updateButtonState()
        }

        return binding.root
    }

    private fun formatIban(iban: String): String {
        return iban.chunked(4).joinToString(" ")
    }

    private fun customerPasswordValidation(password: String) {
        val transactionCustomer = binding.customer
        if (transactionCustomer != null) {
            lifecycleScope.launch {
                isPasswordValid =
                    passwordViewModel.isCustomersPasswordCorrect(transactionCustomer, password)
                updateButtonState()
            }
        }
    }

    private fun corpPasswordValidation(password: String) {
        val transactionCorp = binding.corp
        if (transactionCorp != null) {
            lifecycleScope.launch {
                isPasswordValid =
                    passwordViewModel.isCorpsPasswordCorrect(transactionCorp, password)
                updateButtonState()
            }
        }
    }


    private fun updateButtonState() {
        binding.button3.isEnabled = isPasswordValid && isCheckboxChecked
    }
}
