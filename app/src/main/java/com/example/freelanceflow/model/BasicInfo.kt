package com.example.freelanceflow.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class BasicInfo(
    var name: String? = null,
    var email: String? = null,
    var imageUri: Uri? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeParcelable(imageUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasicInfo> {
        override fun createFromParcel(parcel: Parcel): BasicInfo {
            return BasicInfo(parcel)
        }

        override fun newArray(size: Int): Array<BasicInfo?> {
            return arrayOfNulls(size)
        }
    }
}
