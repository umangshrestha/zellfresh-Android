package com.zellfresh.client

object Config {
    private const val DOMAIN = "www.zellfresh.com"

    const val BASE_URL = "https://$DOMAIN"
    const val GRAPHQL_URL = "$BASE_URL/graphql"
    const val WS_URL = "wss://$DOMAIN/graphql"
}