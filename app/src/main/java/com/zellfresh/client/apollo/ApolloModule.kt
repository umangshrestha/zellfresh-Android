package com.zellfresh.client.apollo

import com.zellfresh.client.Config
import com.zellfresh.client.http.TokenRepository
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    @Provides
    @Singleton
    fun provideApolloClient(
        tokenRepository: TokenRepository
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(Config.GRAPHQL_URL)
            .webSocketServerUrl(Config.WS_URL)
            .addHttpInterceptor(AuthorizationInterceptor(tokenRepository))
            .build()
    }
}