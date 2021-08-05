package com.teddypark.dice_for_board_game

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.teddypark.dice_for_board_game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val diceNumberList: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6)
    private val diceButtonList: MutableList<View> = mutableListOf()
    private val diceSumList: MutableList<Int> = MutableList(9) { 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

}