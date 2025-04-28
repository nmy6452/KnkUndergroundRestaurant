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

    override fun onStart() {
        super.onStart()

        // 다이얼로그가 화면에 표시된 후 버튼 색상 설정
        val datePickerDialog = dialog as DatePickerDialog
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.MainActivityTitle, null))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.MainActivityTitle, null))
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

        // 선택된 날짜의 형식: "2025-04-26"
        val formattedDate = String.format("%d-%02d-%02d", year, month + 1, day)

        // 날짜에 맞는 메뉴 정보를 가져오기 위해 listener 호출
        listener?.updateMenu(formattedDate)

        val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = getKoreanDayOfWeek(dayOfWeek)

        val dateFormat = SimpleDateFormat("MM월 dd일", Locale.getDefault())
        val formattedDateText = dateFormat.format(selectedCalendar.time)
        listener?.updateDate("$formattedDateText ($dayOfWeekString)")
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