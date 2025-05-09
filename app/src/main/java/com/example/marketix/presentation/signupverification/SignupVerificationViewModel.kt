package com.example.marketix.presentation.signupverification

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.domain.repository.SettingsRepository
import com.example.marketix.domain.repository.UserRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import com.example.marketix.util.showHTTPExceptionErrors
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

class SignupVerificationViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<SignupVerificationActivityListener>() {

    private val TAG = SignupVerificationViewModel::class.java.name

    lateinit var listener: SignupVerificationActivityListener
    private val compositeDisposable = CompositeDisposable()

    var isCalled = false

    var otpCode = MutableLiveData<String>()
    var email = MutableLiveData<String>()

    //    1 for setting, 2 for preference and 3 for dashboard
    var loginCheck = MutableLiveData<Int>()

    fun getOTPCode() {
        listener.hideKeyboardListener()
        progressVisibility.postValue(true)

        userRepository.sendEmailVerification(
            preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken()
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                context.customLog(it.toString())
                progressVisibility.postValue(false)
                listener.displayMessageListener(it.message)
                if (it.status == Constants.STATUS_OK) {
                    context.customToast(it.message, 1)
                } else {
                    context.customToast(it.message)
                }
            }
        }, {
            context.customLog(it.toString())
            progressVisibility.postValue(false)
            context.showHTTPExceptionErrors(it as HttpException)
        }).let {
            compositeDisposable.add(it)
        }
    }

    fun verifyNowClick() {
        listener.hideKeyboardListener()
        progressVisibility.postValue(true)
        val hashMap = HashMap<String, String>()
        hashMap[Constants.PARAM_TOKEN] = otpCode.value!!

        userRepository.verifyUserEmail(
            preferencesRepository.getDeviceToken(),
            preferencesRepository.getAccessToken(),
            hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                progressVisibility.postValue(false)
                context.customLog(it.toString())
                context.customToast(it.message)
                if (it.status == Constants.STATUS_OK) {
                    loadAllSettings()
                }
            }
        }, {
            context.customLog(it.toString())
            progressVisibility.postValue(false)
            context.showHTTPExceptionErrors(it as HttpException)
        }).let {
            compositeDisposable.add(it)
        }
    }

    private fun loadAllSettings() {
        listener.hideKeyboardListener()
        progressVisibility.postValue(true)

        settingsRepository.loadAllSettings(
            preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken()
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                progressVisibility.postValue(false)
                context.customLog(it.toString())
                if (it.status == Constants.STATUS_OK) {
                    preferencesRepository.setAppSettings(Gson().toJson(it))
                }
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