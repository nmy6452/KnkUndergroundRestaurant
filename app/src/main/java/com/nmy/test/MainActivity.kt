package com.nmy.test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nmy.test.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private lateinit var mAdView: AdView
    private final val baseUrl = "https://nmy6452.github.io/KnkUndergroundRestaurant/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Date.text = getDateText()

        val imageView: ImageView = binding.imageView
        // 비동기적으로 이미지를 가져오고, 가져온 이미지를 ImageView에 설정합니다.

        // 비동기적으로 이미지를 다운로드하고 ImageView에 설정
        lifecycleScope.launch {
            val image = getDateBasedImage()
            if (image != null) {
                // 이미지를 ImageView에 설정
                imageView.setImageBitmap(image)
                // 이미지 클릭 시 전체 화면으로 확대해서 보여주기
                imageView.setOnClickListener {
                    showFullScreenImage(image)  // 이미지 클릭 시 전체 화면 이미지 보여주기
                }
            } else {
                // 이미지 다운로드 실패 시 처리
                println("이미지를 다운로드할 수 없습니다.")
                Toast.makeText(getApplicationContext(), "이미지 다운로드 오류 발생", Toast.LENGTH_SHORT).show()  // Toast 객체 정의
            }
        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
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

    fun getCurrentWeekOfYear(): Int {
        // 현재 날짜와 시간을 Calendar로 가져오기
        val calendar = Calendar.getInstance()
        // 1년 중 몇 번째 주인지 가져오기
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }

    // Coroutine을 사용한 비동기 이미지 다운로드
    suspend fun getDateBasedImage(): Bitmap? {
        return withContext(Dispatchers.IO) {
            // 현재 날짜를 가져옵니다
            val calendar = Calendar.getInstance()

            // Locale을 사용해 날짜를 포맷합니다 (예: 2025-09)
            val year = calendar.get(Calendar.YEAR)
            val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

            // 날짜를 "2025-09" 형태로 만들기
            val dateString = String.format(Locale.getDefault(), "%d-%02d", year, weekOfYear)

            // URL을 형성합니다.
            val imageUrl = "$baseUrl$dateString.jpg"

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
            // URL을 통해 연결 설정
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "GET"

            // 연결이 성공적이면 이미지 파일을 다운로드합니다.
            val inputStream: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // 다운로드한 이미지를 반환
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}