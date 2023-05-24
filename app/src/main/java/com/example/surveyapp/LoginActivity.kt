package com.example.surveyapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.surveyapp.Session.LoginPref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {


    private var database : FirebaseDatabase? = null
    lateinit var session : LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        printHashKey()

        session = LoginPref(this)
        if(session.isLoggedIn()){
            var i : Intent = Intent(applicationContext, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        try {
            database = FirebaseDatabase.getInstance("https://surveyapp-837e7-default-rtdb.europe-west1.firebasedatabase.app/")
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreate: ", e)
        }


        val registerText: TextView = findViewById(R.id.registerNowBtn)

        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        performSignIn()
    }

    private fun performSignIn() {
        val phone = findViewById<EditText>(R.id.phoneLogin)
        val password = findViewById<EditText>(R.id.passwordLogin)

        val loginBtn = findViewById<TextView>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val phoneText = phone.text.toString()
            val passwordText = password.text.toString()

            if (phoneText.isEmpty() || passwordText.isEmpty()) {

                Toast.makeText(this, "Please enter phone/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                database?.reference?.child("users")?.addListenerForSingleValueEvent(object : ValueEventListener {
                    @SuppressLint("CommitPrefEdits")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(phoneText)) {
//                            verificam daca parola pentru numarul de telefon din baza de date e aceeasi cu cea introdusa
                            val getPassword = snapshot.child(phoneText).child("password").value.toString()
                            if (getPassword == passwordText) {
                                Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                                val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("phone", phoneText)
                                editor.apply()


                                session.createLoginSession(phoneText)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("phone", phoneText)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this@LoginActivity, "Wrong password", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
    }

    fun printHashKey() {
        try {
            val info: PackageInfo = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures){
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(),0))
                Log.d(TAG, "printHashKey() Hash Key: $hashKey")
            }
        }
        catch (e: NoSuchAlgorithmException){
            Log.d(TAG, "printHashKey()", e )
        }
        catch (e: Exception){
            Log.d(TAG, "printHashKey()", e )
        }
    }
}