package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAnasayfaBinding
import com.example.garantibbva.ui.viewmodel.HomePageViewModel

class AnasayfaFragment : Fragment() {
    private lateinit var binding: FragmentAnasayfaBinding
    private val viewModel: HomePageViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_anasayfa, container, false)
        binding.anasayfaFragment = this


        viewModel.customerList.observe(viewLifecycleOwner, Observer { customers ->
        })

        return binding.root
    }

    fun costumerLoginButton(it: View) {
        Navigation.findNavController(it).navigate(R.id.action_anasayfaFragment_to_loginFragment)
    }

    fun corpLoginButton(it: View) {
        Navigation.findNavController(it).navigate(R.id.action_anasayfaFragment_to_corpLoginFragment)
    }
}
