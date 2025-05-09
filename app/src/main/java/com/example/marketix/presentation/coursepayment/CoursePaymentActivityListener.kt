package com.example.marketix.presentation.coursepayment

interface CoursePaymentActivityListener {

    fun backPressActivity()
    fun openProfileActivity()
    fun openLoginActivity()
    fun displayMessageListener(message: String)

}