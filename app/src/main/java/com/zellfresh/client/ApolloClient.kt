package com.zellfresh.client

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.ws.GraphQLWsProtocol
import com.apollographql.apollo.network.ws.WebSocketNetworkTransport

val apolloClient = ApolloClient.Builder()
    .serverUrl("${Config.BASE_URL}/graphql")
    .subscriptionNetworkTransport(
        WebSocketNetworkTransport.Builder()
            .protocol(GraphQLWsProtocol.Factory())
            .serverUrl("${Config.BASE_URL}/graphql".replace("http", "ws"))
            .build()
    )
    .build()