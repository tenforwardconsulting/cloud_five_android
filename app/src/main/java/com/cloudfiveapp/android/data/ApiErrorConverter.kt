package com.cloudfiveapp.android.data

import com.cloudfiveapp.android.data.model.ApiError
import okhttp3.ResponseBody
import retrofit2.Converter

typealias ApiErrorConverter = Converter<ResponseBody, ApiError>
