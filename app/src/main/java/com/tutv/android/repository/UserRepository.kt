package com.tutv.android.repository

import android.content.SharedPreferences
import android.util.Base64
import com.tutv.android.datasource.retrofit.endpoint.UserAPI
import com.tutv.android.db.dao.UserDao
import com.tutv.android.domain.User
import io.reactivex.Single
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class UserRepository(private val userDao: UserDao?, private val userAPI: UserAPI?, private val authenticationSharedPreferences: SharedPreferences?) {
    fun login(mail: String?, password: String?): Single<User?>? {
        val credentials = "$mail:$password"
        val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        return userAPI.getCurrentUser("Basic $encodedCredentials")
    }

    fun getCurrentUser(): Single<User?>? {
        return getCurrentUser(true)
    }

    fun getCurrentUser(invokeLoginScreenOnFail: Boolean): Single<User?>? {
        return if (invokeLoginScreenOnFail) userAPI.getCurrentUser() else userAPI.getCurrentUserNoForceLoginScreenOnFail()
    }

    fun logout() {
        val editor = authenticationSharedPreferences.edit()
        editor.remove("Token")
        editor.commit()
    }

}