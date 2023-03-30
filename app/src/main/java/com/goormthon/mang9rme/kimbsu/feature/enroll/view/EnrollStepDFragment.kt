package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goormthon.mang9rme.databinding.FragmentEnrollStepDBinding

class EnrollStepDFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEnrollStepDBinding? = null
    private val binding: FragmentEnrollStepDBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnrollStepDBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    companion object {
        private const val TAG: String = "EnrollStepDFragment"
    }
}