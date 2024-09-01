package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurCustomerPersonalBinding
import com.example.garantibbva.ui.viewmodel.PersonalCustomerRegisterViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeOurCustomerPersonalFragment : Fragment() {
    private lateinit var binding: FragmentBeOurCustomerPersonalBinding
    private val registerViewModel: PersonalCustomerRegisterViewModel by viewModels()

    // Listeyi oluşturuyoruz
    private val selectedAccountTypes = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_be_our_customer_personal, container, false)
        binding.beOurPersonalCustomerFragment = this
        binding.checkBoxPrivacy.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.checkBoxInfoLetter.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.chipGroupAccountType.setOnCheckedStateChangeListener { _, checkedChipIds -> updateSelectedChips(checkedChipIds) }

        return binding.root
    }

    private fun updateButtonState() {
        val isAnyChipChecked = binding.chipGroupAccountType.checkedChipIds.isNotEmpty()
        val areBothCheckBoxesChecked = binding.checkBoxPrivacy.isChecked && binding.checkBoxInfoLetter.isChecked
        binding.buttonStartRegister.isEnabled = isAnyChipChecked && areBothCheckBoxesChecked
    }

    private fun updateSelectedChips(checkedChipIds: List<Int>) {
        selectedAccountTypes.clear()
        checkedChipIds.forEach { chipId ->
            val chip = binding.chipGroupAccountType.findViewById<Chip>(chipId)
            chip?.let {
                selectedAccountTypes.add(it.text.toString())
            }
        }
    }

    fun returnBeOurCustomerPage(it: View) {
        Navigation.findNavController(it).navigate(R.id.action_beOurCustomerPersonalFragment_to_beOurCustomerFragment)
    }

    fun register() {
        if (selectedAccountTypes.isEmpty()) {
            Snackbar.make(binding.root, "Lütfen bir hesap türü seçin", Snackbar.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val selectedAccountType=selectedAccountTypes.toString()
            try {
                val customer=registerViewModel.personalCustomerRegister(
                    customerId = "",
                    costumerProfilePicture = R.drawable.baseline_person_24,
                    customerName = "",
                    customerTc = "",
                    customerBirthDate = "",
                    customerPhoneNumber = "",
                    customerPassword = "",
                    customersBalance = 0.0,
                    accountLocation = "",
                    accountType = selectedAccountType,
                    accountPurpose = ""
                )
                Snackbar.make(binding.root, "Kayıt başarılı", Snackbar.LENGTH_SHORT).show()
                val action=BeOurCustomerPersonalFragmentDirections.actionBeOurCustomerPersonalFragmentToBeOurPersonalCustomerStepTwoFragment(customer=customer)
                findNavController().navigate(action)

            } catch (e: Exception) {
                Log.e("hata","Kayıt sırasında bir hata oluştu: ${e.message}")
            }
        }
    }
}
