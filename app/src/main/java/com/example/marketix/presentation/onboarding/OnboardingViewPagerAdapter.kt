package com.example.marketix.presentation.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.marketix.R


class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.onboarding_title_1),
                context.resources.getString(R.string.onboarding_dec_1),
                R.drawable.onboarding_image_1_v
            )

            1 -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.onboarding_title_2),
                context.resources.getString(R.string.onboarding_dec_2),
                R.drawable.onboarding_image_2_v
            )

            else -> OnboardingFragment.newInstance(
                context.resources.getString(R.string.onboarding_title_3),
                context.resources.getString(R.string.onboarding_dec_3),
                R.drawable.onboarding_image_3_v
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}