package com.tutv.android.di;

import android.content.Context;

import com.tutv.android.datasource.retrofit.endpoint.UserAPI;
import com.tutv.android.db.dao.UserDao;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.ui.login.LoginPresenter;

public class AppContainer implements Container {
    private final ContainerModule containerModule;

    private UserRepository userRepository;
    private UserDao userDao;
    private UserAPI userAPI;

    public AppContainer(final Context context) {
        this.containerModule = new ContainerModule(context);
    }

    @Override
    public UserRepository getUserRepository() {
        if(userRepository == null)
            userRepository = containerModule.provideUserRepository(getUserDao(), getUserAPI());

        return userRepository;
    }

    private UserAPI getUserAPI() {
        if(userAPI == null)
            userAPI = containerModule.provideUserAPI();

        return userAPI;
    }

    private UserDao getUserDao() {
        if(userDao == null)
            userDao = containerModule.provideUserDao();

        return userDao;
    }
}
