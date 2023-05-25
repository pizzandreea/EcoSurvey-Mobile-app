package com.example.surveyapp

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Intent
import android.os.Bundle
import android.util.Property
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView

class SurveysFragment : Fragment() {
    private lateinit var revealButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_surveys, container, false)

        val button = view.findViewById<View>(R.id.blockView)
        button.setOnClickListener {
            animate(button, View.ALPHA, 1.0f, 0.0f, 500, LinearInterpolator())
            animate(
                button,
                View.TRANSLATION_Y,
                button.translationY,
                button.translationY + 100f,
                500,
                DecelerateInterpolator()
            )

        }


        return view
    }

    fun animate(button : View, property : Property<View, Float>, from : Float, to : Float,
                duration : Long, interpolator : TimeInterpolator) {

            val tY = ObjectAnimator.ofFloat(button, property, from, to)

            tY.duration = duration
            tY.interpolator = interpolator
            tY.start()



    }

}