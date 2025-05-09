package com.example.marketix.presentation.forgetpassword

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

class ForgetPasswordViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<ForgetPasswordActivityListener>() {

    private val TAG = ForgetPasswordViewModel::class.java.name

    lateinit var listener: ForgetPasswordActivityListener
    private val compositeDisposable = CompositeDisposable()

    var isCalled = false

    var title = MutableLiveData<String>()
    var editTextTitle = MutableLiveData<String>()
    var detailDescription = MutableLiveData<String>()
    var otpCode = MutableLiveData<String>()
    var newPassword = MutableLiveData<String>()
    var newConfirmPassword = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var token = ""

    // 1 for email view, 2 for otp, 3 for new password
    var viewCondition = MutableLiveData<Int>()

    fun recoverPasswordClick() {
        if (viewCondition.value == 1) {
//            editTextTitle.postValue(context.resources.getString(R.string.text_email))
//            title.postValue(context.resources.getString(R.string.text_email_here))
//            detailDescription.postValue(context.resources.getString(R.string.text_forget_password_dec))
            sendRecoveryEmail()
        } else if (viewCondition.value == 2) {
//            title.postValue(context.resources.getString(R.string.enter_otp_pin))
//            editTextTitle.postValue(context.resources.getString(R.string.text_pin))
//            detailDescription.postValue(context.resources.getString(R.string.text_email_verification_dec))
            sendVerifyForgetPassword()
        } else if (viewCondition.value == 3) {
//            detailDescription.postValue(context.resources.getString(R.string.text_reset_password_dec))
//            title.postValue(context.resources.getString(R.string.enter_new_password))
            sendResetPassword()
        }
    }

    fun sendRecoveryEmail() {
        if (!email.value!!.isEmailValid()) {
            context.customToast(context.resources.getString(R.string.please_enter_valid_email))
        } else if (!isInternetAvailable(context)) {
            context.customToast(context.resources.getString(R.string.internet_is_not_working))
        } else {
            listener.hideKeyboardListener()
            progressVisibility.postValue(true)
            val hashMap = HashMap<String, String>()
            hashMap[Constants.PARAM_EMAIL] = email.value!!

            userRepository.sendRecoveryEmail(
                hashMap
            ).subscribeOn(schedulersProvider.io()).subscribe({
                it?.let {
                    progressVisibility.postValue(false)
                    context.customLog(it.toString())
//                    context.customToast(it.message)
                    listener.displayMessageListener(it.message)
                    if (it.status == Constants.STATUS_OK) {
                        viewCondition.postValue(2)
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

    fun sendVerifyForgetPassword() {
        listener.hideKeyboardListener()
        progressVisibility.postValue(true)
        val hashMap = HashMap<String, String>()
        hashMap[Constants.PARAM_EMAIL] = email.value!!
        hashMap[Constants.PARAM_TOKEN] = otpCode.value!!

        userRepository.sendVerifyForgetPassword(
            hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                progressVisibility.postValue(false)
                context.customLog(it.toString())
                context.customToast(it.message)
                if (it.status == Constants.STATUS_OK) {
                    token = it.data.token.toString()
                    viewCondition.postValue(3)
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

    fun sendResetPassword() {
        listener.hideKeyboardListener()
        progressVisibility.postValue(true)
        val hashMap = HashMap<String, String>()
        hashMap[Constants.PARAM_EMAIL] = email.value!!
        hashMap[Constants.PARAM_TOKEN] = token
        hashMap[Constants.PARAM_PASSWORD] = newPassword.value!!
        hashMap[Constants.PARAM_CONFIRM_PASSWORD] = newConfirmPassword.value!!

        userRepository.sendResetPassword(
            hashMap
        ).subscribeOn(schedulersProvider.io()).subscribe({
            it?.let {
                progressVisibility.postValue(false)
                context.customLog(it.toString())
                context.customToast(it.message)
                if (it.status == Constants.STATUS_OK) {
//                    viewCondition.postValue(3)
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

    override fun onCleared() {
        compositeDisposable.clear()
    }

}