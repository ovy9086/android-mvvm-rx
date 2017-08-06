package org.olu.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.users_fragment.*
import org.olu.mvvm.App
import org.olu.mvvm.R
import org.olu.mvvm.viewmodel.data.UsersList

class UsersListFragment : MvvmFragment() {

    val userListViewModel = App.injectUserListViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        subscribe(userListViewModel.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showUsers(it)
                }, {
                    showError()
                }))
    }

    fun showUsers(data: UsersList) {
        usersList.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, data.users)
    }

    fun showError() {
        Toast.makeText(context, "An error occurred :(", Toast.LENGTH_SHORT).show()
    }
}
