package com.example.marketix.presentation.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.User
import com.example.marketix.domain.repository.PreferencesRepository
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<AccountActivityListener>() {

    private val TAG = AccountViewModel::class.java.name

    lateinit var listener: AccountActivityListener
    private val compositeDisposable = CompositeDisposable()

    var email = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()
    var profileImage = MutableLiveData<String>()

    fun loadProfileData() {
        val modelData =
            Gson().fromJson(preferencesRepository.getUserProfile(), User::class.java)

        email.value = modelData.email
        address.value = modelData.address
        userName.value = modelData.name
        phoneNumber.value = modelData.phone
        profileImage.value = modelData.image

    }

    fun logoutClick() {
        preferencesRepository.setAccessToken("")
        preferencesRepository.setUserProfile("")
        preferencesRepository.setSplashCheck(1)
        listener.openLogoutActivity()
    }

    fun backPressClick() {
        listener.openBackActivity()
    }

    fun editProfileClick() {
        listener.editProfileActivity()
    }

    fun aboutUsClick() {
        listener.openAboutUskActivity()
    }

    fun changePasswordClick() {
        listener.openChangePasswordActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}