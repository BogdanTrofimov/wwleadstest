package com.example.wwleadstest

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wwleadstest.utils.Event

class SharedViewModel : ViewModel() {
    private var countDownTimer: CountDownTimer? = null
    private var gameViewState = GameViewState("0", 1, true, 30, 100, false)

    private val _timeCounterLivedata = MutableLiveData<Event<GameViewState>>()
    val sendPingDialogStateLiveData: LiveData<Event<GameViewState>> get() = _timeCounterLivedata

    fun setupTimer() {
        val duration = gameViewState.currentTime
        val interval = 1000 // интервал обновления в миллисекундах
        countDownTimer =
            object : CountDownTimer((duration * 1000).toLong(), interval.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    val progress = (millisUntilFinished.toFloat() / (duration * 1000) * 100).toInt()
                    gameViewState = gameViewState.copy(
                        currentTime = millisUntilFinished.toInt() / 1000,
                        currentProgress = progress
                    )
                    if (gameViewState.currentScore.toInt() >= 20) {
                        this.cancel()
                        gameViewState =
                            gameViewState.copy(
                                isViewVisible = false,
                                currentProgress = 0,
                                currentScore = "Win!"
                            )
                    }
                    _timeCounterLivedata.value = Event(gameViewState)
                }

                override fun onFinish() {
                    gameViewState = gameViewState.copy(youLoose = true)
                    _timeCounterLivedata.value = Event(gameViewState)
                    allClear()
                }
            }

        (countDownTimer as CountDownTimer).start()
    }

    fun onCounterButtonClicked() {
        gameViewState =
            gameViewState.copy(currentScore = (gameViewState.currentScore.toInt() + gameViewState.currentAtk).toString())
        _timeCounterLivedata.value = Event(gameViewState)
    }

    fun allClear() {
        countDownTimer?.cancel()
        gameViewState =
            gameViewState.copy(currentScore = "0", isViewVisible = true, youLoose = false)
        _timeCounterLivedata.value = Event(gameViewState)
    }

    fun increaseAtk() {
        if (gameViewState.currentAtk < 5) {
            gameViewState = gameViewState.copy(currentAtk = gameViewState.currentAtk.inc())
            _timeCounterLivedata.value = Event(gameViewState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        allClear()
    }

    fun resetAtk() {
        gameViewState = gameViewState.copy(currentAtk = 1)
        _timeCounterLivedata.value = Event(gameViewState)
    }

    fun setEasyLevel() {
        gameViewState =
            gameViewState.copy(currentScore = "0", isViewVisible = true, currentTime = 100)
        _timeCounterLivedata.value = Event(gameViewState)
    }

    fun setMediumLevel() {
        gameViewState =
            gameViewState.copy(currentScore = "0", isViewVisible = true, currentTime = 50)
        _timeCounterLivedata.value = Event(gameViewState)
    }

    fun setHardLevel() {
        gameViewState =
            gameViewState.copy(currentScore = "0", isViewVisible = true, currentTime = 25)
        _timeCounterLivedata.value = Event(gameViewState)
    }

    fun retry() {
        gameViewState =
            gameViewState.copy(
                currentScore = "0",
                isViewVisible = true,
                currentTime = gameViewState.currentTime,
                currentProgress = 100,
                youLoose = false
            )
        countDownTimer?.start()
        _timeCounterLivedata.value = Event(gameViewState)

    }
}