package com.nmy.knk

import java.time.Year

interface DatePickerListener {
    fun updateImage(dateString: String)
    fun updateMenu(dateString: String)
    fun updateDate(str :String)
}