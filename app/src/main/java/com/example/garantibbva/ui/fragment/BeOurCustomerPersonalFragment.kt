package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurCustomerPersonalBinding

class BeOurCustomerPersonalFragment : Fragment() {
    private lateinit var binding: FragmentBeOurCustomerPersonalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_be_our_customer_personal, container, false)
        binding.beOurPersonalCustomerFragment = this
        binding.checkBoxPrivacy.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.checkBoxInfoLetter.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.chipGroupAccountType.setOnCheckedStateChangeListener{ _, _ -> updateButtonState() }

        return binding.root
    }

    private fun updateButtonState() {
        val isAnyChipChecked = binding.chipGroupAccountType.checkedChipIds.isNotEmpty()
        val areBothCheckBoxesChecked = binding.checkBoxPrivacy.isChecked && binding.checkBoxInfoLetter.isChecked
        binding.buttonStartRegister.isEnabled = isAnyChipChecked && areBothCheckBoxesChecked
    }

    fun returnBeOurCustomerPage(it: View) {
        Navigation.findNavController(it).navigate(R.id.action_beOurCustomerPersonalFragment_to_beOurCustomerFragment)
    }
}


