package com.template.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface BaseTestGraph {
    @Provides
    fun provideApplicationContext(application: Application): Context = application

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(engine: MockEngine, json: Json): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(json, contentType = ContentType.Application.Json)
            json(json, contentType = ContentType.Text.JavaScript)
        }
    }
}
