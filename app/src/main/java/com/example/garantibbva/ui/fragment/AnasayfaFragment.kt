package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAnasayfaBinding

class AnasayfaFragment : Fragment() {
    private lateinit var binding: FragmentAnasayfaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_anasayfa, container, false)
        binding.anasayfaFragment=this

        return binding.root
    }

    fun costumerLoginButton(it:View){
        Navigation.findNavController(it).navigate(R.id.action_anasayfaFragment_to_loginFragment)
    }

    fun corpLoginButton(it:View){
        Navigation.findNavController(it).navigate(R.id.action_anasayfaFragment_to_corpLoginFragment)
    }

}
