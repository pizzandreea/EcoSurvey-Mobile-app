package com.example.surveyapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private var database : FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        try {
            database = FirebaseDatabase.getInstance("https://surveyapp-837e7-default-rtdb.europe-west1.firebasedatabase.app/")
        }
        catch (e: Exception) {
            Log.e(TAG, "onCreate: ", e)
        }

        val loginNowBtn: TextView = findViewById(R.id.loginNowBtn)

        loginNowBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        performSignUp()

    }

    private fun performSignUp() {
        val email = findViewById<EditText>(R.id.emailRegister)
        val password = findViewById<EditText>(R.id.passwordRegister)
        val conPassword = findViewById<EditText>(R.id.conPasswordRegister)
        val fullName = findViewById<EditText>(R.id.fullNameRegister)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val phone = findViewById<EditText>(R.id.phoneRegister)

        registerBtn.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val conPasswordText = conPassword.text.toString()
            val fullNameText = fullName.text.toString()
            val phoneText = phone.text.toString()
            val score = 0

            if (emailText.isEmpty() || passwordText.isEmpty() || fullNameText.isEmpty() || phoneText.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (passwordText.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (passwordText != conPasswordText) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                database?.reference?.child("users")?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(phoneText)) {
                            Toast.makeText(this@RegisterActivity, "Phone number already exists", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            database?.reference?.child("users")?.child(phoneText)?.child("password")?.setValue(passwordText)
                            database?.reference?.child("users")?.child(phoneText)?.child("fullName")?.setValue(fullNameText)
                            database?.reference?.child("users")?.child(phoneText)?.child("email")?.setValue(emailText)
                            database?.reference?.child("users")?.child(phoneText)?.child("score")?.setValue(score)
                            Toast.makeText(this@RegisterActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled: ", error.toException())
                    }
                })


            }
        }
    }


}