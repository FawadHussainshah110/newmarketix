package com.example.marketix.di.module

import com.example.marketix.presentation.aboutus.AboutUsActivity
import com.example.marketix.presentation.account.AccountActivity
import com.example.marketix.presentation.announcement.AnnouncementActivity
import com.example.marketix.presentation.courselearning.CourseLearningActivity
import com.example.marketix.presentation.coursepayment.CoursePaymentActivity
import com.example.marketix.presentation.dashboard.DashboardActivity
import com.example.marketix.presentation.editaccount.EditAccountActivity
import com.example.marketix.presentation.forgetpassword.ForgetPasswordActivity
import com.example.marketix.presentation.fullimage.FullImageActivity
import com.example.marketix.presentation.fullvideo.FullVideoActivity
import com.example.marketix.presentation.history.HistoryActivity
import com.example.marketix.presentation.learn_trading.LearnTradingActivity
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.marketpayment.MarketPaymentActivity
import com.example.marketix.presentation.onboarding.OnBoardingActivity
import com.example.marketix.presentation.signals.MarketSignalsActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.presentation.signupverification.SignupVerificationActivity
import com.example.marketix.presentation.splash.SplashActivity
import com.example.marketix.presentation.start_trading.StartTradingActivity
import com.example.marketix.presentation.updatepassword.UpdatePasswordActivity
import com.example.marketix.presentation.webpage.PaymentWebPageActivity
import com.example.marketix.presentation.welcome.WelcomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface ActivityModule {

    @ContributesAndroidInjector
    fun splashActivityInjector(): SplashActivity

    @ContributesAndroidInjector
    fun onBoardingActivityInjector(): OnBoardingActivity

    @ContributesAndroidInjector
    fun loginActivityInjector(): LoginActivity

    @ContributesAndroidInjector
    fun forgetPasswordActivityInjector(): ForgetPasswordActivity

    @ContributesAndroidInjector
    fun signupActivityInjector(): SignupActivity

    @ContributesAndroidInjector
    fun signupVerificationActivityInjector(): SignupVerificationActivity

    @ContributesAndroidInjector
    fun welcomeActivityInjector(): WelcomeActivity

    @ContributesAndroidInjector
    fun dashboardActivityInjector(): DashboardActivity

    @ContributesAndroidInjector
    fun announcementActivityInjector(): AnnouncementActivity

    @ContributesAndroidInjector
    fun historyActivityInjector(): HistoryActivity

    @ContributesAndroidInjector
    fun fullImageActivityInjector(): FullImageActivity

    @ContributesAndroidInjector
    fun fullVideoActivityInjector(): FullVideoActivity

    @ContributesAndroidInjector
    fun learnTradingActivityInjector(): LearnTradingActivity

    @ContributesAndroidInjector
    fun coursePaymentActivityInjector(): CoursePaymentActivity

    @ContributesAndroidInjector
    fun courseLearningActivityInjector(): CourseLearningActivity

    @ContributesAndroidInjector
    fun startTradingActivityInjector(): StartTradingActivity

    @ContributesAndroidInjector
    fun marketPaymentActivityInjector(): MarketPaymentActivity

    @ContributesAndroidInjector
    fun marketSignalsActivityInjector(): MarketSignalsActivity

    @ContributesAndroidInjector
    fun accountActivityInjector(): AccountActivity

    @ContributesAndroidInjector
    fun editAccountActivityInjector(): EditAccountActivity

    @ContributesAndroidInjector
    fun updatePasswordActivityInjector(): UpdatePasswordActivity

    @ContributesAndroidInjector
    fun aboutUsActivityInjector(): AboutUsActivity

    @ContributesAndroidInjector
    fun paymentWebPageActivityInjector(): PaymentWebPageActivity

}