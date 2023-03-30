package com.goormthon.mang9rme.kimbsu.common.util

import android.content.Context
import android.util.TypedValue

object ConvertUtil {

    /**
     * @return dp를 px 단위로 변경한 값, Int & 내림처리
     */
    fun dpToPx(context: Context, dp: Int): Int = dpToPx(context, dp.toFloat()).toInt()

    /**
     * @return dp를 px 단위로 변경한 값, Float
     */
    fun dpToPx(context: Context, dp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

    /**
     * @return px을 dp 단위로 변경한 값, Int & 내림처리
     */
    fun pxToDp(context: Context, px: Int): Int = pxToDp(context, px.toFloat()).toInt()

    /**
     * @return px을 dp 단위로 변경한 값, Float
     */
    fun pxToDp(context: Context, px: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.resources.displayMetrics)
}