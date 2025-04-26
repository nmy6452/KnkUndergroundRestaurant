package com.nmy.knk.dto

data class MenuData(
    val id: Int,
    val date: String,
    val doWeek: String,
    val lunchCorner1: List<String>,
    val lunchCorner2: List<String>,
    val dinnerCorner1: List<String>
)
