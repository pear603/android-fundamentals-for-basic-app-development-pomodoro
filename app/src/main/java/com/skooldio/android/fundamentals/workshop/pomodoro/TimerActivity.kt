package com.skooldio.android.fundamentals.workshop.pomodoro

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skooldio.android.fundamentals.workshop.pomodoro.config.NotificationConfig
import com.skooldio.android.fundamentals.workshop.pomodoro.data.PomodoroCounter
import com.skooldio.android.fundamentals.workshop.pomodoro.databinding.ActivityTimerBinding
import kotlin.getValue



class TimerActivity : AppCompatActivity() {

    private val pomodoroCounter = PomodoroCounter()
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
        setupView()
        setupPomodoroCounter()

//        binding.buttonPlay.setOnClickListener {
//            showNotification(
//                "science",
//                "Hello from android development course"
//            )
//        }
    }

    private fun restoreBundle() {
        config = intent.getParcelableExtra(EXTRA_CONFIG)

    }


    private fun showNotification(title: String, text: String) {
        val notification: Notification = NotificationCompat.Builder(
            this,
            NotificationConfig.CHANNEL_ID
        ).apply {
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(R.drawable.ic_notification)
        }.build()

        val manager = NotificationManagerCompat.from(this)
        manager.notify(0, notification)
    }

    private fun setupView() {
        binding.buttonPlay.setOnClickListener {
            binding.buttonPlay.visibility = View.GONE
            binding.buttonPause.visibility = View.VISIBLE
            pomodoroCounter.play()
        }
        binding.buttonPause.setOnClickListener {
            binding.buttonPlay.visibility = View.VISIBLE
            binding.buttonPause.visibility = View.GONE
            pomodoroCounter.pause()
        }
        binding.buttonFastForward.setOnClickListener {
            binding.buttonPlay.visibility = View.VISIBLE
            binding.buttonPause.visibility = View.GONE
            pomodoroCounter.fastForward()
        }
    }
    private fun setupPomodoroCounter() {
        config?.let {
            pomodoroCounter.apply {
                config(
                    workDuration = it.workDuraton,
                    shortBreakDuration = it.shortBreakDuration,
                    longBreakDuration = it.longBreakDuration
                )
                setListener(
                    onReady = { minute, second ->
                        updateCounterStatus(State.Ready, minute, second)
                    },
                    onWork = { minute, second ->
                        updateCounterStatus(State.Work, minute, second)
                    },
                    onShortBreak = { minute, second ->
                        updateCounterStatus(State.ShortBreak, minute, second)
                    },
                    onLongBreak = { minute, second ->
                        updateCounterStatus(State.LongBreak, minute, second)
                    }
                )
            }
        }
    }

    sealed class State {
        object Ready: State()
        object Work: State()
        object ShortBreak: State()
        object LongBreak: State()
    }

    private fun updateCounterStatus(state: State, minute: Int, second: Int) {
        binding.textViewState.setText(getStateTextResourceId(state))
        binding.textViewMinute.text = getString(R.string.time_value, minute)
        binding.textViewSecond.text = getString(R.string.time_value, second)

        if (minute == 0 && second == 0){
            onPomodoroCounterFinished(state)
        }
    }

    private fun getStateTextResourceId(state: State): Int {
        return when(state) {
            State.Ready -> R.string.ready
            State.Work -> R.string.state_work
            State.ShortBreak -> R.string.state_short_break
            State.LongBreak -> R.string.state_long_break
        }
    }

    private fun onPomodoroCounterFinished(state: State) {
        when(state){
            State.Ready -> {}
            State.Work -> showNotification(
                title = getString(R.string.notification_time_up_title),
                text = getString(R.string.notification_time_up_text_work)
            )
            State.ShortBreak -> showNotification(
                title = getString(R.string.notification_time_up_title),
                text = getString(R.string.notification_time_up_text_short_break)
            )
            State.LongBreak -> showNotification(
                title = getString(R.string.notification_time_up_title),
                text = getString(R.string.notification_time_up_text_long_break)
            )
        }
    }

}
