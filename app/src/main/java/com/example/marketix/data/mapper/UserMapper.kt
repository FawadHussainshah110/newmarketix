package com.example.marketix.data.mapper

import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.model.SigninUserResponse
import com.example.marketix.domain.model.VerifyForgetPasswordResponse
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun getUserMapping(response: SigninUserResponse): SigninUserResponse {
        return response
    }

    fun getSignupUserMapping(response: CommonResponse): CommonResponse {
        return response
    }

    fun getEditProfileMapping(response: CommonResponse): CommonResponse {
        return response
    }

    fun getCommonMapping(response: CommonResponse): CommonResponse {
        return response
    }

    fun getVerifyForgetPasswordMapping(response: VerifyForgetPasswordResponse): VerifyForgetPasswordResponse {
        return response
    }


}
