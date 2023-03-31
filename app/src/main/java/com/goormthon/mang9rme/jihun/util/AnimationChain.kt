package com.goormthon.mang9rme.jihun.util

import android.view.View
import android.view.animation.Animation

class AnimationChain private constructor(builder: Builder) {

    private val animation: Animation = builder.firstAnimation
    private val targetView: View = builder.targetView

    fun startAnimation() {
        targetView.startAnimation(animation)
    }

    class Builder {
        private val _animationList: MutableList<Animation> = mutableListOf()
        private var _targetView: View? = null

        val firstAnimation: Animation
            get() {
                if (_animationList.isEmpty()) {
                    throw IllegalStateException("You need to add at least one animation")
                }

                return _animationList.first()
            }

        val targetView: View
            get() {
                return _targetView ?: throw IllegalStateException("You need to call setView(View) first")
            }

        fun targetView(view: View): Builder {
            _targetView = view
            return this
        }

        fun addAnimation(nextAnimation: Animation): Builder {
            if (_animationList.isEmpty()) {
                _animationList.add(nextAnimation)
                return this
            }

            _animationList.last()
                .setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) = Unit
                    override fun onAnimationRepeat(animation: Animation?) = Unit
                    override fun onAnimationEnd(animation: Animation?) {
                        _targetView?.startAnimation(nextAnimation)
                    }
                })

            _animationList.add(nextAnimation)
            return this
        }

        fun build(): AnimationChain {
            if (_targetView == null) {
                throw IllegalStateException("You need to call setView(View) first")
            }

            if (_animationList.isEmpty()) {
                throw IllegalStateException("You need to add at least one animation")
            }

            return AnimationChain(this)
        }
    }
}