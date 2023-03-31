package com.goormthon.mang9rme.jihun.util

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd

object ObjectAnimator {

    fun getStartAnim(view : View) : ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
            .setDuration(500)
    }

    fun getCloseAnim(view : View) : ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            .setDuration(500)
    }
}