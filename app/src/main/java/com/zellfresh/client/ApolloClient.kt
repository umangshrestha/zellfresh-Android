package com.zellfresh.client

import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("${Config.BASE_URL}/graphql")
    .build()