package com.goormthon.mang9rme.jihun.presentation.ui.main.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.ActivityMainBinding
import com.goormthon.mang9rme.databinding.ItemPopupFilterBinding
import com.goormthon.mang9rme.jihun.presentation.ui.detail.view.DetailActivity
import com.goormthon.mang9rme.jihun.presentation.ui.main.adapter.StoneFeedAdapter
import com.goormthon.mang9rme.jihun.presentation.ui.main.viewmodel.MainViewModel
import com.goormthon.mang9rme.kimbsu.common.util.ConvertUtil
import com.goormthon.mang9rme.kimbsu.feature.enroll.view.EnrollActivity
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val popup by lazy {
        ItemPopupFilterBinding.inflate(layoutInflater)
    }
    private lateinit var popupWindow: PopupWindow

    private lateinit var adapter: StoneFeedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, com.goormthon.mang9rme.R.layout.activity_main)


        initBinding()
        initView()
        observe()

    }


    private fun initBinding() {
        binding.activity = this
    }

    private fun initView() {
        binding.mainRvContents.adapter =
            StoneFeedAdapter(::openDetailActivity).also { adapter = it }
        binding.mainRvContents.addItemDecoration(MainRvDecorUtil(this))
    }

    override fun onStart() {
        super.onStart()
        //:TODO 스플래시에서 받아오는 로직 추가하기
        viewModel.getStoneFeedData()
    }

    fun openEnrollActivity() {
        val enrollIntent = Intent(this, EnrollActivity::class.java)
        activityResultLauncher.launch(enrollIntent)
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == RESULT_OK) {
                Snackbar.make(binding.mainTvFilter, "수집한 돌이 등록되었어요", Snackbar.LENGTH_SHORT).apply {
                    setAnchorView(binding.mainRvContents)
                    animationMode = Snackbar.ANIMATION_MODE_FADE
                    show()
                }
                viewModel.getStoneFeedData()
            }
        }

    private fun observe() {
        viewModel.stoneFeedData.observe(this) {
            adapter.submitList(it.sortedByDescending { data -> data.dateTime })
            adapter.notifyDataSetChanged()
        }
        viewModel.userToastMsg.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openDetailActivity(stoneId: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("targetStone", stoneId)
        startActivity(intent)

    }

    fun openFilter() {
        popupWindow = PopupWindow(
            popup.root,
            binding.mainLayoutFilter.width + 80,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )
        popupWindow.elevation = 3f

        popupWindow.showAsDropDown(binding.mainLayoutFilter, -80, 0)

        popup.itemPopupFilterSortWithTime.setOnClickListener(this)
        popup.itemPopupFilterSortWithLevel.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.item_popup_filter_sort_with_time -> {
                popup.itemPopupFilterSortWithTime.typeface =
                    ResourcesCompat.getFont(this, R.font.pretendard_semibold)
                popup.itemPopupFilterSortWithLevel.typeface =
                    ResourcesCompat.getFont(this, R.font.pretendard_regular)
                binding.mainTvFilter.text = "최신순"
            }

            R.id.item_popup_filter_sort_with_level -> {
                popup.itemPopupFilterSortWithTime.typeface =
                    ResourcesCompat.getFont(this, R.font.pretendard_regular)
                popup.itemPopupFilterSortWithLevel.typeface =
                    ResourcesCompat.getFont(this, R.font.pretendard_semibold)
                binding.mainTvFilter.text = "레벨순"
            }
        }
        if (popupWindow.isShowing) {
            setAdapterFiltering(p0!!.id)
            popupWindow.dismiss()
        }
    }

    private fun setAdapterFiltering(res: Int) {
        when (res) {
            R.id.item_popup_filter_sort_with_level -> adapter.submitList(adapter.currentList.sortedBy { it.level })
            else -> adapter.submitList(adapter.currentList.sortedByDescending { it.dateTime })
        }
    }

    inner class MainRvDecorUtil(private val context : Context) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = ConvertUtil.dpToPx(context, 18)
        }
    }
}