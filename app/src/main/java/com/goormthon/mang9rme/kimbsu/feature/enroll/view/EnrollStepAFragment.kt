package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.DialogSelectPhotoBottomPopupBinding
import com.goormthon.mang9rme.databinding.FragmentEnrollStepABinding
import com.goormthon.mang9rme.kimbsu.data.SelectPhotoStatus
import com.goormthon.mang9rme.kimbsu.feature.enroll.viewmodel.EnrollViewModel

class EnrollStepAFragment : Fragment(), View.OnClickListener {

    private val model: EnrollViewModel by activityViewModels()
    private var _binding: FragmentEnrollStepABinding? = null
    private val binding: FragmentEnrollStepABinding get() = _binding!!

    private val bottomSheetDialog: BottomSheetDialog by lazy {
        val bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_select_photo_bottom_popup,
            null
        )
        DialogSelectPhotoBottomPopupBinding.bind(bottomSheetView).apply {
            // 카메라로 촬영
            tvSelectPhotoFromCamera.setOnClickListener { view ->
                bottomSheetDialog.dismiss()
                model.setSelectPhotoStatus(SelectPhotoStatus.CAMERA)
            }
            // 앨범에서 선택
            tvSelectPhotoFromGallery.setOnClickListener { view ->
                bottomSheetDialog.dismiss()
                model.setSelectPhotoStatus(SelectPhotoStatus.GALLERY)
            }
        }
        BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog).apply {
            setContentView(bottomSheetView)
            window?.setBackgroundDrawableResource(R.color.transparent)
        }
    }

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
        _binding = FragmentEnrollStepABinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion Fragment LifeCycle

    private fun init() {
        binding.apply {
            ivEnrollBack.setOnClickListener(this@EnrollStepAFragment)
            clEnrollAddPhoto.setOnClickListener(this@EnrollStepAFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            // 좌상단 뒤로가기 버튼
            R.id.iv_enroll_back -> {
                requireActivity().onBackPressed()
            }
            // 사진 추가 버튼
            R.id.cl_enroll_addPhoto -> {
                if (!bottomSheetDialog.isShowing) {
                    bottomSheetDialog.show()
                }
            }
        }
    }

    companion object {
        const val TAG: String = "EnrollStepAFragment"
    }
}