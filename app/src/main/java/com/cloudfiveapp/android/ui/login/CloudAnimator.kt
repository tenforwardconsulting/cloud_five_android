package com.cloudfiveapp.android.ui.login

import android.animation.Animator
import android.animation.ValueAnimator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.util.nextFloatInRange
import java.util.*

class CloudAnimator(private val parentViewGroup: ViewGroup,
                    private val layoutInflater: LayoutInflater)
    : LifecycleObserver {

    companion object {
        private const val MIN_DURATION = 8000L
        private const val MAX_DURATION = 22000L

        private const val MIN_START_X = 0.25f
        private const val MAX_START_X = 0.75f
    }

    private val random = Random()

    private val animations = mutableListOf<ValueAnimator>()

    private val parentWidth
        get() = parentViewGroup.width.toFloat()

    private val parentHeight
        get() = parentViewGroup.height.toFloat()

    private val cloudLayoutIds = listOf(R.layout.cloud1, R.layout.cloud2, R.layout.cloud3,
            R.layout.cloud4, R.layout.cloud5, R.layout.cloud6, R.layout.cloud7)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initAnimations() {
        addRandomCloud(true)
        addRandomCloud(true)
        addRandomCloud(true)
        addRandomCloud(true)
        addRandomCloud()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumeAnimations() {
        animations.forEach { it.resume() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseAnimations() {
        animations.forEach { it.pause() }
    }

    private fun addRandomCloud(visible: Boolean = false) {
        val startFromLeft = random.nextBoolean()
        val speed = random.nextInt((MAX_DURATION - MIN_DURATION).toInt()) + MIN_DURATION

        val minValue = if (visible && startFromLeft) {
            random.nextFloatInRange(MIN_START_X, MAX_START_X)
        } else 0f

        val maxValue = if (visible && !startFromLeft) {
            random.nextFloatInRange(MIN_START_X, MAX_START_X)
        } else 1f

        val cloudLayoutId = cloudLayoutIds[random.nextInt(cloudLayoutIds.size)]
        val cloud = layoutInflater.inflate(cloudLayoutId, parentViewGroup, false)

        animations += ValueAnimator.ofFloat(minValue, maxValue).apply {
            duration = speed
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                val progress = if (startFromLeft) value else maxValue - value

                /*
                 * When the cloud view is first created, its width will be 0 before it is measured.
                 * Offset by the parent's width if the cloud has not been measured so that it does
                 * not appear on the screen yet. Otherwise, subtract the cloud's width so that it
                 * will float all the way off the screen before the animation finishes.
                 */
                val cloudWidthOffset = if (cloud.width == 0) parentWidth else -cloud.width.toFloat()
                val translationX = ((parentWidth + cloud.width) * progress) + cloudWidthOffset
                cloud.translationX = translationX

                /*
                 * Set translationY once here because the parent's or cloud's height might still be
                 * zero before this starts.
                 */
                if (parentHeight > 0 && cloud.height > 0 && cloud.translationY == 0f) {
                    cloud.translationY = random.nextFloatInRange(0.01f, parentHeight - cloud.height)
                }
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    animations.remove(animation)
                    parentViewGroup.removeView(cloud)
                    addRandomCloud()
                }

                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    // Add to index 0 to ensure clouds are behind all other views in parent.
                    parentViewGroup.addView(cloud, 0)
                }

                override fun onAnimationEnd(animation: Animator?) = Unit
                override fun onAnimationRepeat(animation: Animator?) = Unit
                override fun onAnimationCancel(animation: Animator?) = Unit
                override fun onAnimationStart(animation: Animator?) = Unit
            })

            start()
        }
    }
}
