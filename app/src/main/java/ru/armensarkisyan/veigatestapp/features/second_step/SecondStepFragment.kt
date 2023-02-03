package ru.armensarkisyan.veigatestapp.features.second_step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import ru.armensarkisyan.veigatestapp.R
import ru.armensarkisyan.veigatestapp.common.data.net.responses.Raiting
import ru.armensarkisyan.veigatestapp.common.secondsToHoursMinutesSeconds
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseFragment
import ru.armensarkisyan.veigatestapp.common.ui.custom_views.CustomLoader
import ru.armensarkisyan.veigatestapp.databinding.FragmentSecondStepBinding
import ru.armensarkisyan.veigatestapp.features.second_step.adapters.RatingAdapter

class SecondStepFragment : BaseFragment<FragmentSecondStepBinding>() {

    private val viewModel by inject<SecondStepViewModel>()

    private companion object {
        const val LOADER_INITIAL_PROGRESS_PERCENTAGE = 0
        const val INITIAL_TIMER_DURATION_SECONDS = 3600
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clearState()

        binding?.apply {
            setProgressToLoader(
                LOADER_INITIAL_PROGRESS_PERCENTAGE,
                loaderRatingsProgressTv,
                customLoaderRatings
            )

            viewModel.getRatings()

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is SecondStepStates.DataLoadedState -> {
                        setUpRatingList(it.ratings.map { it.value })
                    }
                    SecondStepStates.ErrorState -> {
                        setProgressToLoader(LOADER_INITIAL_PROGRESS_PERCENTAGE, loaderRatingsProgressTv, customLoaderRatings)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_network),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is SecondStepStates.LoadingProgressState -> {
                        val (customLoaderView, loaderPercentView) = when (it.loaderType) {
                            SecondStepLoaderTypes.FIRST -> {
                                Pair(customLoaderFirst, loaderFirstProgressTv)
                            }
                            SecondStepLoaderTypes.SECOND -> {
                                Pair(customLoaderSecond, loaderSecondProgressTv)
                            }
                            SecondStepLoaderTypes.RATINGS -> {
                                Pair(customLoaderRatings, loaderRatingsProgressTv)
                            }
                        }
                        setProgressToLoader(it.percentage, loaderPercentView, customLoaderView)
                    }
                    is SecondStepStates.TimerTickState -> {
                        setTimeToTimer(remainingTimeInSeconds = it.remainingSecondsAmount)
                    }
                }
            }

            reInitLoaders()
            startTimer()
            randomizeValues.setOnClickListener {
                startMockedDownloading()
            }
            backIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setLoadingState(SecondStepViewModel.LoadingStates.PAUSED)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setLoadingState(SecondStepViewModel.LoadingStates.RESUMED)
    }

    private fun setUpRatingList(ratings: List<Raiting>) {
        binding?.apply {
            ratingsRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            ratingsRv.itemAnimator = LandingAnimator(LinearInterpolator())
            ratingsRv.adapter = RatingAdapter().also { it.setData(ratings) }
        }
    }

    private fun reInitLoaders() {
        binding?.apply {
            setProgressToLoader(
                LOADER_INITIAL_PROGRESS_PERCENTAGE,
                loaderFirstProgressTv,
                customLoaderFirst
            )
            setProgressToLoader(
                LOADER_INITIAL_PROGRESS_PERCENTAGE,
                loaderSecondProgressTv,
                customLoaderSecond
            )
        }
    }

    private fun setProgressToLoader(
        percentage: Int,
        progressPercentageView: TextView,
        progressLoaderView: CustomLoader
    ) {
        progressLoaderView.setPercentage(percentage)
        progressPercentageView.text = String.format(
            resources.getString(R.string.percentage_count),
            percentage
        )
    }

    private fun startTimer() {
        setTimeToTimer(INITIAL_TIMER_DURATION_SECONDS)
        viewModel.startTimer()
    }


    private fun setTimeToTimer(remainingTimeInSeconds: Int) {
        binding?.apply {
            val (hours, minutes, seconds) = remainingTimeInSeconds.secondsToHoursMinutesSeconds()
            timerHoursTv.text = hours
            timerMinutesTv.text = minutes
            timerSecondsTv.text = seconds
        }
    }



    private fun startMockedDownloading() {
        binding?.apply {
            reInitLoaders()
            viewModel.startMockedDownloading()
        }
    }


    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSecondStepBinding {
        return FragmentSecondStepBinding.inflate(inflater, container, false)
    }

}