package com.example.marketix.presentation.editaccount

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.R
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.User
import com.example.marketix.domain.repository.PreferencesRepository
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

class EditAccountViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<EditAccountActivityListener>() {

    private val TAG = EditAccountViewModel::class.java.name

    lateinit var listener: EditAccountActivityListener
    private val compositeDisposable = CompositeDisposable()

    var email = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()
    var profileImage = MutableLiveData<String>()

    lateinit var modelData: User

    fun loadProfileData() {
        modelData =
            Gson().fromJson(preferencesRepository.getUserProfile(), User::class.java)

        email.value = modelData.email
        address.value = modelData.address
        userName.value = modelData.name
        phoneNumber.value = modelData.phone
        profileImage.value = modelData.image

    }

    fun backPressClick() {
        listener.openBackActivity()
    }

    fun chooseImageClick() {
        listener.chooseImageClick()
    }

    fun updateProfileClick() {
        if (!email.value!!.isEmailValid()) {
            context.customToast(context.resources.getString(R.string.please_enter_valid_email))
        } else if (!isInternetAvailable(context)) {
            context.customToast(context.resources.getString(R.string.internet_is_not_working))
        } else {
            listener.hideKeyboardListener()
            progressVisibility.postValue(true)
            val hashMap = HashMap<String, String>()
            hashMap[Constants.PARAM_NAME] = userName.value!!
            hashMap[Constants.PARAM_EMAIL] = email.value!!
            hashMap[Constants.PARAM_PHONE] = phoneNumber.value!!
            hashMap[Constants.PARAM_ADDRESS] = address.value!!
            hashMap[Constants.PARAM_IMAGE] = profileImage.value!!

            userRepository.editProfile(
                preferencesRepository.getDeviceToken(),
                preferencesRepository.getAccessToken(),
                hashMap
            ).subscribeOn(schedulersProvider.io()).subscribe({
                it?.let {
                    progressVisibility.postValue(false)
                    context.customLog(it.toString())
                    context.customToast(it.message)
                    if (it.status == Constants.STATUS_OK) {
                        modelData.name = userName.value!!
                        modelData.email = email.value!!
                        modelData.phone = phoneNumber.value!!
                        modelData.address = address.value!!
                        modelData.image = profileImage.value!!
                        preferencesRepository.setUserProfile(Gson().toJson(modelData))
                        listener.openUpdateProfileActivity()
                    } else {
                        if (it.message.contains(Constants.UNAUTHENTICATED_ACCESS)) {
                            preferencesRepository.setAccessToken("")
                            preferencesRepository.setSplashCheck(1)
                            listener.displayMessageListener(it.message)
                        } else
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
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}