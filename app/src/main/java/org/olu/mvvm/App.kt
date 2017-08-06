package org.olu.mvvm

import android.app.Application
import org.olu.mvvm.datamodel.api.UserApi
import org.olu.mvvm.viewmodel.UserDataModel
import org.olu.mvvm.viewmodel.UserListViewModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class App : Application() {

    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var userApi: UserApi
        private lateinit var userDataModel: UserDataModel
        private lateinit var userListViewModel: UserListViewModel

        fun injectUserApi() = userApi

        fun injectUserListViewModel() = userListViewModel
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
        userDataModel = UserDataModel(userApi)
        userListViewModel = UserListViewModel(userDataModel)

    }
}
