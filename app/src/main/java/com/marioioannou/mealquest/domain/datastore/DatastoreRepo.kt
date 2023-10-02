package com.marioioannou.mealquest.domain.datastore

interface DatastoreRepo {

    suspend fun saveLanguage(key: String,value : String)

    suspend fun getLanguage(key: String):String?

}