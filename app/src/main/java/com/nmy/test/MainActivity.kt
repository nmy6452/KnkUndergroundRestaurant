package com.nmy.test

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nmy.test.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Date.text = getDateText()

        //showBannerAd()
    }

//    private fun showBannerAd() {
//        // 애드몹 초기화
//        MobileAds.initialize(this) {}
//
//        // 테스트용 기기를 등록하여 광고 카운팅을 제외하도록 처리
//        val testDeviceIds = listOf("My device ID1", "My Device ID2")
//        MobileAds.setRequestConfiguration(
//            RequestConfiguration.Builder()
//                .setTestDeviceIds(testDeviceIds)
//                .build()
//        )
//
//        mAdView = binding.adView
//        // 인터넷에서 광고를 불러옴
//        val bannerAdRequest = AdRequest.Builder().build()
//        // 광고를 배너에 표시
//        mAdView.loadAd(bannerAdRequest)
//        mAdView.adListener = object : AdListener() {
//            // 광고 로딩이 성공했을때 처리
//            override fun onAdLoaded() {
//                Toast.makeText(applicationContext, "Banner Ad loaded", Toast.LENGTH_SHORT).show()
//            }
//
//            // 광고 로딩이 실패했을때 처리
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                Toast.makeText(applicationContext, "Banner Ad loading failed", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }

    override fun onPause() {
        //mAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        //mAdView.resume()
    }

    override fun onDestroy() {
        //mAdView.destroy()
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

}