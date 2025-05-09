package com.example.marketix.presentation.signup

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.R
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.domain.repository.UserRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.customToast
import com.example.marketix.util.isEmailValid
import com.example.marketix.util.isInternetAvailable
import com.example.marketix.util.showHTTPExceptionErrors
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

class SignupViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<SignupActivityListener>() {

    private val TAG = SignupViewModel::class.java.name

    lateinit var listener: SignupActivityListener
    private val compositeDisposable = CompositeDisposable()

    var isCalled = false

    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()

    //    1 for setting, 2 for preference and 3 for dashboard
    var loginCheck = MutableLiveData<Int>()

    fun registerNowClick() {
        if (!email.value!!.isEmailValid()) {
            context.customToast(context.resources.getString(R.string.please_enter_valid_email))
        } else if (!isInternetAvailable(context)) {
            context.customToast(context.resources.getString(R.string.internet_is_not_working))
        } else {
            listener.hideKeyboardListener()
            progressVisibility.postValue(true)
            val hashMap = HashMap<String, String>()
            hashMap[Constants.PARAM_NAME] = name.value!!
            hashMap[Constants.PARAM_EMAIL] = email.value!!
            hashMap[Constants.PARAM_PHONE] = phone.value!!
            hashMap[Constants.PARAM_ADDRESS] = address.value!!
            hashMap[Constants.PARAM_PASSWORD] = password.value!!
            hashMap[Constants.PARAM_CONFIRM_PASSWORD] = confirmPassword.value!!
            hashMap[Constants.PARAM_DEVICE_TOKEN] = preferencesRepository.getDeviceToken()

            userRepository.signupUser(
                preferencesRepository.getDeviceToken(), hashMap
            ).subscribeOn(schedulersProvider.io()).subscribe({
                it?.let {
                    context.customLog(it.toString())
                    context.customToast(it.message)
                    if (it.status == Constants.STATUS_OK) {
                        listener.openLoginActivity()
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
    }
    fun loginActivity(){
        listener.loginActiviry()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}