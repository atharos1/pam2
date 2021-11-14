package com.tutv.android.repository

import com.tutv.android.db.dao.UserDao
import com.tutv.android.datasource.retrofit.endpoint.UserAPI
import android.content.SharedPreferences
import android.util.Base64
import com.tutv.android.domain.User
import io.reactivex.Single

class UserRepository(
    private val userDao: UserDao,
    private val userAPI: UserAPI,
    private val authenticationSharedPreferences: SharedPreferences
) {
    fun login(mail: String, password: String): Single<User> {
        val encodedCredentials = with("$mail:$password") { Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP) }
        return userAPI.getCurrentUser("Basic $encodedCredentials")
    }

    val currentUser: Single<User>
        get() = getCurrentUser(true)

    fun getCurrentUser(invokeLoginScreenOnFail: Boolean): Single<User> {
        return if (invokeLoginScreenOnFail) userAPI.currentUser else userAPI.currentUserNoForceLoginScreenOnFail
    }

    fun logout() {
        authenticationSharedPreferences.edit().apply {
            remove("Token")
            commit()
        }
    }
}