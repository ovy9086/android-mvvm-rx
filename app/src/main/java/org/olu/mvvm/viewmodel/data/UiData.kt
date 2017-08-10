package org.olu.mvvm.viewmodel.data

import org.olu.mvvm.repository.data.User

data class UsersList(val users: List<User>, val message: String, val error: Throwable? = null)
