package com.example.marketix.domain.repository

import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.model.SigninUserResponse
import com.example.marketix.domain.model.VerifyForgetPasswordResponse
import io.reactivex.Single

interface UserRepository {

    fun signinUser(
        deviceToken: String,
        hashMap: HashMap<String, String>
    ): Single<SigninUserResponse>

    fun signupUser(
        deviceToken: String,
        hashMap: HashMap<String, String>
    ): Single<CommonResponse>

    fun verifyUserEmail(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<CommonResponse>

    fun sendEmailVerification(
        deviceToken: String,
        accessToken: String
    ): Single<CommonResponse>

    fun sendRecoveryEmail(
        hashMap: HashMap<String, String>
    ): Single<CommonResponse>

    fun sendVerifyForgetPassword(
        hashMap: HashMap<String, String>
    ): Single<VerifyForgetPasswordResponse>

    fun sendResetPassword(
        hashMap: HashMap<String, String>
    ): Single<CommonResponse>

    fun editProfile(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<CommonResponse>


}