package com.example.myapplication.data


import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser

data class Post(
    val postId: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val short_content: String? = null,
    val content: String? = null,
    val kategorie: MutableList<String>? = null,
    val people_number: Int? = null,
    val max_people: Int? = null,
    val localization: String? = null,
    val timestamp: Long? = null,
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
        // Read other properties...
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        // Write other properties...
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
