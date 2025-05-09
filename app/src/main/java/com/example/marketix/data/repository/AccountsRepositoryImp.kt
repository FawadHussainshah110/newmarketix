package com.example.marketix.data.repository

import com.example.marketix.data.mapper.AccountsMapper
import com.example.marketix.data.source.remote.RetrofitService
import com.example.marketix.domain.model.CommonResponse
import com.example.marketix.domain.repository.AccountsRepository
import com.example.marketix.util.Constants
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AccountsRepositoryImp(
    private val retrofitService: RetrofitService, val mapper: dagger.Lazy<AccountsMapper>
) : AccountsRepository {

    override fun logoutCall(deviceToken: String, acessToken: String): Single<CommonResponse> {

        return retrofitService.logoutUser(deviceToken, acessToken).map {
            mapper.get().getLogoutMapping(it)
        }
    }

    override fun updatePasswordCall(
        deviceToken: String, accessToken: String, hashMap: HashMap<String, String>
    ): Single<CommonResponse> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart(
            Constants.PARAM_OLD_PASSWORD,
            hashMap.get(Constants.PARAM_OLD_PASSWORD).toString()
        )
        builder.addFormDataPart(
            Constants.PARAM_PASSWORD,
            hashMap.get(Constants.PARAM_PASSWORD).toString()
        )
        builder.addFormDataPart(
            Constants.PARAM_CONFIRM_PASSWORD,
            hashMap.get(Constants.PARAM_CONFIRM_PASSWORD).toString()
        )

        val requestBody: RequestBody = builder.build()

        return retrofitService.updatePassword(deviceToken, accessToken, requestBody).map {
            mapper.get().getUpdatePasswordMapping(it)
        }
    }

}