package com.cloudfiveapp.android.ui.common.networking

import com.cloudfiveapp.android.ui.common.data.ApiError
import okhttp3.ResponseBody
import retrofit2.Converter

public typealias ApiErrorConverter = Converter<ResponseBody, ApiError>
