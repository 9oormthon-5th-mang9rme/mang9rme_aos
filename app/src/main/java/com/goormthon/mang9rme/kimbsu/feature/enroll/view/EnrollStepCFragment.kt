package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.FragmentEnrollStepCBinding
import com.goormthon.mang9rme.kimbsu.common.util.MangoormeVibrator
import com.goormthon.mang9rme.kimbsu.feature.enroll.viewmodel.EnrollViewModel

class EnrollStepCFragment : Fragment(), View.OnClickListener {

    private val model: EnrollViewModel by activityViewModels()
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
        model.holeQuestionAnswer.observe(requireActivity(), Observer { holeQuestionAnswer ->
            when (holeQuestionAnswer) {
                0 -> {
                    binding.apply {
                        tvEnrollAnswerHole1.setBackgroundResource(R.drawable.back_enroll_answer_left_selected)
                        tvEnrollAnswerHole1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        tvEnrollAnswerHole2.setBackgroundResource(R.drawable.back_enroll_answer_mid_unselected)
                        tvEnrollAnswerHole2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerHole3.setBackgroundResource(R.drawable.back_enroll_answer_right_unselected)
                        tvEnrollAnswerHole3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                    }
                }
                1 -> {
                    binding.apply {
                        tvEnrollAnswerHole1.setBackgroundResource(R.drawable.back_enroll_answer_left_unselected)
                        tvEnrollAnswerHole1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerHole2.setBackgroundResource(R.drawable.back_enroll_answer_mid_selected)
                        tvEnrollAnswerHole2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        tvEnrollAnswerHole3.setBackgroundResource(R.drawable.back_enroll_answer_right_unselected)
                        tvEnrollAnswerHole3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                    }
                }
                2 -> {
                    binding.apply {
                        tvEnrollAnswerHole1.setBackgroundResource(R.drawable.back_enroll_answer_left_unselected)
                        tvEnrollAnswerHole1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerHole2.setBackgroundResource(R.drawable.back_enroll_answer_mid_unselected)
                        tvEnrollAnswerHole2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerHole3.setBackgroundResource(R.drawable.back_enroll_answer_right_selected)
                        tvEnrollAnswerHole3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                    }
                }
            }
            binding.tvEnrollNextStep.alpha = if (model.allQuestionCheck) 1f else 0.3f
        })

        model.dotQuestionAnswer.observe(requireActivity(), Observer { dotQuestionAnswer ->
            when (dotQuestionAnswer) {
                0 -> {
                    binding.apply {
                        tvEnrollAnswerDot1.setBackgroundResource(R.drawable.back_enroll_answer_left_selected)
                        tvEnrollAnswerDot1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        tvEnrollAnswerDot2.setBackgroundResource(R.drawable.back_enroll_answer_mid_unselected)
                        tvEnrollAnswerDot2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerDot3.setBackgroundResource(R.drawable.back_enroll_answer_right_unselected)
                        tvEnrollAnswerDot3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                    }
                }
                1 -> {
                    binding.apply {
                        tvEnrollAnswerDot1.setBackgroundResource(R.drawable.back_enroll_answer_left_unselected)
                        tvEnrollAnswerDot1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerDot2.setBackgroundResource(R.drawable.back_enroll_answer_mid_selected)
                        tvEnrollAnswerDot2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        tvEnrollAnswerDot3.setBackgroundResource(R.drawable.back_enroll_answer_right_unselected)
                        tvEnrollAnswerDot3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                    }
                }
                2 -> {
                    binding.apply {
                        tvEnrollAnswerDot1.setBackgroundResource(R.drawable.back_enroll_answer_left_unselected)
                        tvEnrollAnswerDot1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerDot2.setBackgroundResource(R.drawable.back_enroll_answer_mid_unselected)
                        tvEnrollAnswerDot2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerDot3.setBackgroundResource(R.drawable.back_enroll_answer_right_selected)
                        tvEnrollAnswerDot3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                    }
                }
            }
            binding.tvEnrollNextStep.alpha = if (model.allQuestionCheck) 1f else 0.3f
        })

        model.colorQuestionAnswer.observe(requireActivity(), Observer { colorQuestionAnswer ->
            when (colorQuestionAnswer) {
                0 -> {
                    binding.apply {
                        tvEnrollAnswerColor1.setBackgroundResource(R.drawable.back_enroll_answer_left_selected)
                        tvEnrollAnswerColor1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        tvEnrollAnswerColor2.setBackgroundResource(R.drawable.back_enroll_answer_mid_unselected)
                        tvEnrollAnswerColor2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerColor3.setBackgroundResource(R.drawable.back_enroll_answer_right_unselected)
                        tvEnrollAnswerColor3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                    }
                }
                1 -> {
                    binding.apply {
                        tvEnrollAnswerColor1.setBackgroundResource(R.drawable.back_enroll_answer_left_unselected)
                        tvEnrollAnswerColor1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerColor2.setBackgroundResource(R.drawable.back_enroll_answer_mid_selected)
                        tvEnrollAnswerColor2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        tvEnrollAnswerColor3.setBackgroundResource(R.drawable.back_enroll_answer_right_unselected)
                        tvEnrollAnswerColor3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                    }
                }
                2 -> {
                    binding.apply {
                        tvEnrollAnswerColor1.setBackgroundResource(R.drawable.back_enroll_answer_left_unselected)
                        tvEnrollAnswerColor1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerColor2.setBackgroundResource(R.drawable.back_enroll_answer_mid_unselected)
                        tvEnrollAnswerColor2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_800
                            )
                        )
                        tvEnrollAnswerColor3.setBackgroundResource(R.drawable.back_enroll_answer_right_selected)
                        tvEnrollAnswerColor3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                    }
                }
            }
            binding.tvEnrollNextStep.alpha = if (model.allQuestionCheck) 1f else 0.3f
        })
    }

    private fun init() {
        binding.apply {
            ivEnrollBack.setOnClickListener(this@EnrollStepCFragment)

            tvEnrollAnswerHole1.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerHole2.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerHole3.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerDot1.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerDot2.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerDot3.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerColor1.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerColor2.setOnClickListener(this@EnrollStepCFragment)
            tvEnrollAnswerColor3.setOnClickListener(this@EnrollStepCFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            // 좌상단 뒤로가기 버튼
            R.id.iv_enroll_back -> {
                requireActivity().onBackPressed()
            }
            R.id.tv_enroll_answer_hole_1 -> {
                model.setHoleQuestionAnswer(0)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_hole_2 -> {
                model.setHoleQuestionAnswer(1)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_hole_3 -> {
                model.setHoleQuestionAnswer(2)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_dot_1 -> {
                model.setDotQuestionAnswer(0)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_dot_2 -> {
                model.setDotQuestionAnswer(1)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_dot_3 -> {
                model.setDotQuestionAnswer(2)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_color_1 -> {
                model.setColorQuestionAnswer(0)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_color_2 -> {
                model.setColorQuestionAnswer(1)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
            R.id.tv_enroll_answer_color_3 -> {
                model.setColorQuestionAnswer(2)
                MangoormeVibrator.requestVibrate(
                    requireContext(),
                    MangoormeVibrator.VibrationType.TICK
                )
            }
        }
    }

    companion object {
        const val TAG: String = "EnrollStepCFragment"
    }
}