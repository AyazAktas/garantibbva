package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garantibbva.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeOurCorpCustomerStepTwoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_be_our_corp_customer_step_two, container, false)
    }

}