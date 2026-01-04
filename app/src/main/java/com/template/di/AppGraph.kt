package com.template.di

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

@DependencyGraph(AppScope::class)
interface AppGraph {
    val circuit: Circuit

    @Provides fun provideApplicationContext(application: Application): Context = application

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(json: Json): HttpClient =
        HttpClient(OkHttp) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Timber.tag("TemplateHttp").d(message)
                        }
                    }
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Application.Json)
                json(json, contentType = ContentType.Text.JavaScript)
            }
        }

    @Provides
    @SingleIn(AppScope::class)
    fun provideCircuit(
        presenterFactories: Set<@JvmSuppressWildcards Presenter.Factory>,
        uiFactories: Set<@JvmSuppressWildcards Ui.Factory>,
    ): Circuit =
        Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .setOnUnavailableContent { screen, modifier ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Missing Circuit content for ${screen::class.simpleName}")
                }
            }
            .build()

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
