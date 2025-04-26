package com.nmy.knk

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.nmy.knk.databinding.ActivityMainBinding
import com.nmy.knk.dto.MenuData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity(), DatePickerListener {
    private lateinit var binding: ActivityMainBinding


    private val baseUrl = "http://211.187.162.165:9080/knk/menu/ocr/get/menu/date?date="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Date.text = getDateText()

        binding.Date.setOnClickListener {
            val dialogFragment: DialogFragment = DatePickerFragment()
            dialogFragment.show(supportFragmentManager, "datePicker")
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day)

        updateMenu(dateString = dateString)
    }

     override fun updateMenu(dateString: String) {
        lifecycleScope.launch {
            val menuData = getMenuData(dateString)
            if (menuData != null) {
                val menuText = buildMenuText(menuData)
                binding.menuTextView.text = menuText
            } else {
                binding.menuTextView.text = "메뉴 정보를 가져올 수 없습니다."
            }
        }
    }

    private suspend fun getMenuData(date: String): MenuData? {
        Log.d("baseUrl",baseUrl)
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl$date")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                Log.d("MenuData", response)

                val gson = Gson()
                return@withContext gson.fromJson(response, MenuData::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    private fun buildMenuText(menuData: MenuData): String {
        val builder = StringBuilder()

        builder.append("날짜: ${menuData.date} (${menuData.doWeek})\n\n")

        builder.append("[점심 1코너]\n")
        builder.append(menuData.lunchCorner1.joinToString(", "))
        builder.append("\n\n")

        builder.append("[점심 2코너]\n")
        builder.append(menuData.lunchCorner2.joinToString(", "))
        builder.append("\n\n")

        builder.append("[저녁 코너]\n")
        if (menuData.dinnerCorner1.isNotEmpty()) {
            builder.append(menuData.dinnerCorner1.joinToString(", "))
        } else {
            builder.append("메뉴 없음")
        }

        return builder.toString()
    }

    //private lateinit var mAdView: AdView
//    private final val baseUrl = "https://nmy6452.github.io/KnkUndergroundRestaurant/"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.Date.text = getDateText()
//
//        binding.Date.setOnClickListener(View.OnClickListener {
//            val dialogFragment: DialogFragment = DatePickerFragment()
//            dialogFragment.show(supportFragmentManager, "datePicker")
//        })
//
//        val calendar = Calendar.getInstance()
//
//        // Locale을 사용해 날짜를 포맷합니다 (예: 2025-09)
//        val year = calendar.get(Calendar.YEAR)
//        val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
//
//        // 날짜를 "2025-09" 형태로 만들기
//        val dateString = String.format(Locale.getDefault(), "%d-%02d", year, weekOfYear)
//
//        updateImage(dateString = dateString)
//
//    }
//
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun updateImage(dateString: String){
//        val imageView = binding.imageView
//
//        // 비동기적으로 이미지를 다운로드하고 ImageView에 설정
//        lifecycleScope.launch {
//
//            val image = getDateBasedImage(date=dateString)
//            if (image != null) {
//                // 이미지를 ImageView에 설정
//                imageView.setImageBitmap(image)
//                // 이미지 클릭 시 전체 화면으로 확대해서 보여주기
//                imageView.setOnClickListener {
//                    showFullScreenImage(image)  // 이미지 클릭 시 전체 화면 이미지 보여주기
//                }
//            } else {
//                // 이미지 다운로드 실패 시 처리
//                imageView.setImageBitmap(null)
//                Toast.makeText(getApplicationContext(), "이미지 다운로드 오류 발생", Toast.LENGTH_SHORT).show()  // Toast 객체 정의
//            }
//        }
    }

    override fun updateDate(str: String) {
        binding.Date.text = str
    }

    fun getDateText(): String {
        // 현재 날짜와 시간을 Calendar로 가져오기
        val calendar = Calendar.getInstance()

        // 요일을 한글로 변환하기
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = getKoreanDayOfWeek(dayOfWeek)

        // 날짜 포맷 정의
        val formatter = SimpleDateFormat("MM월 dd일", Locale.getDefault())
        val dateText = formatter.format(calendar.time)

        return "$dateText ($dayOfWeekString)"
    }

    // 요일을 한글로 변환하는 메서드
    private fun getKoreanDayOfWeek(dayOfWeek: Int): String {
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
//
//    fun getCurrentWeekOfYear(): Int {
//        // 현재 날짜와 시간을 Calendar로 가져오기
//        val calendar = Calendar.getInstance()
//        // 1년 중 몇 번째 주인지 가져오기
//        return calendar.get(Calendar.WEEK_OF_YEAR)
//    }
//
    // Coroutine을 사용한 비동기 이미지 다운로드
    suspend fun getDateBasedImage(date: String): Bitmap? {
        return withContext(Dispatchers.IO) {

            // URL을 형성합니다.
            val imageUrl = "$baseUrl$date.jpg"

            Log.d("imageUrl",imageUrl)

            // 이미지를 비동기적으로 다운로드하여 반환합니다.
            return@withContext downloadImage(imageUrl)
        }

    }

    // 전체 화면에 이미지를 띄우는 함수
    private fun showFullScreenImage(image: Bitmap) {
        val fragment = FullScreenImageDialogFragment.newInstance(image)
        fragment.show(supportFragmentManager, "fullScreenImageDialog")
    }


    fun downloadImage(url: String): Bitmap? {
        return try {
            (URL(url).openConnection() as? HttpURLConnection)?.run {
                connectTimeout = 5000
                readTimeout = 5000
                requestMethod = "GET"
                inputStream.use { BitmapFactory.decodeStream(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}