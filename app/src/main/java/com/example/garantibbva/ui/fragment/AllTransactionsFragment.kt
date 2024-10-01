package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAllTransactionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTransactionsFragment : Fragment() {
    private lateinit var binding: FragmentAllTransactionsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentAllTransactionsBinding.inflate(inflater,container,false)
        return binding.root
    }
}