package com.example.freelanceflow.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Education(
    var id: String = UUID.randomUUID().toString(),
    var school: String? = null,
    var major: String? = null,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var courses: List<String>? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(school)
        dest.writeString(major)
        dest.writeLong(startDate?.time ?: 0)
        dest.writeLong(endDate?.time ?: 0)
        dest.writeStringList(courses)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Education> {
        override fun createFromParcel(parcel: Parcel): Education {
            return Education(parcel)
        }

        override fun newArray(size: Int): Array<Education?> {
            return arrayOfNulls(size)
        }
    }
}
