package com.nmy.knk.dto

data class MenuData(
    public val id: Int,
    public val date: String,
    public val doWeek: String,
    public val lunchCorner1: List<String>,
    public val lunchCorner2: List<String>,
    public val dinnerCorner1: List<String>
)
