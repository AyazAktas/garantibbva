package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateValueAsState
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentMoneyTransferBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoneyTransferFragment : Fragment() {
    private lateinit var binding:FragmentMoneyTransferBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_money_transfer,container,false)
        binding.fragmentMoneyTransfer=this
        val args:MoneyTransferFragmentArgs by navArgs()
        val transactionCustomer = args.customer
        val transactionCorp = args.corp

        binding.customer = transactionCustomer
        binding.corp = transactionCorp

        transactionCustomer?.let {
            binding.cardViewIban.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToIbanTransferFragment(transactionCustomer,null)
                findNavController().navigate(action)
            }
            binding.toolbarImage.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToCustomerPageFragment(transactionCustomer)
                findNavController().navigate(action)
            }
        }
        transactionCorp?.let {
            binding.cardViewIban.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToIbanTransferFragment(null,transactionCorp)
                findNavController().navigate(action)
            }
            binding.toolbarImage.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToCorpPageFragment(transactionCorp)
                findNavController().navigate(action)
            }
        }


        return binding.root
    }
}