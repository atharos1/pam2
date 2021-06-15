package com.tutv.android.repository;

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

    public UserRepository(UserDao userDao, UserAPI userAPI) {
        this.userDao = userDao;
        this.userAPI = userAPI;
    }

    public Single<User> login(String mail, String password) {
        String credentials = mail + ":" + password;
        String encodedCredentials = android.util.Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return userAPI.getCurrentUser("Basic " + encodedCredentials);
    }

    public Single<User> getCurrentUser() {
        return userAPI.getCurrentUser();
    }

}
