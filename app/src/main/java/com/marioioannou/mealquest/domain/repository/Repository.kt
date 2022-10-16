package com.marioioannou.mealquest.domain.repository

import com.marioioannou.mealquest.domain.LocalDataSource
import com.marioioannou.mealquest.domain.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped //Inject it in viewModel
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
){
    val remote = remoteDataSource
    val local = localDataSource
}