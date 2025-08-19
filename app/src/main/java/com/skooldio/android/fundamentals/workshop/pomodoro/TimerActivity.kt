package com.skooldio.android.fundamentals.workshop.pomodoro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skooldio.android.fundamentals.workshop.pomodoro.databinding.ActivityTimerBinding
import kotlin.getValue



class TimerActivity : AppCompatActivity() {


    private val binding: ActivityTimerBinding by lazy {
        ActivityTimerBinding.inflate(layoutInflater)
    }


private var config: Config? = null

    companion object {
        private const val EXTRA_CONFIG = "config"
        fun newIntent(
            context: Context,
            config:Config
        ): Intent {
            return Intent(context, TimerActivity::class.java).apply {
                putExtra(EXTRA_CONFIG, config)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        restoreBundle()
    }

    private fun restoreBundle() {
        config = intent.getParcelableExtra(EXTRA_CONFIG)

    }
}
