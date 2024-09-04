package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAccountDetailsPersonalBinding


class AccountDetailsPersonalFragment : Fragment() {
    private lateinit var binding: FragmentAccountDetailsPersonalBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_details_personal, container, false)
    }

}