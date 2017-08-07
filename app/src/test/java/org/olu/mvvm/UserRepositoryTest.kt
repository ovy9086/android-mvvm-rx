package org.olu.mvvm

import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.olu.mvvm.repository.api.UserApi
import org.olu.mvvm.repository.data.User
import org.olu.mvvm.viewmodel.UserRepository
import java.util.*
import kotlin.test.assertEquals


class UserRepositoryTest {

    lateinit var userRepository: UserRepository
    lateinit var userApi: UserApi

    @Before
    fun setup() {
        userApi = mock()
        userRepository = UserRepository(userApi)
    }

    @Test
    fun test_emptyCache_noDataOnApi_returnsEmptyList() {
        `when`(userApi.getUsers()).thenReturn(Observable.just(emptyList<User>()))

        userRepository.getUsers().test()
                .assertValue { it.isEmpty() }
    }

    @Test
    fun test_emptyCache_hasDataOnApi_returnsApiData() {
        `when`(userApi.getUsers()).thenReturn(Observable.just(listOf(aRandomUser())))

        userRepository.getUsers().test()
                .assertValueCount(1)
                .assertValue { it.size == 1 }
    }

    @Test
    fun test_hasCacheData_hasApiData_returnsBothData() {
        val cachedData = listOf(aRandomUser())
        val apiData = listOf(aRandomUser(), aRandomUser())
        `when`(userApi.getUsers()).thenReturn(Observable.just(apiData))
        userRepository.cachedUsers = cachedData

        userRepository.getUsers().test()
                //Both cached & API data delivered
                .assertValueCount(2)
                //First cache data delivered
                .assertValueAt(0, { it == cachedData })
                //Secondly api data delivered
                .assertValueAt(1, { it == apiData })
    }

    @Test
    fun test_cache_updatedWithApiData() {
        val apiData = listOf(aRandomUser(), aRandomUser())
        `when`(userApi.getUsers()).thenReturn(Observable.just(apiData))

        userRepository.getUsers().test()

        assertEquals(userRepository.cachedUsers, apiData)
    }

    fun aRandomUser() = User("mail@test.com", "John", UUID.randomUUID().toString().take(5))
}