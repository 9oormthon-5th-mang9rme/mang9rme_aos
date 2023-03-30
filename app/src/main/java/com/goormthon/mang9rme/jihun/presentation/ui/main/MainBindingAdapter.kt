package com.goormthon.mang9rme.jihun.presentation.ui.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

@BindingAdapter("setRadiusImage")
fun ImageView.setIV(path : Any?) {
    Glide.with(this).load(path)
        .transform(CenterCrop(), RoundedCorners(10)).into(this)
}