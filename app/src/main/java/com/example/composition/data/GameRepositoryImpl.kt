package com.example.composition.data

import com.example.composition.domain.GameRepository
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val rightAnswer = sum - visibleNumber
        val options = HashSet<Int>()
        options.add(rightAnswer)
        val from = max(rightAnswer - countOptions, MIN_ANSWER_VALUE)
        val to = min(maxSumValue, rightAnswer + countOptions)
        while (options.size < countOptions){
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.EASY -> GameSettings(
                10,
                10,
                40,
                60
            )
            Level.NORMAL -> GameSettings(
                20,
                20,
                60,
                40
            )
            Level.HARD -> GameSettings(
                30,
                30,
                80,
                40
            )
        }
    }
}