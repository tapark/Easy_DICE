package com.teddypark.dice_for_board_game

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowMetrics
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.teddypark.dice_for_board_game.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val diceNumberList: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6)
    private val diceButtonList: MutableList<View> = mutableListOf()
    private val diceSumList: MutableList<Int> = MutableList(9) { 0 }

    private var bannerAdView: AdView? = null

    private val adSize: AdSize
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onPause() {
        super.onPause()
        bannerAdView?.pause()
    }

    override fun onResume() {
        super.onResume()
        bannerAdView?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAdView?.destroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        initBannerAdView()
        getDiceButtonList()
        initDiceButton()
        initButton()

    }

    private fun getDiceButtonList() {
        diceButtonList.add(binding.diceButton1)
        diceButtonList.add(binding.diceButton2)
        diceButtonList.add(binding.diceButton3)
        diceButtonList.add(binding.diceButton4)
        diceButtonList.add(binding.diceButton5)
        diceButtonList.add(binding.diceButton6)
        diceButtonList.add(binding.diceButton7)
        diceButtonList.add(binding.diceButton8)
        diceButtonList.add(binding.diceButton9)
    }

    private fun initDiceButton() {

        diceButtonList.forEachIndexed { index,  button ->
            button.setOnClickListener { view ->
                diceClickEvent(view, index)
            }
        }
    }

    private fun initButton() {
        binding.rollingButton.setOnClickListener {
            diceSumList.forEachIndexed { index, num ->
                if (num != 0) {
                    diceClickEvent(diceButtonList[index], index)
                }
            }
        }
        binding.resetButton.setOnClickListener {
            diceButtonList.forEachIndexed { index, view ->
                view.setBackgroundResource(R.drawable.dice_plus)
                diceSumList[index] = 0
            }
            initDiceCountTextView()
        }
    }

    private fun View.blinkAnimation(time : Long){
        visibility = View.VISIBLE
        startAnimation(
            AlphaAnimation(0.0f, 1.0f).apply {
                duration = time
                fillAfter = true
            })
    }

    private fun diceClickEvent(view: View, index: Int) {
        diceNumberList.shuffle()
        view.blinkAnimation(1000)
        diceSumList[index] = diceNumberList[0]
        when (diceNumberList[0]) {
            1 -> view.setBackgroundResource(R.drawable.yellow_dice1)
            2 -> view.setBackgroundResource(R.drawable.yellow_dice2)
            3 -> view.setBackgroundResource(R.drawable.yellow_dice3)
            4 -> view.setBackgroundResource(R.drawable.yellow_dice4)
            5 -> view.setBackgroundResource(R.drawable.yellow_dice5)
            else -> view.setBackgroundResource(R.drawable.yellow_dice6)
        }
        initDiceCountTextView()
    }

    private fun initDiceCountTextView() {
        binding.sumTextView.text = diceSumList.sum().toString()
        binding.diceText1.text = diceSumList.count { it == 1 }.toString()
        binding.diceText2.text = diceSumList.count { it == 2 }.toString()
        binding.diceText3.text = diceSumList.count { it == 3 }.toString()
        binding.diceText4.text = diceSumList.count { it == 4 }.toString()
        binding.diceText5.text = diceSumList.count { it == 5 }.toString()
        binding.diceText6.text = diceSumList.count { it == 6 }.toString()
    }

    private fun initBannerAdView() {


        // Create a new ad view.
        bannerAdView = AdView(this)
        bannerAdView?.adUnitId = "ca-app-pub-4608445788299748/6671701586"//"ca-app-pub-3940256099942544/9214589741"
        bannerAdView?.setAdSize(adSize)

        // Replace ad container with new ad view.
        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(bannerAdView)

        val adRequest = AdRequest.Builder().build()
        bannerAdView?.loadAd(adRequest)
    }

}