package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.FragmentEnrollStepBBinding
import com.goormthon.mang9rme.kimbsu.feature.enroll.viewmodel.EnrollViewModel

class EnrollStepBFragment : Fragment(), View.OnClickListener {

    private val model: EnrollViewModel by activityViewModels()
    private var _binding: FragmentEnrollStepBBinding? = null
    private val binding: FragmentEnrollStepBBinding get() = _binding!!

    // region Fragment LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnrollStepBBinding.inflate(layoutInflater, container, false)

        setObserver()
        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion Fragment LifeCycle

    private fun setObserver() {
        model.address.observe(requireActivity(), Observer { address ->
            model.setProgressFlag(false)
            binding.tvEnrollStoneHome.text = address
        })
    }

    private fun init() {
        binding.apply {
            ivEnrollBack.setOnClickListener(this@EnrollStepBFragment)
            tvEnrollNextStep.setOnClickListener(this@EnrollStepBFragment)

            model.uploadImageData.value?.let { uploadImageData ->
                tvEnrollTitle.text = uploadImageData.imgCreateDate
                ivEnrollBlurPhoto.setImageBitmap(uploadImageData.bitmap)
                ivEnrollAddPhoto.setImageBitmap(uploadImageData.bitmap)

                val lat = uploadImageData.imgLat
                val lng = uploadImageData.imgLng
                model.requestCoordToAddress(lat, lng)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            // 좌상단 뒤로가기 버튼
            R.id.iv_enroll_back -> {
                requireActivity().onBackPressed()
            }
            // 다음으로 버튼
            R.id.tv_enroll_nextStep -> {
                (activity as? EnrollActivity)?.setFragment(EnrollStepCFragment())
            }
        }
    }

    companion object {
        const val TAG: String = "EnrollStepBFragment"
    }
}