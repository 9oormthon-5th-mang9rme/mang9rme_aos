package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goormthon.mang9rme.databinding.FragmentEnrollStepCBinding

class EnrollStepCFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEnrollStepCBinding? = null
    private val binding: FragmentEnrollStepCBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    // region Fragment LifeCycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnrollStepCBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion Fragment LifeCycle

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    companion object {
        private const val TAG: String = "EnrollStepCFragment"
    }
}