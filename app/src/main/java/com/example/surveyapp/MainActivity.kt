package com.example.surveyapp

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.surveyapp.Session.LoginPref
import com.example.surveyapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    lateinit var toggleNav: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var preferences: SharedPreferences
    private var database: FirebaseDatabase? = null
    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var score: String
    lateinit var session: LoginPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session = LoginPref(this)

        getFCMToken()
        //getting the user phone number from shared preferences
        val userPhone = intent.getStringExtra("phone").toString()

        session.checkLogin()

        var user: HashMap<String, String> = session.getUserDetails()

        //getting all data from the user in the database
        try {
            database = FirebaseDatabase.getInstance("https://surveyapp-837e7-default-rtdb.europe-west1.firebasedatabase.app/")
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreate: ", e)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        drawerLayout = findViewById(R.id.drawerLayout)

        var navView : NavigationView = findViewById(R.id.nav_view)
        var bundle = Bundle()

        Log.d("userPhone", userPhone)

        val myRef = database?.reference?.child("users")
            val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(userPhone)){
                    fullName = snapshot.child(userPhone).child("fullName").value.toString()
                    email = snapshot.child(userPhone).child("email").value.toString()
                    score = snapshot.child(userPhone).child("score").value.toString()
                    Toast.makeText(this@MainActivity, "Welcome " + fullName.toString(), Toast.LENGTH_SHORT).show()
                    val headerView = navView.getHeaderView(0)
                    val userFullName = headerView.findViewById(R.id.userName) as TextView
                    val userEmail = headerView.findViewById(R.id.userEmail) as TextView

                    bundle = Bundle()
                    bundle.putString("fullName", fullName)
                    bundle.putString("email", email)
                    bundle.putString("score", score)


                    userFullName.setText(fullName)
                    userEmail.setText(email)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled: ", error.toException())
            }
        }

        myRef?.addValueEventListener(valueEventListener)

        replaceFragment(HomeFragment(), "Home")

        toggleNav = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggleNav)
        toggleNav.syncState()

        //setting the user data in the navigation drawer


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {
            it.isChecked=true

            when (it.itemId) {
                R.id.navHome -> {
                    replaceFragment(HomeFragment(), it.title.toString())
                    true
                }
                R.id.navLeaderboard -> {
                    replaceFragment(LeaderboardFragment(), it.title.toString())
                    true
                }
                R.id.navProfile-> {
                    // i want to send the user data to the profile fragment
                    val fragment = ProfileFragment()
                    fragment.arguments = bundle

                    replaceFragment(fragment, it.title.toString())
                    true
                }
                R.id.navLogout-> {
                    preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    session.logoutUser()
                    startActivity(intent)
                    true
                }
//                    R.id.navSurveys -> {
//                        val intent = Intent(this@HomeActivity, SurveysCompletedActivity::class.java)
//                        startActivity(intent)
//                        true
//                    }
//                    R.id.navQuizzes-> {
//                        val intent = Intent(this@HomeActivity, MainActivity::class.java)
//                        startActivity(intent)
//                        true
//                    }
                else -> false
            }
        }

    }

    private fun replaceFragment(fragmet: Fragment, title: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_container, fragmet)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleNav.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFCMToken(){
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(ContentValues.TAG, "Token: $token")
            }
        }
    }
}