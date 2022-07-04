package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.local.access.getDatabase
import com.udacity.asteroidradar.data.models.Asteroid
import com.udacity.asteroidradar.data.models.PictureOfDay
import com.udacity.asteroidradar.data.services.AsteroidApi
import com.udacity.asteroidradar.data.services.AsteroidApiStatus
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(applicationContext: Application) : ViewModel() {

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    var asteroids = MutableLiveData<List<Asteroid>>()
    var pictureOfTheDay = MutableLiveData<PictureOfDay>()

    var df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var calendar: Calendar = Calendar.getInstance()

    private val database = getDatabase(applicationContext)
    private val asteroidRepository = AsteroidRepository(database)

    private val _context = applicationContext

    //TODO: Past data not showing in UI
    fun getAsteroidPastData() {
        asteroids = asteroidRepository.asteroids as MutableLiveData<List<Asteroid>>
        _status.value = AsteroidApiStatus.DONE

    }

    fun getAsteroidTodayData() {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                asteroids.value = parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getAsteroidsDataAsync(
                            df.format(calendar.time),
                            df.format(calendar.time)
                        ).await()
                    )
                )
                _status.value = AsteroidApiStatus.DONE
            } catch (ex: Exception) {
                Log.i("FETCH", "getAsteroidTodayData:" + ex.stackTraceToString())
                _status.value = AsteroidApiStatus.ERROR
                Toast.makeText(_context, "No internet connection", Toast.LENGTH_SHORT).show()
                getAsteroidPastData()
            }
        }
    }

    fun getAsteroidWeekData() {

        val calendarNextWeek: Calendar = Calendar.getInstance()
        calendarNextWeek.add(Calendar.DATE, 7)
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                asteroids.value = parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getAsteroidsDataAsync(
                            df.format(calendar.time),
                            df.format(calendarNextWeek.time)
                        ).await()
                    )
                )
                _status.value = AsteroidApiStatus.DONE
            } catch (exception: Exception) {
                Log.i("FETCH", "getAsteroidTodayData:" + exception.stackTraceToString())
                _status.value = AsteroidApiStatus.ERROR

                if (exception.message?.contains("403") == true) {
                    Toast.makeText(_context, "Access Forbidden", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(_context, "No internet connection", Toast.LENGTH_SHORT).show()
                }
                getAsteroidPastData()
            }
        }
    }

    fun getPictureOfTheDayData() {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                pictureOfTheDay.value = AsteroidApi.retrofitService.getPictureOfTheDay().await()
                _status.value = AsteroidApiStatus.DONE
            } catch (exception: Exception) {
                Log.i("FETCH", "getAsteroidTodayData:" + exception.stackTraceToString())
                _status.value = AsteroidApiStatus.ERROR

                if (exception.message?.contains("403") == true) {
                    Toast.makeText(_context, "Access Forbidden", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(_context, "No internet connection", Toast.LENGTH_SHORT).show()
                }
                _status.value = AsteroidApiStatus.DONE
            }
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