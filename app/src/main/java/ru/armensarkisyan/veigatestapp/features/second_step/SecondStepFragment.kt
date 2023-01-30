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
import ru.armensarkisyan.veigatestapp.common.ui.base.BaseFragment
import ru.armensarkisyan.veigatestapp.common.ui.custom_views.CustomLoader
import ru.armensarkisyan.veigatestapp.databinding.FragmentSecondStepBinding
import ru.armensarkisyan.veigatestapp.features.second_step.adapters.RatingAdapter
import kotlin.random.Random

class SecondStepFragment : BaseFragment<FragmentSecondStepBinding>() {

    private val viewModel by inject<SecondStepViewModel>()

    private var downloadingJob: Job? = null
    private var timerJob: Job? = null

    private var isPaused = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearState()

        binding?.apply {
            setProgressToLoader(0, loaderRatingsProgressTv, customLoaderRatings)

            var job: Job? = lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                viewModel.getRatings()
                val seconds = 7
                var currentSecond = 0
                while (isActive)
                    if (!isPaused) {
                        if (currentSecond >= seconds - 1) {
                            cancel()
                        } else currentSecond += 1
                        val percentage = (100f / seconds * currentSecond).toInt()
                        withContext(Dispatchers.Main) {
                            val mPercentage = if (percentage in 0..100) percentage else 100
                            setProgressToLoader(
                                mPercentage,
                                loaderRatingsProgressTv,
                                customLoaderRatings
                            )
                        }
                        delay(1000)

                    }

            }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is SecondStepStates.DataLoadedState -> {
                        job?.cancel()
                        job = null
                        setProgressToLoader(100, loaderRatingsProgressTv, customLoaderRatings)
                        setUpRatingList(it.ratings.map { it.value })
                    }
                    SecondStepStates.ErrorState -> {
                        job?.cancel()
                        job = null
                        setProgressToLoader(0, loaderRatingsProgressTv, customLoaderRatings)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_network),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            reInitDownloaders()
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
        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
    }

    private fun setUpRatingList(ratings: List<Raiting>) {
        binding?.apply {
            ratingsRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            ratingsRv.itemAnimator = LandingAnimator(LinearInterpolator())
            ratingsRv.adapter = RatingAdapter().also { it.setData(ratings) }
        }
    }

    private fun reInitDownloaders() {
        binding?.apply {
            setProgressToLoader(0, loaderFirstProgressTv, customLoaderFirst)
            setProgressToLoader(0, loaderSecondProgressTv, customLoaderSecond)
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
        setTimeToTimer(null)
        var seconds = 60 * 60
        timerJob = lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            repeat(seconds) {
                if (seconds == 0) {
                    stopTimer()
                } else {
                    seconds--
                    setTimeToTimer(seconds)
                }
                delay(1000)
            }
        }
    }

    private fun setTimeToTimer(seconds: Int?) {
        binding?.apply {
            if (seconds == null) {
                timerHoursTv.text = "01"
                timerMinutesTv.text = "00"
                timerSecondsTv.text = "00"
            } else {
                val hours = (seconds / 3600).toString()
                val minutes = (seconds / 60).toString()
                val mSeconds = (seconds % 60).toString()
                timerHoursTv.text = if (hours.length < 2) "0$hours" else hours
                timerMinutesTv.text = if (minutes.length < 2) "0$minutes" else minutes
                timerSecondsTv.text = if (mSeconds.length < 2) "0$mSeconds" else mSeconds
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob?.cancelChildren()
    }

    private fun startMockedDownloading() {
        binding?.apply {
            reInitDownloaders()
            downloadingJob?.cancelChildren()
            downloadingJob?.cancel()
            downloadingJob = lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                launch {
                    startRandomDownloading(loaderFirstProgressTv, customLoaderFirst)

                }
                delay(Random.nextInt(0, 500).toLong())
                launch {
                    startRandomDownloading(loaderSecondProgressTv, customLoaderSecond)
                }
            }
        }
    }

    private suspend fun startRandomDownloading(
        progressPercentageView: TextView,
        progressLoaderView: CustomLoader
    ) {
        withContext(Dispatchers.IO) {
            val seconds = Random.nextInt(5, 25)
            var currentSecond = 0
            while (isActive)
                if (!isPaused) {
                    if (currentSecond >= seconds) {
                        cancel()
                    } else currentSecond += 1
                    val percentage = (100f / seconds * currentSecond).toInt()
                    withContext(Dispatchers.Main) {
                        val mPercentage = if (percentage in 0..100) percentage else 100
                        setProgressToLoader(mPercentage, progressPercentageView, progressLoaderView)
                    }
                    delay(1000)

                }

        }
    }


    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSecondStepBinding {
        return FragmentSecondStepBinding.inflate(inflater, container, false)
    }

}