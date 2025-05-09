package com.example.marketix.data.repository

import com.example.marketix.data.mapper.UserMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.model.SigninUserResponse
import com.example.marketix.domain.model.VerifyForgetPasswordResponse
import com.example.marketix.domain.repository.UserRepository
import com.example.marketix.util.Constants
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepositoryImp(
    private val retrofitService: RetrofitService, val mapper: dagger.Lazy<UserMapper>
) : UserRepository {

    override fun signinUser(
        deviceToken: String, hashMap: HashMap<String, String>
    ): Single<SigninUserResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        hashMap[Constants.PARAM_EMAIL]?.let { builder.addFormDataPart(Constants.PARAM_EMAIL, it) }
        hashMap[Constants.PARAM_PASSWORD]?.let {
            builder.addFormDataPart(Constants.PARAM_PASSWORD, it)
        }

        val requestBody: RequestBody = builder.build()

        return retrofitService.signinUser(deviceToken, requestBody).map {
            mapper.get().getUserMapping(it)
        }
    }

    override fun signupUser(
        deviceToken: String, hashMap: HashMap<String, String>
    ): Single<CommonResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart(Constants.PARAM_NAME, hashMap.get(Constants.PARAM_NAME).toString())
        builder.addFormDataPart(
            Constants.PARAM_EMAIL, hashMap.get(Constants.PARAM_EMAIL).toString()
        )
        builder.addFormDataPart(
            Constants.PARAM_PHONE, hashMap.get(Constants.PARAM_PHONE).toString()
        )
        builder.addFormDataPart(
            Constants.PARAM_ADDRESS, hashMap.get(Constants.PARAM_ADDRESS).toString()
        )
        builder.addFormDataPart(
            Constants.PARAM_PASSWORD, hashMap.get(Constants.PARAM_PASSWORD).toString()
        )
        builder.addFormDataPart(
            Constants.PARAM_CONFIRM_PASSWORD,
            hashMap.get(Constants.PARAM_CONFIRM_PASSWORD).toString()
        )
        builder.addFormDataPart(
            Constants.DEVICE_TOKEN, hashMap.get(Constants.DEVICE_TOKEN).toString()
        )

        val requestBody: RequestBody = builder.build()

        return retrofitService.signupUser(deviceToken, requestBody).map {
            mapper.get().getSignupUserMapping(it)
        }
    }

    override fun verifyUserEmail(
        deviceToken: String, accessToken: String, hashMap: HashMap<String, String>
    ): Single<CommonResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        hashMap.get(Constants.PARAM_TOKEN)
            ?.let { builder.addFormDataPart(Constants.PARAM_TOKEN, it) }

        val requestBody: RequestBody = builder.build()

        return retrofitService.sendVerifyEmailOTP(deviceToken, accessToken, requestBody).map {
            mapper.get().getCommonMapping(it)
        }
    }

    override fun sendEmailVerification(
        deviceToken: String,
        accessToken: String,
    ): Single<CommonResponse> {
        return retrofitService.sendVerificationEmail(deviceToken, accessToken).map {
            mapper.get().getCommonMapping(it)
        }
    }

    override fun sendRecoveryEmail(
        hashMap: HashMap<String, String>
    ): Single<CommonResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        hashMap.get(Constants.PARAM_EMAIL)
            ?.let { builder.addFormDataPart(Constants.PARAM_EMAIL, it) }

        val requestBody: RequestBody = builder.build()

        return retrofitService.sendRecoveryEmail(requestBody).map {
            mapper.get().getCommonMapping(it)
        }
    }

    override fun sendVerifyForgetPassword(
        hashMap: HashMap<String, String>
    ): Single<VerifyForgetPasswordResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        hashMap.get(Constants.PARAM_EMAIL)
            ?.let { builder.addFormDataPart(Constants.PARAM_EMAIL, it) }
        hashMap.get(Constants.PARAM_TOKEN)
            ?.let { builder.addFormDataPart(Constants.PARAM_TOKEN, it) }

        val requestBody: RequestBody = builder.build()

        return retrofitService.sendVerifyForgetPassword(requestBody).map {
            mapper.get().getVerifyForgetPasswordMapping(it)
        }
    }

    override fun sendResetPassword(
        hashMap: HashMap<String, String>
    ): Single<CommonResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        hashMap.get(Constants.PARAM_EMAIL)
            ?.let { builder.addFormDataPart(Constants.PARAM_EMAIL, it) }
        hashMap.get(Constants.PARAM_TOKEN)
            ?.let { builder.addFormDataPart(Constants.PARAM_TOKEN, it) }
        hashMap.get(Constants.PARAM_PASSWORD)
            ?.let { builder.addFormDataPart(Constants.PARAM_PASSWORD, it) }
        hashMap.get(Constants.PARAM_CONFIRM_PASSWORD)
            ?.let { builder.addFormDataPart(Constants.PARAM_CONFIRM_PASSWORD, it) }

        val requestBody: RequestBody = builder.build()

        return retrofitService.sendResetPassword(requestBody).map {
            mapper.get().getCommonMapping(it)
        }
    }

    override fun editProfile(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<CommonResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        hashMap.get(Constants.PARAM_EMAIL)
            ?.let { builder.addFormDataPart(Constants.PARAM_EMAIL, it) }
        hashMap.get(Constants.PARAM_PHONE)
            ?.let { builder.addFormDataPart(Constants.PARAM_PHONE, it) }
        hashMap.get(Constants.PARAM_ADDRESS)
            ?.let { builder.addFormDataPart(Constants.PARAM_ADDRESS, it) }
        hashMap.get(Constants.PARAM_IMAGE)
            ?.let { builder.addFormDataPart(Constants.PARAM_IMAGE, it) }

        if (hashMap.containsKey(Constants.PARAM_IMAGE)) {
            val image = File(hashMap.get(Constants.PARAM_IMAGE))
            builder.addFormDataPart(
                Constants.PARAM_IMAGE, image.name,
                image.asRequestBody(MultipartBody.FORM)
            )
        }

        val requestBody: RequestBody = builder.build()

        return retrofitService.editProfile(deviceToken, accessToken, requestBody).map {
            mapper.get().getEditProfileMapping(it)
        }
    }

}