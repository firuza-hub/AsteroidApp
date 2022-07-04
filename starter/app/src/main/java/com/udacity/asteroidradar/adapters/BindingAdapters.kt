package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.asteroidradar.data.services.AsteroidApiStatus

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        this.setImageResource(R.drawable.ic_status_potentially_hazardous)
        this.contentDescription = this.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        this.setImageResource(R.drawable.ic_status_normal)
        this.contentDescription = this.context.getString(R.string.not_hazardous_asteroid_image)
    }

}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        this.setImageResource(R.drawable.asteroid_hazardous)
        this.contentDescription = this.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        this.setImageResource(R.drawable.asteroid_safe)
        this.contentDescription = this.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}



@BindingAdapter("asteroidApiStatus")
fun ProgressBar.bindStatus(status: AsteroidApiStatus?) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            this.visibility = View.VISIBLE
        }
        AsteroidApiStatus.ERROR -> {
            this.visibility = View.VISIBLE
        }
        AsteroidApiStatus.DONE -> {
            this.visibility = View.GONE
        }
    }
}


