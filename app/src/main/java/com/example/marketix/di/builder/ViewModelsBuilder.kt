package com.example.marketix.di.builder

import androidx.lifecycle.ViewModel
import com.example.marketix.di.ViewModelKey
import com.example.marketix.presentation.aboutus.AboutUsViewModel
import com.example.marketix.presentation.account.AccountViewModel
import com.example.marketix.presentation.announcement.AnnouncementViewModel
import com.example.marketix.presentation.courselearning.CourseLearningViewModel
import com.example.marketix.presentation.coursepayment.CoursePaymentViewModel
import com.example.marketix.presentation.dashboard.DashboardViewModel
import com.example.marketix.presentation.editaccount.EditAccountViewModel
import com.example.marketix.presentation.forgetpassword.ForgetPasswordViewModel
import com.example.marketix.presentation.fullimage.FullImageViewModel
import com.example.marketix.presentation.fullvideo.FullVideoViewModel
import com.example.marketix.presentation.history.HistoryViewModel
import com.example.marketix.presentation.learn_trading.LearnTradingViewModel
import com.example.marketix.presentation.login.LoginViewModel
import com.example.marketix.presentation.marketpayment.MarketPaymentViewModel
import com.example.marketix.presentation.onboarding.OnBoardingViewModel
import com.example.marketix.presentation.signals.MarketSignalsViewModel
import com.example.marketix.presentation.signup.SignupViewModel
import com.example.marketix.presentation.signupverification.SignupVerificationViewModel
import com.example.marketix.presentation.splash.SplashViewModel
import com.example.marketix.presentation.start_trading.StartTradingViewModel
import com.example.marketix.presentation.updatepassword.UpdatePasswordViewModel
import com.example.marketix.presentation.welcome.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnBoardingViewModel::class)
    abstract fun bindOnBoardingViewModel(onBoardingViewModel: OnBoardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel::class)
    abstract fun bindForgetPasswordViewModel(forgetPasswordViewModel: ForgetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun bindSignupViewModel(signupViewModel: SignupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupVerificationViewModel::class)
    abstract fun bindSignupVerificationViewModel(signupVerificationViewModel: SignupVerificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnnouncementViewModel::class)
    abstract fun bindAnnouncementViewModel(announcementViewModel: AnnouncementViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModel(historyViewModel: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullImageViewModel::class)
    abstract fun bindFullImageViewModel(fullImageViewModel: FullImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullVideoViewModel::class)
    abstract fun bindFullVideoViewModel(fullVideoViewModel: FullVideoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LearnTradingViewModel::class)
    abstract fun bindLearnTradingViewModel(learnTradingViewModel: LearnTradingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CourseLearningViewModel::class)
    abstract fun bindCourseLearningViewModel(courseLearningViewModel: CourseLearningViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoursePaymentViewModel::class)
    abstract fun bindCoursePaymentViewModel(coursePaymentViewModel: CoursePaymentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartTradingViewModel::class)
    abstract fun bindStartTradingViewModel(startTradingViewModel: StartTradingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MarketPaymentViewModel::class)
    abstract fun bindMarketPaymentViewModel(marketPaymentViewModel: MarketPaymentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MarketSignalsViewModel::class)
    abstract fun bindMarketSignalsViewModel(marketSignalsViewModel: MarketSignalsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdatePasswordViewModel::class)
    abstract fun bindUpdatePasswordViewModel(updatePasswordViewModel: UpdatePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel::class)
    abstract fun bindAboutUsViewModel(aboutUsViewModel: AboutUsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    abstract fun bindEditAccountViewModel(editAccountViewModel: EditAccountViewModel): ViewModel


}