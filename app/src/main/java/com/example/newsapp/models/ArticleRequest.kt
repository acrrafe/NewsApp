package com.example.newsapp.models

import android.os.Parcel
import android.os.Parcelable

/** MODEL OF ARTICLE REQUEST
  This is responsible for being the structure when fetching article from
  the api **/
data class ArticleRequest(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Source::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(content)
        parcel.writeString(description)
        parcel.writeString(publishedAt)
        parcel.writeParcelable(source, flags)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleRequest> {
        override fun createFromParcel(parcel: Parcel): ArticleRequest {
            return ArticleRequest(parcel)
        }

        override fun newArray(size: Int): Array<ArticleRequest?> {
            return arrayOfNulls(size)
        }
    }
}

private fun Parcel.writeParcelable(source: Source?, flags: Int) {

}