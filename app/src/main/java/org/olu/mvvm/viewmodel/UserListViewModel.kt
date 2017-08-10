package org.olu.mvvm.viewmodel

import io.reactivex.Observable
import org.olu.mvvm.viewmodel.data.UsersList
import timber.log.Timber

class UserListViewModel(val userRepository: UserRepository) {

    fun getUsers(): Observable<UsersList> {
        //Create the data for your UI, the users lists and maybe some additional data needed as well
        return userRepository.getUsers()
                .map {
                    Timber.d("Mapping users to UIData...")
                    UsersList(it.take(10), "Top 10 Users")
                }
                .onErrorReturn {
                    UsersList(emptyList(), "An error occured", it)
                }
    }
}
