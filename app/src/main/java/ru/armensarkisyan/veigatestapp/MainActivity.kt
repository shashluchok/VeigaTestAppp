package ru.armensarkisyan.veigatestapp

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import ru.armensarkisyan.veigatestapp.databinding.ActivityMainBinding
import ru.armensarkisyan.veigatestapp.databinding.PopUpAlertBinding

class MainActivity : AppCompatActivity() {

    private var popUpAlert: PopupWindow? = null

    private lateinit var binding: ActivityMainBinding
    private var navHostFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment

    }

    fun showPopupAlert() {
        try {
            if (popUpAlert == null) {

                val binding = PopUpAlertBinding.inflate(layoutInflater)

                popUpAlert = PopupWindow(
                    binding.root, ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT, true
                )

                popUpAlert?.showAtLocation(binding.root, Gravity.NO_GRAVITY, 0, 0)

                binding.apply {

                    popUpAlertContentCl.apply {
                        alpha = 0f
                        scaleX = 0f
                        scaleY = 0f
                        visibility = View.VISIBLE
                        animate().alpha(1f)
                            .scaleY(1f)
                            .scaleX(1f)
                            .duration = 200
                    }
                    popUpAlertMainCl.setOnClickListener {
                        popUpAlert?.dismiss()
                        popUpAlert = null
                    }
                    popUpAlertCloseTv.setOnClickListener {
                        popUpAlert?.dismiss()
                        popUpAlert = null
                    }
                }
                popUpAlert?.setOnDismissListener {
                    popUpAlert = null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}