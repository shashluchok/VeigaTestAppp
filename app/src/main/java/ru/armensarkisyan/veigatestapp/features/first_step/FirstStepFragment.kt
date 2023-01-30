package ru.armensarkisyan.veigatestapp.features.first_step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import ru.armensarkisyan.veigatestapp.MainActivity
import ru.armensarkisyan.veigatestapp.R
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseFragment
import ru.armensarkisyan.veigatestapp.databinding.FragmentFirstStepBinding

class FirstStepFragment : BaseFragment<FragmentFirstStepBinding>() {

    private var isPaused = false
    private var wasLottieAnimating = false
    private var mockedJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMockedLoading()
        setOnClickListeners()
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        binding?.apply {
            wasLottieAnimating = lottieAnimationView.isAnimating
            lottieAnimationView.pauseAnimation()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            if (wasLottieAnimating)
                lottieAnimationView.resumeAnimation()
        }
        isPaused = false

    }

    private fun setOnClickListeners() {
        binding?.apply {
            lottieStartTv.setOnClickListener {
                lottieAnimationView.resumeAnimation()
            }
            lottieStopTv.setOnClickListener {
                lottieAnimationView.pauseAnimation()
            }
            customAlertTv.setOnClickListener {
                (requireActivity() as? MainActivity)?.showPopupAlert()
            }
            lottieVisibilityTv.setOnClickListener {
                if (lottieAnimationView.alpha == 1f) {
                    lottieAnimationView.animate().alpha(0f).duration = 200
                } else if (lottieAnimationView.alpha == 0f) {
                    lottieAnimationView.animate().alpha(1f).duration = 200
                }
            }
            secondStepTv.setOnClickListener {
                findNavController().navigate(R.id.action_firstStepFragment_to_secondStepFragment)
            }
        }
    }

    private fun startMockedLoading() {
        mockedJob?.cancel()
        binding?.customLoader?.setPercentage(0)
        binding?.loaderProgressTv?.text =
            String.format(resources.getString(R.string.percentage_count), 0)
        mockedJob = lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            val seconds = 14
            var currentSecond = 0
            while (isActive)

                if (!isPaused) {
                    if (currentSecond >= seconds) cancel()
                    else currentSecond += 1
                    val percentage = (100f / seconds * currentSecond).toInt()
                    withContext(Dispatchers.Main) {
                        val mPercentage = if (percentage in 0..100) percentage else 100
                        binding?.customLoader?.setPercentage(mPercentage)
                        binding?.loaderProgressTv?.setText(
                            String.format(
                                resources.getString(R.string.percentage_count),
                                mPercentage
                            )
                        )
                    }
                    delay(1000)
                }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFirstStepBinding {
        return FragmentFirstStepBinding.inflate(inflater, container, false)
    }

}