package com.androbeat.androbeatagent.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.repository.managers.ExtractorManager
import com.androbeat.androbeatagent.data.repository.managers.SensorManager
import com.androbeat.androbeatagent.presentation.tabFragments.ServicesFragment
import com.androbeat.androbeatagent.presentation.tabFragments.StatusFragment
import com.androbeat.androbeatagent.presentation.WindowUtils.setTransparentStatusBarAndNavigationBar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sensorManager: SensorManager

    @Inject
    lateinit var extractorManager: ExtractorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTransparentStatusBarAndNavigationBar(window)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager2)

        val adapter = ViewPagerAdapter(this, sensorManager, extractorManager)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Status"
                else -> "Services"
            }
        }.attach()
    }
}

class ViewPagerAdapter(
    activity: AppCompatActivity,
    private val sensorManager: SensorManager,
    private val extractorManager: ExtractorManager
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatusFragment()
            else -> ServicesFragment(sensorManager, extractorManager)
        }
    }
}