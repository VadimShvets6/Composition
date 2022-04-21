package com.example.composition.domain.entity

data class GameSettings(
    val maxSumValue : Int,
    val minCountOfRightAnswer: Int,
    val minProcentOfRightAnswer : Int,
    val gameTimeInSeconds : Int
)