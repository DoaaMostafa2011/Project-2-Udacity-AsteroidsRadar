package com.udacity.asteroidradar.repository

import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.AsteroidsApiService
import com.udacity.asteroidradar.api.AsteroidsApiService.AsteroidsApi.retrofitService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidsDao
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository (private val database: AsteroidDatabase) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().minusDays(7)

    //
    val allAsteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val todayAsteroids : LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAsteroidsDay(startDate.format(
                DateTimeFormatter.ISO_DATE))){
                it.asDomainModel()
            }

    @RequiresApi(Build.VERSION_CODES.O)
    val weekAsteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAsteroidsDate(startDate.format(
                DateTimeFormatter.ISO_DATE),
                endDate.format(DateTimeFormatter.ISO_DATE))){
                it.asDomainModel()
            }

    //refresh the offline cache
    //Get the data from the network and then put it in the database
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidsApiService.AsteroidsApi.retrofitService.getAsteroids(API_KEY)
            val result = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDao.insertAll(*result.asDatabaseModel())
        }
    }
}