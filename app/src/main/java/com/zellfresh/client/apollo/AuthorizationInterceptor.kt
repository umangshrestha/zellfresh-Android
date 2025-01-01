package com.zellfresh.client.apollo

import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.zellfresh.client.http.TokenRepository

internal class AuthorizationInterceptor(
    private val dataStoreRepository: TokenRepository,
) : HttpInterceptor {

    override suspend fun intercept(
        request: HttpRequest, chain: HttpInterceptorChain
    ): HttpResponse {
        val token = dataStoreRepository.getAccessToken()
        return chain.proceed(
            request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        )
    }
}
