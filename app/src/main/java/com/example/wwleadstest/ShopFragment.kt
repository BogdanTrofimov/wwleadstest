package com.example.wwleadstest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.wwleadstest.databinding.FragmentShopBinding
import kotlin.properties.Delegates

class ShopFragment : Fragment() {
    private var binding by Delegates.notNull<FragmentShopBinding>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.increasePowerTv.setOnClickListener {
            sharedViewModel.increaseAtk()
        }
        binding.resetAtk.setOnClickListener {
            sharedViewModel.resetAtk()
        }
        sharedViewModel.sendPingDialogStateLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                binding.currentClickPower.text = it.currentAtk.toString()
            }
        }
    }
}