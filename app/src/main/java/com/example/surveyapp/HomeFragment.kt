package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class HomeFragment : Fragment(R.layout.fragment_home) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val leaderboardBtn = view.findViewById<CardView>(R.id.cardLeaderboard)
        val surveyBtn = view.findViewById<CardView>(R.id.cardSurveysCompleted)


        leaderboardBtn.setOnClickListener {
            val fragment = LeaderboardFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_container, fragment)?.commit()
        }


        return view
    }


}