package com.kuliah.pkm.tajwidify.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kuliah.pkm.tajwidify.databinding.ActivitySplashBinding
import com.kuliah.pkm.tajwidify.ui.auth.AuthActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnim()

        Handler().postDelayed({
            Intent(this, AuthActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }, 2600)
    }

    private fun playAnim() {
        val scaleX = ObjectAnimator.ofFloat(binding.ivLogo, View.SCALE_X, 0f, 1f).setDuration(1500)
        val scaleY = ObjectAnimator.ofFloat(binding.ivLogo, View.SCALE_Y, 0f, 1f).setDuration(1500)

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }
}