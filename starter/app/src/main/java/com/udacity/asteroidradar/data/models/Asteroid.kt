package com.udacity.asteroidradar.data.models

import android.os.Parcel
import android.os.Parcelable

data class Asteroid(
    val id: Long, val codename: String?, val closeApproachDate: String?,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readBoolean()
    )
    constructor(id: Long, name:String, date:String, isHazard: Boolean): this(
        id,
        name,
       date,
        1.1,
        1.1,
        1.1,
        1.1,
        isHazard
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(codename)
        parcel.writeString(closeApproachDate)
        parcel.writeDouble(absoluteMagnitude)
        parcel.writeDouble(estimatedDiameter)
        parcel.writeDouble(relativeVelocity)
        parcel.writeDouble(distanceFromEarth)
        parcel.writeBoolean(isPotentiallyHazardous)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Asteroid> {
        override fun createFromParcel(parcel: Parcel): Asteroid {
            return Asteroid(parcel)
        }

        override fun newArray(size: Int): Array<Asteroid?> {
            return arrayOfNulls(size)
        }
    }
}