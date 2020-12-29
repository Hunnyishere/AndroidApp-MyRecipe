package com.wustlcse438sp20.myrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wustlcse438sp20.myrecipe.Fragments.MealPlanFragment
import com.wustlcse438sp20.myrecipe.Fragments.ProfileFragment
import com.wustlcse438sp20.myrecipe.Fragments.RecipeFragment
import kotlinx.android.synthetic.main.activity_main_page.*


class MainPageActivity : AppCompatActivity() {
    private lateinit var fragmentAdapter: MyPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_page)
        fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager2.adapter=fragmentAdapter
        tab_mainpage.setupWithViewPager(viewPager2)
    }

    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount() : Int {
            return 3
        }

        override fun getItem(position: Int) : Fragment {
            return when (position) {
                0 -> {
                    RecipeFragment()
                }
                1 -> {
                    MealPlanFragment()
                }
                else ->ProfileFragment()

            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Recipes"
                1 -> "Meal Plan"
                else ->"Profile"
            }
        }
    }

}
