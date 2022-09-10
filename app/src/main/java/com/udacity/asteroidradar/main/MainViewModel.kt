package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.api.AsteroidsApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

    /**
     * Call getAsteroids() on init so we can display status immediately.
     */
    init {
        getAsteroids()
    }

    /**
     * Sets the value of the status LiveData to the Asteroids API status.
     */
    private fun getAsteroids() {
        // TODO implement this function to get the data

        _response.value = "Set the Asteroids API Response here!"
    }

}