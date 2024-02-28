package com.jaanussinivali.catkeeper.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
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
import java.security.cert.PKIXParameters
import kotlin.concurrent.thread

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

        database = CatKeeperDatabase.getDatabase(this)

        setFragments()
        binding.pager.adapter = PagerAdapter(this, catCardsFragments)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = "Cat ${position + 1}"
            tab.icon = AppCompatResources.getDrawable(this, R.drawable.ic_pets_24)
        }.attach()
    }

    private fun setFragments() {
        catCardsFragments = listOf(
            CatCardsFragment.newInstance("Fragment 1", 0),
            CatCardsFragment.newInstance("Fragment 2", 1)
        )
    }

    inner class PagerAdapter(
        activity: FragmentActivity,
        private val catCardsFragments: List<Fragment>
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount() = catCardsFragments.size
        override fun createFragment(position: Int): Fragment = catCardsFragments[position]

        fun getPageTitle(position: Int): String? {
            return catCardsFragments[position].tag
        }
    }
}