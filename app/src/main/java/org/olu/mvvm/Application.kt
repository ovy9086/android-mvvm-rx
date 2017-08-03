package org.olu.mvvm

import android.app.Application
import org.olu.mvvm.repository.api.UserApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var retrofit: Retrofit
        lateinit var userApi: UserApi
    }

    override fun onCreate() {
        super.onCreate()
        Timber.uprootAll()
        Timber.plant(Timber.DebugTree())

        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://randomapi.com/api/")
                .build()

        userApi = retrofit.create(UserApi::class.java)
    }
}
