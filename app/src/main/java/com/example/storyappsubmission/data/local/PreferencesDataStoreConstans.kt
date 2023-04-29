package com.example.storyappsubmission.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesDataStoreConstans {
    val USER_ID = stringPreferencesKey("USER_ID")
    val NAME = stringPreferencesKey("NAME")
    val TOKEN = stringPreferencesKey("TOKEN")
}