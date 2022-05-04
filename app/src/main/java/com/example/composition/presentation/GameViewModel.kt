package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecase.GenerateQuestionUseCase
import com.example.composition.domain.usecase.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    val context = application

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formatTime = MutableLiveData<String>()
    val formatTime: LiveData<String>
        get() = _formatTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _procentOfRightAnswers = MutableLiveData<Int>()
    val procentOfRightAnswers: LiveData<Int>
        get() = _procentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughProcentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughProcentOfRightAnswers: LiveData<Boolean>
        get() = _enoughProcentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswer = 0
    private var countOfQuestion = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTime()
        generateQuestion()
        updateProgress()
    }

    private fun updateProgress() {
        val procent = calculateProcentOfRightAnswers()
        _procentOfRightAnswers.value = procent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswer,
            gameSettings.minCountOfRightAnswer
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswer >= gameSettings.minCountOfRightAnswer
        _enoughProcentOfRightAnswers.value = procent >= gameSettings.minProcentOfRightAnswer
    }

    private fun calculateProcentOfRightAnswers(): Int {
        if(countOfQuestion == null){
            return 0
        }
        return ((countOfRightAnswer / countOfQuestion.toDouble()) * 100).toInt()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = _question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswer++
        }
        countOfQuestion++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minProcentOfRightAnswer
    }

    private fun startTime() {
        timer = object :
            CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                _formatTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - minutes * SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCountOfRightAnswers.value == true && enoughProcentOfRightAnswers.value == true,
            countOfRightAnswer,
            countOfQuestion,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}