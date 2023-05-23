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
import com.example.surveyapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    lateinit var toggleNav: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var preferences: SharedPreferences
    private var database: FirebaseDatabase? = null
    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var score: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //getting the user phone number from shared preferences
        val userPhone = intent.getStringExtra("phone").toString()


        //getting all data from the user in the database
        try {
            database = FirebaseDatabase.getInstance("https://surveyapp-837e7-default-rtdb.europe-west1.firebasedatabase.app/")
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreate: ", e)
        }

//if the user is not logged in, redirect to login activity
//        if (userPhone == null) {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }


        binding = ActivityMainBinding.inflate(layoutInflater)

        drawerLayout = findViewById(R.id.drawerLayout)

        var navView : NavigationView = findViewById(R.id.nav_view)

        database?.reference?.child("users")?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(userPhone)){
                    fullName = snapshot.child(userPhone).child("fullName").value.toString()
                    email = snapshot.child(userPhone).child("email").value.toString()
                    score = snapshot.child(userPhone).child("score").value.toString()
                    Toast.makeText(this@MainActivity, "Welcome " + fullName.toString(), Toast.LENGTH_SHORT).show()
                    val headerView = navView.getHeaderView(0)
                    val userFullName = headerView.findViewById(R.id.userName) as TextView
                    val userEmail = headerView.findViewById(R.id.userEmail) as TextView

                    userFullName.setText(fullName)
                    userEmail.setText(email)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled: ", error.toException())
            }
        })

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
                    replaceFragment(ProfileFragment(), it.title.toString())
                    true
                }
                R.id.navLogout-> {
                    preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
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
}