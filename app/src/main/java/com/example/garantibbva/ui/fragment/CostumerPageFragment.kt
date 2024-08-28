package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentCustomerPageBinding

class CustomerPageFragment : Fragment() {
    private lateinit var binding: FragmentCustomerPageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_customer_page,container,false)
        binding.customerPageFragment=this

        val bundle:CustomerPageFragmentArgs by navArgs()
        val transactionCustomer=bundle.customer
        binding.costumer=transactionCustomer


        return binding.root
    }
}