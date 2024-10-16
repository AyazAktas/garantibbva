package com.example.garantibbva.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentAnasayfaBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnasayfaFragment : Fragment() {

    private lateinit var binding: FragmentAnasayfaBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_anasayfa, container, false)
        binding.anasayfaFragment = this

        val bottomSheet = binding.root.findViewById<View>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        binding.textViewContactUs.setOnClickListener {
            val phoneNumber = "4440333"
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }
        bottomSheetBehavior.peekHeight = 300

        return binding.root
    }

    fun beOurCustomerText(it: View) {
        findNavController(it).navigate(R.id.action_anasayfaFragment_to_beOurCustomerFragment)
    }

    fun costumerLoginButton(it: View) {
        findNavController(it).navigate(R.id.action_anasayfaFragment_to_loginFragment)
    }

    fun corpLoginButton(it: View) {
        findNavController(it).navigate(R.id.action_anasayfaFragment_to_corpLoginFragment)
    }

    fun onShowMapClicked(it: View) {
        findNavController(it).navigate(R.id.action_anasayfaFragment_to_mapsFragment)
    }

}
