package com.clint.nasa.pictures

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pictures(
    @SerializedName("copyright") var copyright: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("explanation") var explanation: String? = null,
    @SerializedName("hdurl") var hdurl: String? = null,
    @SerializedName("media_type") var mediaType: String? = null,
    @SerializedName("service_version") var serviceVersion: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("url") var url: String? = null
) : Parcelable {
}
