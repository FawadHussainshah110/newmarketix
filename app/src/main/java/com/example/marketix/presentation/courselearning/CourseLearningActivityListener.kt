package com.example.marketix.presentation.courselearning

interface CourseLearningActivityListener {

    fun backPressActivity()
    fun openProfileActivity()
    fun openLoginActivity()
    fun announcementActivity()
    fun historyActivity()
    fun startTradingActivity()
    fun dashboardActivity()
    fun displayMessageListener(message: String)

}