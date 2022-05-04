package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GameSettings(
    val maxSumValue : Int,
    val minCountOfRightAnswer: Int,
    val minProcentOfRightAnswer : Int,
    val gameTimeInSeconds : Int
): Parcelable