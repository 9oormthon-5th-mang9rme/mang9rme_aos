package com.goormthon.mang9rme.jihun.presentation.ui.main

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

@BindingAdapter("setRadiusImage")
fun ImageView.setIV(path : Any?) {
    Glide.with(this).load(path)
        .transform(CenterCrop(), RoundedCorners(50)).into(this)
}

@BindingAdapter("setBlurBackground")
fun ImageView.setBlur(path : Any?) {
    Glide.with(this)
        .load(path)
        .centerCrop()
        .apply(RequestOptions.bitmapTransform(BlurTransformation(30, 3)))
        .into(this)
}