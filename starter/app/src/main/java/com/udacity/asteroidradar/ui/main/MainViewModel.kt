package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.local.access.getDatabase
import com.udacity.asteroidradar.data.models.Asteroid
import com.udacity.asteroidradar.data.models.PictureOfDay
import com.udacity.asteroidradar.data.services.AsteroidApi
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(applicationContext: Application) : ViewModel() {
    var asteroids = MutableLiveData<List<Asteroid>>()
    var pictureOfTheDay = MutableLiveData<PictureOfDay>()

    var df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var calendar: Calendar = Calendar.getInstance()

    private val database = getDatabase(applicationContext)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    //TODO: Past data not showing in UI
    fun getAsteroidPastData(){
        asteroids =  asteroidRepository.asteroids as MutableLiveData<List<Asteroid>>
    }

    fun getAsteroidTodayData() {
        viewModelScope.launch {
            asteroids.value = parseAsteroidsJsonResult(
                JSONObject(
                    AsteroidApi.retrofitService.getAsteroidsDataAsync(
                        df.format(calendar.time),
                        df.format(calendar.time)
                    ).await()
                )
            )
        }
    }

    fun getAsteroidWeekData() {

        val calendarNextWeek: Calendar = Calendar.getInstance()
        calendarNextWeek.add(Calendar.DATE, 7)
        viewModelScope.launch {
            asteroids.value = parseAsteroidsJsonResult(
                JSONObject(
                    AsteroidApi.retrofitService.getAsteroidsDataAsync(
                        df.format(calendar.time),
                        df.format(calendarNextWeek.time)
                    ).await()
                )
            )
        }
    }

    fun getPictureOfTheDay(){
        viewModelScope.launch {
            pictureOfTheDay.value = AsteroidApi.retrofitService.getPictureOfTheDay().await()
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }


    }

}