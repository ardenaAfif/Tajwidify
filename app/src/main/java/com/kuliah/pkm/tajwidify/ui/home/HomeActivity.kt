package com.kuliah.pkm.tajwidify.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.ActivityHomeBinding
import com.kuliah.pkm.tajwidify.ui.doa.DoaFragment
import com.kuliah.pkm.tajwidify.ui.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.doa -> {
                    replaceFragment(DoaFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentHomeView, fragment).commit()
    }
}