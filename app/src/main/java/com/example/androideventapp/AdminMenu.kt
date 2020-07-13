package com.example.androideventapp


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import java.io.IOException

class AdminMenu : AppCompatActivity(){





    private val fragmentManager = supportFragmentManager
    private val userFragment = UserListFragment()
    private val eventFragment = EventListFragment()
    private val newUserFragment = NewUserFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_menu)

        val navigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationView)
        //val mainFrame: FrameLayout = findViewById<FrameLayout>(R.id.mainLayout)

        /* Display First Fragment initially */
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, userFragment)
        fragmentTransaction.commit()

        navigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId){
                R.id.nav_user -> {
                    item.isChecked = true
                    setFragment(userFragment)
                    true
                }
                R.id.nav_event -> {
                    //navigationView.itemBackgroundResource = R.color.colorPrimaryDark
                    item.isChecked = true
                    setFragment(eventFragment)
                    true
                }
                R.id.nav_new_user -> {
                    //navigationView.itemBackgroundResource = R.color.colorPrimaryDark
                    item.isChecked = true
                    setFragment(newUserFragment)
                    true
                }
            }
            false
        }


    }

    /*fun btnOne(v: View){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, userFragment)
        fragmentTransaction.commit()
    }

    fun btnTwo(v: View){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, eventFragment)
        fragmentTransaction.commit()
    }*/

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, fragment)
        fragmentTransaction.commit()
    }


}