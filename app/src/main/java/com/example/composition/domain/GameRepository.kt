package com.example.composition.domain

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOptions : Int
    ) : Question

    fun getGameSettings(level: Level) : GameSettings
}