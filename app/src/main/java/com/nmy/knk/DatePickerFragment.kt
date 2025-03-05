package com.nmy.knk

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.nmy.knk.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerFragment : DialogFragment(), OnDateSetListener {
    private lateinit var binding: ActivityMainBinding

    private var listener: DatePickerListener? = null

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is DatePickerListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityMainBinding.inflate(layoutInflater)

        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val selectedCalendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val currentCalendar = Calendar.getInstance()

        // 미래 날짜인지 비교
        if (selectedCalendar.after(currentCalendar)) {
            Toast.makeText(activity, "미래 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val formattedDateWeek = String.format("%d-%02d", year, getWeekOfYear(year, month, day))
        listener?.updateImage(formattedDateWeek)

        val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = getKoreanDayOfWeek(dayOfWeek)

        val dateFormat = SimpleDateFormat("MM월 dd일", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedCalendar.time)
        listener?.updateDate("$formattedDate ($dayOfWeekString)")
    }

    private fun getWeekOfYear(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }
    // 요일을 한글로 변환하는 메서드
    fun getKoreanDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            Calendar.SUNDAY -> "일"
            else -> ""
        }
    }
}