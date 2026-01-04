package com.template.data.itunes

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@Inject
@SingleIn(AppScope::class)
class ITunesClient(private val httpClient: HttpClient) {
    suspend fun search(
        term: String,
        media: String = "music",
        limit: Int = 50,
    ): Result<ITunesSearchResponse> = runCatching {
        httpClient
            .get("https://itunes.apple.com/search") {
                parameter("term", term)
                parameter("media", media)
                parameter("limit", limit)
            }
            .body()
    }
}
