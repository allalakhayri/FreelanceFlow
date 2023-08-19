package com.example.freelanceflow.model
import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Project(
    var id: String = UUID.randomUUID().toString(),
    var name: String? = null,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var details: List<String>? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeLong(startDate?.time ?: 0)
        dest.writeLong(endDate?.time ?: 0)
        dest.writeStringList(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}
