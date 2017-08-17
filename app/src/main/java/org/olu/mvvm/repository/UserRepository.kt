package org.olu.mvvm.viewmodel

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.olu.mvvm.repository.api.UserApi
import org.olu.mvvm.repository.data.User
import org.olu.mvvm.repository.db.UserDao
import timber.log.Timber

class UserRepository(val userApi: UserApi, val userDao: UserDao) {

    fun getUsers(): Observable<List<User>> {
        return Observable.concatArray(
                getUsersFromDb(),
                getUsersFromApi())
    }


    fun getUsersFromDb(): Observable<List<User>> {
        return userDao.getUsers().filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from DB...")
                }
    }

    fun getUsersFromApi(): Observable<List<User>> {
        return userApi.getUsers()
                .doOnNext {
                    Timber.d("Dispatching ${it.size} users from API...")
                    storeUsersInDb(it)
                }
    }

    fun storeUsersInDb(users: List<User>) {
        Observable.fromCallable { userDao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Timber.d("Inserted ${users.size} users from API in DB...")
                }
    }

}
