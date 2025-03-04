package com.nmy.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.nmy.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showBannerAd()
    }

    private fun showBannerAd() {
        // 애드몹 초기화
        MobileAds.initialize(this) {}

        // 테스트용 기기를 등록하여 광고 카운팅을 제외하도록 처리
        val testDeviceIds = listOf("My device ID1", "My Device ID2")
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
        )

        mAdView = binding.adView
        // 인터넷에서 광고를 불러옴
        val bannerAdRequest = AdRequest.Builder().build()
        // 광고를 배너에 표시
        mAdView.loadAd(bannerAdRequest)
        mAdView.adListener = object : AdListener() {
            // 광고 로딩이 성공했을때 처리
            override fun onAdLoaded() {
                Toast.makeText(applicationContext, "Banner Ad loaded", Toast.LENGTH_SHORT).show()
            }

            // 광고 로딩이 실패했을때 처리
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Toast.makeText(applicationContext, "Banner Ad loading failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}