package com.example.palliativecare.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.example.palliativecare.R
import com.example.palliativecare.databinding.ActivityDoctorBottomNavigationBarBinding
import com.example.palliativecare.view.fragment.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DoctorBottomNavigationBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorBottomNavigationBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_bottom_navigation_bar)

        binding = ActivityDoctorBottomNavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val chatFragment = ChatFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.chat -> setCurrentFragment(chatFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.doctor_app_action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.arabic -> {
                val localeList = LocaleListCompat.forLanguageTags("ar")
                AppCompatDelegate.setApplicationLocales(localeList)
                true
            }
            R.id.english -> {
                val localeList = LocaleListCompat.forLanguageTags("en")
                AppCompatDelegate.setApplicationLocales(localeList)
                true
            }
            R.id.logout -> {
                Firebase.auth.signOut()
                true
            }
            R.id.add_topic -> {
                val intent = Intent(applicationContext,AddTopicActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}