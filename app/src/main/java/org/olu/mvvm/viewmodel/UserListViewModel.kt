package org.olu.mvvm.viewmodel

import io.reactivex.Observable
import org.olu.mvvm.viewmodel.data.UsersList

class UserListViewModel(val userDataModel: UserDataModel) {

    fun getUsers(): Observable<UsersList> {
        //Create the data for your UI, the users lists and maybe some additional data needed as well
        return userDataModel.getUsers().map { UsersList(it, "Users loaded successfully!") }
    }
}
