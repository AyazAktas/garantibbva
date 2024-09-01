package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeOurCustomerFragment : Fragment() {
    private lateinit var binding: FragmentBeOurCustomerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_be_our_customer, container, false)
        binding.beOurCustomerFragment=this
        return binding.root
    }

    fun returnHomePageImage(it:View){
        Navigation.findNavController(it).navigate(R.id.action_beOurCustomerFragment_to_anasayfaFragment)
    }

    fun beOurPersonalCustomer(it:View){
        Navigation.findNavController(it).navigate(R.id.action_beOurCustomerFragment_to_beOurCustomerPersonalFragment)
    }

}