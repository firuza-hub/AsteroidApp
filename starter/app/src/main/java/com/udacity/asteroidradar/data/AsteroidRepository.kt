package com.udacity.asteroidradar.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.local.access.AsteroidDatabase
import com.udacity.asteroidradar.data.local.models.asDatabaseModel
import com.udacity.asteroidradar.data.local.models.asDomainModel
import com.udacity.asteroidradar.data.models.Asteroid
import com.udacity.asteroidradar.data.services.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    var asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) { it.asDomainModel() }

    private val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val calendar: Calendar = Calendar.getInstance()

    suspend fun refreshAsteroids() {

        val today = df.format(calendar.time)
        calendar.add(Calendar.DATE, 7)
        val nextWeek = df.format(calendar.time)

        withContext(Dispatchers.IO) {
            fetchNextWeekAsteroids(today, nextWeek)
            deleteEarlierAsteroids(today)
        }
    }

    private suspend fun fetchNextWeekAsteroids(today: String, nextWeek: String) {
        var asteroidsFromNetwork =
            AsteroidApi.retrofitService.getAsteroidsDataAsync(today, nextWeek)
                .await()

        var data = parseAsteroidsJsonResult(JSONObject(asteroidsFromNetwork))
        database.asteroidDao.insertAsteroids(data.asDatabaseModel())

    }
    private fun deleteEarlierAsteroids(today: String) {
        val asteroids = database.asteroidDao.getAsteroidsForDate(today)
        database.asteroidDao.deleteAsteroidsBefore(asteroids)
    }
}
