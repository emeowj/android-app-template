package com.template.data.itunes

import kotlinx.serialization.Serializable

@Serializable
data class ITunesSearchResponse(val resultCount: Int, val results: List<ITunesResult>)

@Serializable
data class ITunesResult(
    val trackId: Long? = null,
    val collectionId: Long? = null,
    val artistName: String,
    val collectionName: String? = null,
    val trackName: String? = null,
    val artworkUrl100: String? = null,
    val previewUrl: String? = null,
    val primaryGenreName: String? = null,
    val kind: String? = null,
    val wrapperType: String,
)
