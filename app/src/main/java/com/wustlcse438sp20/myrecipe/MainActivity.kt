package com.wustlcse438sp20.myrecipe

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.wustlcse438sp20.myrecipe.Fragments.LoginFragment
import com.wustlcse438sp20.myrecipe.Fragments.SignUpFragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentAdapter: MyPagerAdapter
    private lateinit var globalVariable:MyApplication

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Calling Application class (see application tag in AndroidManifest.xml)
        globalVariable = applicationContext as MyApplication

        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter=fragmentAdapter
        tab_main.setupWithViewPager(viewPager)
            //identify login status
        val currentUser = auth.currentUser
        if (currentUser != null){
            //Set email in global/application context
            globalVariable.setEmail(currentUser?.email!!)
            Log.v("get from global var",globalVariable.getEmail())
            val intent = Intent(this,MainPageActivity::class.java)
            startActivity(intent)
        }
    }

    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount() : Int {
            return 2
        }

        override fun getItem(position: Int) : Fragment {
            return when (position) {
                0 -> {
                    LoginFragment()
                }
                else ->
                    SignUpFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Login"
                else ->"Sign Up"
            }
        }
    }

}
