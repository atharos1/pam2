package com.tutv.android.di;

import android.content.Context;

import com.tutv.android.datasource.retrofit.RetrofitInstance;
import com.tutv.android.datasource.retrofit.endpoint.UserAPI;
import com.tutv.android.db.AppDatabase;
import com.tutv.android.db.dao.UserDao;
import com.tutv.android.repository.UserRepository;

public class ContainerModule {
    private final Context applicationContext;

    public ContainerModule(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    Context getApplicationContext() {
        return applicationContext;
    }

    public UserRepository provideUserRepository(final UserDao userDao, UserAPI userAPI) {
        return new UserRepository(userDao, userAPI);
    }

    public UserDao provideUserDao() {
        return AppDatabase.getInstance(applicationContext).userDao();
    }

    public UserAPI provideUserAPI() {
        return RetrofitInstance.getRetrofitClient().create(UserAPI.class);
    }
}
