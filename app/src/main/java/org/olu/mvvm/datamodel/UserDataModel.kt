package org.olu.mvvm.datamodel

import io.reactivex.Observable
import org.olu.mvvm.data.User
import org.olu.mvvm.repository.api.UserApi
import timber.log.Timber

class UserDataModel(val userApi: UserApi) {

    var cachedUsers = emptyList<User>()

    fun getUsers(): Observable<List<User>> {
        if (cachedUsers.isEmpty()) {
            Timber.d("Returning users from API")
            return userApi.getUsers()
                    .doOnNext { cachedUsers = it }
        } else {
            Timber.d("Returning cached users")
            return Observable.just(cachedUsers)
                    .mergeWith(userApi.getUsers())
                    .doOnNext { cachedUsers = it }
        }
    }
}
