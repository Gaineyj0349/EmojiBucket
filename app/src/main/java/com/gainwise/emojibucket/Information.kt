package com.gainwise.emojibucket

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import spencerstudios.com.bungeelib.Bungee


class Information : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        val s = SpannableString(title)

        val upArrow = resources.getDrawable(R.drawable.ic_arrow_back_black_24dp)
        upArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)

        s.setSpan(ForegroundColorSpan(Color.BLACK), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = s
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        Bungee.windmill(this)
    }
}
