package com.example.marketix.domain.repository

import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.model.SigninUserResponse
import io.reactivex.Single

interface AccountsRepository {

    fun logoutCall(
        params: String,
        deviceToken: String
    ): Single<CommonResponse>

    fun updatePasswordCall(
        deviceToken: String,
        accessToken: String,
        hashMap: HashMap<String, String>
    ): Single<CommonResponse>


}