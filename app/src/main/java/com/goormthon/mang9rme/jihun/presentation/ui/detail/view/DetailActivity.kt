package com.goormthon.mang9rme.jihun.presentation.ui.detail.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.databinding.ActivityDetailBinding
import com.goormthon.mang9rme.databinding.ItemPopupStoneExplainBinding
import com.goormthon.mang9rme.jihun.presentation.ui.detail.viewmodel.DetailViewModel
import com.goormthon.mang9rme.jihun.util.AnimationChain
import com.goormthon.mang9rme.jihun.util.ObjectAnimator
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapView


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        initBinding()
        initClickListener()
        observe()
    }

    fun dismiss() = finish()

    private fun initBinding() {
        binding.activity = this
    }

    private fun initClickListener() {
        binding.cardStoneStat.root.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                val anim = android.animation.ObjectAnimator.ofFloat(it, "scaleX", 1f, 0f)
                    .setDuration(500)
                anim.doOnEnd { on ->
                    rotateMore(it)
                    isLoading = false
                }
                anim.start()

            }
        }
    }

    private fun rotateMore(view: View) {
        android.animation.ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            .setDuration(500).start()
    }

    override fun onStart() {
        super.onStart()
        intent.getIntExtra("targetStone", 0).let { viewModel.getStoneData(it) }
    }

    private fun observe() {
        viewModel.stoneData.observe(this) {
            binding.stoneData = it
            initIncludeView(it)
        }
    }

    private fun initIncludeView(data: StoneData) {
        binding.cardStoneStat.apply {
            itemCardStoneStatTvStoneName.setText(data.stoneName)

            itemCardStoneStatProgressAttackStat.progress = data.attack
            itemCardStoneStatProgressDefenceStat.progress = data.defense
            itemCardStoneStatProgressMagicStat.progress = data.magicDefense

            itemCardStoneStatBtnAttackExplain.setOnClickListener { openAttackPopup() }
            itemCardStoneStatBtnDefenceExplain.setOnClickListener { openDefensePopup() }
            itemCardStoneStatBtnMagicExplain.setOnClickListener { openMagicPopup() }

            itemCardStoneStatTvStoneName.setOnKeyListener { _, p1, p2 ->
                if (p1 == KeyEvent.KEYCODE_ENTER && p2.action == KeyEvent.ACTION_UP) {
                    viewModel.patchStoneName(binding.cardStoneStat.itemCardStoneStatTvStoneName.text.toString())
                    hideKeyboard()
                }
                false
            }


            itemCardStoneStatIvStoneCharacter.setImageResource(
                setImageIcons(
                    data.stoneType,
                    data.attack + data.defense + data.magicDefense
                )
            )
        }
    }

    private fun setImageIcons(stoneType: String, totStat: Int): Int {
        return when (stoneType) {
            "현무암" -> if (totStat >= 150) R.drawable.ic_strong_hyunmu else R.drawable.ic_weak_hyunmu
            "화산송이" -> if (totStat >= 150) R.drawable.ic_strong_hwasan else R.drawable.ic_weak_hwasan
            "화강암" -> if (totStat >= 150) R.drawable.ic_strong_hwagang else R.drawable.ic_weak_hwagang
            else -> if (totStat >= 150) R.drawable.ic_strong_sukhwae else R.drawable.ic_weak_sukhwae
        }
    }

    fun openMap() {
        showMapDialog()
    }

    private fun showMapDialog() {
        MapDialogFragment(MapView(this)).show(
            supportFragmentManager, "MapDialog"
        )
    }

    private fun openAttackPopup() {
        val attackBinding = ItemPopupStoneExplainBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            attackBinding.root,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )
        attackBinding.itemPopupStoneExplainTvExplain.text =
            "제주 돌은 각기 다른 질감을 가지고 있어요. \n사진 속 돌의 질감이 거칠수록 공격력이 높아요."

        popupWindow.elevation = 3f
        popupWindow.showAsDropDown(binding.cardStoneStat.itemCardStoneStatBtnAttackExplain, 0, 0)
    }

    private fun openDefensePopup() {
        val defenseBinding = ItemPopupStoneExplainBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            defenseBinding.root,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )
        defenseBinding.itemPopupStoneExplainTvExplain.text =
            "제주 돌은 각기 다른 강도를 가지고 있어요. \n제주 돌의 종류에 따라 방어력이 달라요."

        popupWindow.elevation = 3f
        popupWindow.showAsDropDown(binding.cardStoneStat.itemCardStoneStatBtnDefenceExplain, 0, 0)
    }

    private fun openMagicPopup() {
        val magicBinding = ItemPopupStoneExplainBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            magicBinding.root,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )
        magicBinding.itemPopupStoneExplainTvExplain.text = "제주 돌은 각기 다른 열 저항력을 가지고 있어요."

        popupWindow.elevation = 3f
        popupWindow.showAsDropDown(binding.cardStoneStat.itemCardStoneStatBtnMagicExplain, 0, 0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val curView: View? = currentFocus
            if (curView != null) {
                // 모든 EditText에 대하여, 포커싱되어 있는 상태에서 바깥 부분을 누르면 포커스 해제함
                if (curView is EditText) {
                    val outRect = Rect()
                    curView.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        curView.post {
                            curView.clearFocus()
                            hideKeyboard()
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            binding.cardStoneStat.itemCardStoneStatTvStoneName.windowToken,
            0
        );
    }
}