package com.zellfresh.client.apollo

import com.zellfresh.client.Config
import com.zellfresh.client.http.TokenRepository
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.ws.GraphQLWsProtocol
import com.apollographql.apollo.network.ws.WebSocketNetworkTransport
import com.zellfresh.client.http.AccountRepository
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
        tokenRepository: TokenRepository,
        accountRepository: AccountRepository
    ): ApolloClient {

        val webSocketTransport = WebSocketNetworkTransport.Builder()
            .serverUrl(Config.WS_URL)
            .protocol(GraphQLWsProtocol.Factory(
                connectionPayload = {
                    val accountDetails = accountRepository.accountDetails.value
                    if (accountDetails !== null) {
                        mapOf(
                            "sus" to accountDetails.sub
                        )
                    } else {
                        emptyMap()
                    }
                }
            ))
            .build()


        return ApolloClient.Builder()
            .serverUrl(Config.GRAPHQL_URL)
            .subscriptionNetworkTransport(webSocketTransport)
            .addHttpInterceptor(AuthorizationInterceptor(tokenRepository))
            .build()
    }
}