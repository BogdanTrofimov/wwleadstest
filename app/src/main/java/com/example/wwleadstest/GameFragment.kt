package com.example.wwleadstest

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.wwleadstest.databinding.FragmentGameBinding
import kotlin.properties.Delegates

class GameFragment : Fragment() {
    private var binding by Delegates.notNull<FragmentGameBinding>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.progressBarCountdown.progressTintList = ColorStateList.valueOf(Color.RED)
        sharedViewModel.setupTimer()
        setOnCLick()
        initCounterListener()
        return binding.root
    }

    private fun initCounterListener() {
        sharedViewModel.sendPingDialogStateLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                binding.progressBarCountdown.progress = it.currentProgress
                binding.timeCounter.text = it.currentTime.toString()
                binding.pointsCounter.text = it.currentScore
                binding.clickButton.isVisible = it.isViewVisible
                binding.progressBarCountdown.isVisible = it.isViewVisible
                binding.timeCounter.isVisible = it.isViewVisible
                binding.retryButton.isVisible = !it.isViewVisible
                if (it.youLoose) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(getString(R.string.loose_message))

                    builder.setPositiveButton("Ok") { dialog, which ->
                        dialog.cancel()
                        sharedViewModel.retry()
                    }
                    builder.setCancelable(false).show()
                }
            }
        }
    }

    private fun setOnCLick() {
        binding.clickButton.setOnClickListener {
            sharedViewModel.onCounterButtonClicked()
        }
        binding.retryButton.setOnClickListener {
            sharedViewModel.retry()
        }
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.allClear()
    }

}