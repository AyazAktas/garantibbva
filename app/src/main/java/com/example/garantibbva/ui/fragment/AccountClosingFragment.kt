package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAccountClosingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountClosingFragment : Fragment() {
    private lateinit var binding: FragmentAccountClosingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_closing, container, false)
    }

}
// kullanıcının tc'si,müşteri no'su,parola 2 kez, onaylıyorum checkbox, sonra alert dialog onda da onaylama tamam a basasrsa çıkış ver