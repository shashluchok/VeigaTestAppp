package ru.armensarkisyan.veigatestapp.common.ui.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.AnimationDrawable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import ru.armensarkisyan.veigatestapp.R
import ru.armensarkisyan.veigatestapp.databinding.LayoutGradientLoaderBinding


class CustomLoader : ConstraintLayout {

    private var binding: LayoutGradientLoaderBinding? = null
    private var currentPercentage: Int = 0
    private var currentType: Types = Types.FIRST_STEP

    enum class Types {
        FIRST_STEP, SECOND_STEP, RATINGS
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private companion object {
        const val MAX_PERCENTAGE = 100
        const val MIN_PERCENTAGE = 0
        const val LOADER_ANIMATION_DURATION = 500L
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        currentType = when (attrs?.getAttributeValue(
            "http://schemas.android.com/apk/res-auto",
            "setType"
        ) ?: "") {
            "0x1" -> {
                Types.FIRST_STEP
            }
            "0x2" -> {
                Types.SECOND_STEP
            }
            "0x3" -> {
                Types.RATINGS
            }
            else -> {
                Types.FIRST_STEP
            }
        }

        initView()

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    fun setPercentage(percentage: Int) {
        this.currentPercentage = percentage
        binding?.apply {
            loaderConstraint.setGuidelinePercent(percentage / MAX_PERCENTAGE.toFloat())
            animateConstraintLayout(loaderCl, ConstraintSet().also { it.clone(loaderCl) }, LOADER_ANIMATION_DURATION)
        }
    }

    private fun animateConstraintLayout(
        constraintLayout: ConstraintLayout?,
        set: ConstraintSet,
        duration: Long
    ) {
        val trans = AutoTransition()
        trans.duration = duration
        trans.interpolator = AccelerateDecelerateInterpolator()
        TransitionManager.beginDelayedTransition(constraintLayout, trans)
        set.applyTo(constraintLayout)
    }

    private fun initView() {
        binding = LayoutGradientLoaderBinding.inflate(LayoutInflater.from(context), this, false)
        binding?.apply {
            addView(root)
            root.layoutTransition.setDuration(LOADER_ANIMATION_DURATION)
            val cardBgColor: Int
            val loaderBgRes: Int
            when (currentType) {
                Types.FIRST_STEP -> {
                    cardBgColor = resources.getColor(R.color.app_white)
                    loaderBgRes = R.drawable.gradient_animation
                }
                Types.SECOND_STEP -> {
                    cardBgColor = resources.getColor(R.color.app_black)
                    loaderBgRes = R.drawable.gradient_animation_second
                }
                Types.RATINGS -> {
                    cardBgColor = resources.getColor(R.color.app_white)
                    loaderBgRes = R.drawable.gradient_animation_ratings
                }
            }
            root.setCardBackgroundColor(cardBgColor)
            loaderProgressGradientView.setBackgroundResource(loaderBgRes)
            val animDrawable = loaderProgressGradientView.background as AnimationDrawable
            animDrawable.setEnterFadeDuration(10)
            animDrawable.setExitFadeDuration(5000)
            animDrawable.start()

        }
    }

}


