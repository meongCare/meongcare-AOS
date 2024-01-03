package com.project.meongcare.home.model.data.remote

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Inject

class HomeRetrofitClient
    @Inject
    constructor() {
        val homeApi: HomeApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://dev.meongcare.com")
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HomeApi::class.java)
        }

        private val nullOnEmptyConverterFactory =
            object : Converter.Factory() {
                fun converterFactory() = this

                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<out Annotation>,
                    retrofit: Retrofit,
                ) = object : Converter<ResponseBody, Any?> {
                    val nextResponseBodyConverter =
                        retrofit.nextResponseBodyConverter<Any?>(
                            converterFactory(),
                            type,
                            annotations,
                        )

                    override fun convert(value: ResponseBody) =
                        if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
                }
            }
    }
