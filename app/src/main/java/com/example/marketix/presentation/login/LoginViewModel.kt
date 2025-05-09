package com.example.marketix.presentation.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.R
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.DataOrEmptyArray
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.domain.repository.SettingsRepository
import com.example.marketix.domain.repository.UserRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import com.example.marketix.util.isEmailValid
import com.example.marketix.util.isInternetAvailable
import com.example.marketix.util.showHTTPExceptionErrors
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<LoginActivityListener>() {

    private val TAG = LoginViewModel::class.java.name

    lateinit var listener: LoginActivityListener
    private val compositeDisposable = CompositeDisposable()

    var isCalled = false

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    //    1 for setting, 2 for preference and 3 for dashboard
    var loginCheck = MutableLiveData<Int>()

    fun signupClick() {
        listener.openSignupActivity()
    }

    fun forgetPasswordClick() {
        listener.openForgetPasswordActivity()
    }

    fun loginClick() {
//        listener.openWelcomeActivity()
        if (!email.value!!.isEmailValid()) {
            context.customToast(context.resources.getString(R.string.please_enter_valid_email))
        } else if (!isInternetAvailable(context)) {
            context.customToast(context.resources.getString(R.string.internet_is_not_working))
        } else {
            listener.hideKeyboardListener()
            progressVisibility.postValue(true)
            val hashMap = HashMap<String, String>()
            hashMap[Constants.PARAM_EMAIL] = email.value!!
            hashMap[Constants.PARAM_PASSWORD] = password.value!!

            userRepository.signinUser(
                preferencesRepository.getDeviceToken(),
                hashMap
            )
                .subscribeOn(schedulersProvider.io())
                .subscribe({
                    it?.let {
                        context.customLog(it.toString())
                        if (it.status == Constants.STATUS_OK) {
                            val data: DataOrEmptyArray.Data = it.data as DataOrEmptyArray.Data
                            preferencesRepository.setAccessToken(data.user.token)
                            preferencesRepository.setUserProfile(Gson().toJson(data.user))
                            preferencesRepository.setCurrentUserId(data.user.id)
                            if (data.user.emailverified) {
                                loadAllSettings()
                            } else
                                listener.openUserVerificationActivity()
                        } else {
                            if (it.status == Constants.STATUS_ERROR) {
                                preferencesRepository.setSplashCheck(0)
                                preferencesRepository.setAccessToken("")
                                preferencesRepository.setCurrentUserId("")
                                preferencesRepository.setUserProfile("")
                                it.message.let { it1 -> context.customToast(it1) }
                            }
//                            else
//                                context.showResponseErrors(it)
                        }
                        progressVisibility.postValue(false)
                    }
                }, {
                    context.customLog(it.toString())
                    progressVisibility.postValue(false)
                    context.showHTTPExceptionErrors(it as HttpException)
                }).let {
                    compositeDisposable.add(it)
                }
        }
    }

    private fun loadAllSettings() {
        listener.hideKeyboardListener()
        progressVisibility.postValue(true)

        settingsRepository.loadAllSettings(
            preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken()
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
                    preferencesRepository.setAppSettings(Gson().toJson(it))
                }
                preferencesRepository.setSplashCheck(2)
                listener.openWelcomeActivity()

            }
        }, {
            context.customLog(it.toString())
            progressVisibility.postValue(false)
            context.showHTTPExceptionErrors(it as HttpException)
        }).let {
            compositeDisposable.add(it)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}