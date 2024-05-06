package com.example.attendance

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserMananger(context: Context) {

    // Create the dataStore and give it a name same as shared preferences
    private val dataStore = context.createDataStore(name = "user_prefs")

    // Create some keys we will use them to store and retrieve the data
    companion object {
        val USER_LOGIN_KEY = preferencesKey<Boolean>("USER_LOGIN_KEY")


    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeloginkey(loggin:Boolean) {
        dataStore.edit {
            it[USER_LOGIN_KEY] = loggin

            // here it refers to the preferences we are editing

        }
    }

    // Create an age flow to retrieve age from the preferences
    // flow comes from the kotlin coroutine
    val userLoginFlow: Flow<Boolean> = dataStore.data.map {
        it[USER_LOGIN_KEY] ?: false
    }





}