package ru.armensarkisyan.veigatestapp.common.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class BaseFragment<T:ViewBinding> : Fragment() {

    var binding: T? = null

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflateViewBinding(inflater,container)
        val view = binding?.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyBinding()
        binding = null
    }

    open fun onDestroyBinding(){}

}