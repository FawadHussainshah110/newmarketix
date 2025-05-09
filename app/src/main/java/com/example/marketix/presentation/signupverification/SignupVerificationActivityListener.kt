package com.example.marketix.presentation.signupverification

interface SignupVerificationActivityListener {

    fun openWelcomeActivity()
    fun hideKeyboardListener()
    fun displayMessageListener(message: String)

}