package com.example.wwleadstest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.wwleadstest.databinding.FragmentSettingsBinding
import kotlin.properties.Delegates

class SettingsFragment : Fragment() {
    private var binding by Delegates.notNull<FragmentSettingsBinding>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.buttonEasyLevel.setOnClickListener {
            sharedViewModel.setEasyLevel()
            Toast.makeText(
                requireContext(),
                getString(R.string.easy_level_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonMediumLevel.setOnClickListener {
            sharedViewModel.setMediumLevel()
            Toast.makeText(
                requireContext(),
                getString(R.string.medium_level_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonHardLevel.setOnClickListener {
            sharedViewModel.setHardLevel()
            Toast.makeText(
                requireContext(),
                getString(R.string.hard_level_text),
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}