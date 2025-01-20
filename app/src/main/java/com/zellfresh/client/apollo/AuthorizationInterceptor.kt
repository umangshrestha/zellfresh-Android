package com.zellfresh.client.apollo

import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.zellfresh.client.http.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class AuthorizationInterceptor(
    private val tokenRepository: TokenRepository,
) : HttpInterceptor {

    @Volatile
    private var accessToken: String? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            tokenRepository.loginState.collect {
                accessToken = it.getAccessToken()
            }
        }
    }

    override suspend fun intercept(
        request: HttpRequest, chain: HttpInterceptorChain
    ): HttpResponse {
        val requestBuilder = request.newBuilder()
        accessToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }
        return chain.proceed(requestBuilder.build())
    }
}
