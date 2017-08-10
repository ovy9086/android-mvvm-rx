package org.olu.mvvm.viewmodel

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.olu.mvvm.repository.api.UserApi
import org.olu.mvvm.repository.data.User
import org.olu.mvvm.repository.db.UserDao
import timber.log.Timber

class UserRepository(val userApi: UserApi, val userDao: UserDao) {

    var cachedUsers = emptyList<User>()

    fun getUsers(): Observable<List<User>> {
        return Observable.concatArrayDelayError(
                getUsersFromCache(), getUsersFromDb(), getUsersFromApi())
    }


    fun getUsersFromCache(): Observable<List<User>> {
        return Observable.just(cachedUsers).filter { it.isNotEmpty() }
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from Cache.")
                }
    }

    fun getUsersFromDb(): Observable<List<User>> {
        return userDao.getUsers().filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from DB.")
                }
    }

    fun getUsersFromApi(): Observable<List<User>> {
        return userApi.getUsers()
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from API.")
                    cacheUsers(it)
                }
    }

    fun cacheUsers(users: List<User>) {
        Timber.d("Cached ${users.size} users in memory.")
        cachedUsers = users
        Observable.fromCallable { userDao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Timber.d("Inserted ${users.size} users from API in DB.")
                }
    }
}
