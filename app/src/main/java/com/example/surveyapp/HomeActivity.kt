package com.example.surveyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import com.example.surveyapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val leaderboardBtn = findViewById<CardView>(R.id.cardLeaderboard)
        val surveyBtn = findViewById<CardView>(R.id.cardSurveysCompleted)


        leaderboardBtn.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }

}