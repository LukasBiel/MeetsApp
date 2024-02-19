package com.example.myapplication.data

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userId: String? = null,
    val imie: String? = null,
    val nazwisko: String? = null,
    val wiek: Int? = null,
    val plec: String? = null,
    val ile_postow: Int? = null

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(imie)
        parcel.writeString(nazwisko)
        parcel.writeValue(wiek)
        parcel.writeString(plec)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

