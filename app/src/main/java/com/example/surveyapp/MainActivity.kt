package com.example.surveyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.surveyapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    lateinit var toggleNav: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        drawerLayout = findViewById(R.id.drawerLayout)

        var navView : NavigationView = findViewById(R.id.nav_view)

        replaceFragment(HomeFragment(), "Home")

        toggleNav = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggleNav)
        toggleNav.syncState()

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
//                    R.id.navProfile-> {
//                        val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
//                        startActivity(intent)
//                        true
//                    }
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