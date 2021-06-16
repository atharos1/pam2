package com.tutv.android.repository;

import android.content.SharedPreferences;
import android.util.Base64;

import com.tutv.android.datasource.retrofit.endpoint.UserAPI;
import com.tutv.android.db.dao.UserDao;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.User;

import io.reactivex.Single;

public class UserRepository {
    private final UserDao userDao;
    private final UserAPI userAPI;
    private final SharedPreferences authenticationSharedPreferences;

    public UserRepository(UserDao userDao, UserAPI userAPI, SharedPreferences authenticationSharedPreferences) {
        this.userDao = userDao;
        this.userAPI = userAPI;
        this.authenticationSharedPreferences = authenticationSharedPreferences;
    }

    public Single<User> login(String mail, String password) {
        String credentials = mail + ":" + password;
        String encodedCredentials = android.util.Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return userAPI.getCurrentUser("Basic " + encodedCredentials);
    }

    public Single<User> getCurrentUser() {
        return getCurrentUser(true);
    }

    public Single<User> getCurrentUser(boolean invokeLoginScreenOnFail) {
        if(invokeLoginScreenOnFail)
            return userAPI.getCurrentUser();
        else
            return userAPI.getCurrentUserNoForceLoginScreenOnFail();
    }

    public void logout() {
        SharedPreferences.Editor editor = authenticationSharedPreferences.edit();
        editor.remove("Token");
        editor.commit();
    }
}
