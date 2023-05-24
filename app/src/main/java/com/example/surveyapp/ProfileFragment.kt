package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ProfileFragment : Fragment() {
    private lateinit var shareButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        val bundle = arguments
        val fullName = bundle!!.getString("fullName")
        val email = bundle.getString("email")
        val score = bundle.getString("score")


        val fullNameText: TextView = view.findViewById(R.id.fullName)
        val emailText: TextView = view.findViewById(R.id.email)
        val scoreText: TextView = view.findViewById(R.id.score)

        fullNameText.text = fullName
        emailText.text = email
        scoreText.text = score

//        val shareButton :Button = view.findViewById<Button>(R.id.shareButton)
//        shareButton.setOnClickListener {
//            val sendIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, "I have scored $score points in the Survey App!")
//                type = "text/plain"
//            }
//
//            val shareIntent = Intent.createChooser(sendIntent, null)
//            startActivity(shareIntent)
//        }


        return view
    }

}