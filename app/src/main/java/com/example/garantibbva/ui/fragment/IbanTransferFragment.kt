package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentIbanTransferBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IbanTransferFragment : Fragment() {
    private lateinit var binding:FragmentIbanTransferBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_iban_transfer, container,false)
        binding.fragmentIbanTransfer=this
        val args:IbanTransferFragmentArgs by navArgs()
        val transactionCustomer = args.customer
        val transactionCorp = args.corp

        binding.customer = transactionCustomer
        binding.corp = transactionCorp

        transactionCustomer?.let {
            binding.textViewAccountBalance.text="Kullanılabilir Bakiye = "+transactionCustomer.customersBalance.toString()+" TL"
            binding.textViewAccInfo.text=transactionCustomer.accountNo+" - "+transactionCustomer.accountLocation

            binding.toolbarImage.setOnClickListener {
                val action=IbanTransferFragmentDirections.actionIbanTransferFragmentToMoneyTransferFragment(null,transactionCustomer)
                findNavController().navigate(action)
            }
        }

        transactionCorp?.let {
            binding.textViewAccountBalance.text="Kullanılabilir Bakiye = "+transactionCorp.accountBalance.toString()+" TL"
            binding.textViewAccInfo.text=transactionCorp.corpAccountNo+" - "+transactionCorp.address

            binding.toolbarImage.setOnClickListener {
                val action=IbanTransferFragmentDirections.actionIbanTransferFragmentToMoneyTransferFragment(transactionCorp,null)
                findNavController().navigate(action)
            }
        }


        return binding.root
    }

}