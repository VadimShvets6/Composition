package com.example.composition.domain.usecase

import com.example.composition.domain.GameRepository
import com.example.composition.domain.entity.Question

class GenerateQuestionUseCase(
    private val repository : GameRepository
) {

    operator fun invoke(maxSumValue : Int) : Question{
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object{
        private const val COUNT_OF_OPTIONS = 6
    }
}