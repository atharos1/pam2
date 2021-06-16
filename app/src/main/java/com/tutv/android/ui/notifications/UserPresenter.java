package com.tutv.android.ui.notifications;

import com.tutv.android.domain.User;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public class UserPresenter {

    private UserRepository userRepository;
    private WeakReference<UserView> userView;
    private BaseSchedulerProvider schedulerProvider;

    private final CompositeDisposable disposables;

    public UserPresenter(UserView userView, UserRepository userRepository, BaseSchedulerProvider schedulerProvider) {
        this.userView = new WeakReference<>(userView);
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;
        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(userRepository.getCurrentUser()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(this::onUserLoaded, this::onUserLoadedError));
    }

    private void onUserLoadedError(Throwable throwable) {
        UserView view = userView.get();
        if(view != null) {
            view.showError("Error al cargar al usuario. Asegurese de tener conexion y de estar logueado");
        }
    }

    private void onUserLoaded(User user) {
        UserView view = userView.get();
        if(view != null) {
            view.showUserAvatar(user.getAvatar());
            view.showUserName(user.getUserName());
        }
    }

    public void onViewDetached() {
        disposables.dispose();
    }



}