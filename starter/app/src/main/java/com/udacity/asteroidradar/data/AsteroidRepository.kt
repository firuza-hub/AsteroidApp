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

    var df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var calendar: Calendar = Calendar.getInstance()

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            var asteroidsFromNetwork =
                AsteroidApi.retrofitService.getAsteroidsDataAsync("2020-09-09", "2020-09-09")
                    .await()

            var data = parseAsteroidsJsonResult(JSONObject(asteroidsFromNetwork))
            database.asteroidDao.insertAsteroids(data.asDatabaseModel())


        }

    }
}
