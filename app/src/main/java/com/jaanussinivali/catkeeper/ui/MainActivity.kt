package com.jaanussinivali.catkeeper.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jaanussinivali.catkeeper.R
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.databinding.ActivityMainBinding
import com.jaanussinivali.catkeeper.ui.cat.CatCardsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: CatKeeperDatabase
    private val catDao: CatDao by lazy { database.getCatDao() }
//    private val catCardsFragment: CatCardsFragment = CatCardsFragment()
    private lateinit var catCardsFragments: List<CatCardsFragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        catCardsFragments = listOf(
//                CatCardsFragment.newInstance("Fragment 1"),


        )

        binding.pager.adapter = PagerAdapter(this, catCardsFragments)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = "Cat name"
        }.attach()

        database = CatKeeperDatabase.getDatabase(this)
    }

    inner class PagerAdapter(activity: FragmentActivity, private val catCardsFragments: List<Fragment>) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 4
        override fun createFragment(position: Int): Fragment = catCardsFragments[position]
    }
}