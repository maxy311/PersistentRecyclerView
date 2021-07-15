package com.stone.persistent.recyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stone.persistent.recyclerview.MainActivity2
import com.stone.persistent.recyclerview.fragment.FeedsFragment
import com.stone.persistent.recyclerview.fragment.FeedsFragment2

class FeedsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val activity: FragmentActivity = fragmentActivity

    override fun createFragment(position: Int): Fragment = if (activity is MainActivity2)
        FeedsFragment2()
    else
        FeedsFragment()

    override fun getItemCount(): Int = 5

}