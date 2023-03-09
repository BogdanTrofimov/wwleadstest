package com.example.wwleadstest

data class GameViewState(
    val currentScore: String,
    val currentAtk: Int,
    val isViewVisible: Boolean,
    val currentTime: Int,
    val currentProgress: Int,
    val youLoose: Boolean,
)
