package ru.armensarkisyan.veigatestapp.features.first_step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import ru.armensarkisyan.veigatestapp.MainActivity
import ru.armensarkisyan.veigatestapp.R
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseFragment
import ru.armensarkisyan.veigatestapp.databinding.FragmentFirstStepBinding


class FirstStepFragment : BaseFragment<FragmentFirstStepBinding>() {

    private var wasLottieAnimating = false

    private val viewModel by inject<FirstStepViewModel>()

    private companion object {
        const val ALPHA_VALUE_VISIBLE = 1f
        const val ALPHA_VALUE_INVISIBLE = 0f
        const val ALPHA_CHANGE_ANIMATION_DURATION = 200L
        const val LOADER_INITIAL_PROGRESS_PERCENTAGE = 0
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clearState()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is FirstStepStates.LoadingProgressState -> {
                    binding?.apply {
                        customLoader.setPercentage(it.percentage)
                        loaderProgressTv.text = String.format(
                            resources.getString(R.string.percentage_count),
                            it.percentage
                        )
                    }
                }
            }
        }

        startMockedLoading()
        setOnClickListeners()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setLoadingState(FirstStepViewModel.LoadingStates.PAUSED)
        binding?.apply {
            wasLottieAnimating = lottieAnimationView.isAnimating
            lottieAnimationView.pauseAnimation()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            if (wasLottieAnimating) {
                lottieAnimationView.resumeAnimation()
            }
        }
        viewModel.setLoadingState(FirstStepViewModel.LoadingStates.RESUMED)
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
                val newAlpha = when (lottieAnimationView.alpha) {
                    ALPHA_VALUE_VISIBLE -> {
                        ALPHA_VALUE_INVISIBLE
                    }
                    ALPHA_VALUE_INVISIBLE -> {
                        ALPHA_VALUE_VISIBLE
                    }
                    else -> {
                        lottieAnimationView.alpha
                    }
                }
                lottieAnimationView.animate().alpha(newAlpha).duration =
                    ALPHA_CHANGE_ANIMATION_DURATION

            }
            secondStepTv.setOnClickListener {
                findNavController().navigate(R.id.action_firstStepFragment_to_secondStepFragment)
            }
        }
    }

    private fun startMockedLoading() {
        binding?.apply {
            customLoader.setPercentage(LOADER_INITIAL_PROGRESS_PERCENTAGE)
            loaderProgressTv.text =
                String.format(
                    resources.getString(R.string.percentage_count),
                    LOADER_INITIAL_PROGRESS_PERCENTAGE
                )
        }
        viewModel.startMockedLoading()
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFirstStepBinding {
        return FragmentFirstStepBinding.inflate(inflater, container, false)
    }

}
