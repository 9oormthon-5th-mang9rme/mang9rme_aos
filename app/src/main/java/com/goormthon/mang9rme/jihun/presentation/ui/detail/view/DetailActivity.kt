package com.goormthon.mang9rme.jihun.presentation.ui.detail.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.databinding.ActivityDetailBinding
import com.goormthon.mang9rme.databinding.ItemCardStoneStatBinding
import com.goormthon.mang9rme.jihun.presentation.ui.detail.viewmodel.DetailViewModel
import com.goormthon.mang9rme.jihun.presentation.ui.main.view.MapDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel : DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        initBinding()
        observe()
    }

    fun dismiss() = finish()

    private fun initBinding() {
        binding.activity = this
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

    private fun initIncludeView(data : StoneData) {
        binding.cardStoneStat.apply {
            itemCardStoneStatTvStoneName.text = data.stoneName

            itemCardStoneStatProgressAttackStat.progress = data.attack
            itemCardStoneStatProgressDefenceStat.progress = data.defense
            itemCardStoneStatProgressMagicStat.progress = data.magicDefense


            itemCardStoneStatIvStoneCharacter.setImageResource(setImageIcons(data.stoneType, data.attack + data.defense + data.magicDefense))
        }
    }

    private fun setImageIcons(stoneType : String, totStat : Int) : Int {
        return when(stoneType) {
            "현무암" -> if(totStat >= 150) R.drawable.ic_strong_hyunmu else R.drawable.ic_weak_hyunmu
            "화산송이" -> if(totStat >= 150) R.drawable.ic_strong_hwasan else R.drawable.ic_weak_hwasan
            "화강암" -> if(totStat >= 150) R.drawable.ic_strong_hwagang else R.drawable.ic_weak_hwagang
            else -> if(totStat >= 150) R.drawable.ic_strong_sukhwae else R.drawable.ic_weak_sukhwae
        }
    }

    fun openMap() {
        showMapDialog()
    }

    private fun showMapDialog() {
        MapDialogFragment().show(
            supportFragmentManager, "MapDialog"
        )
    }
}