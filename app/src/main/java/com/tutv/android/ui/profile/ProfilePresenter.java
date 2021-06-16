package com.tutv.android.ui.profile;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.User;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ProfilePresenter {

    private final WeakReference<ProfileView> view;
    private final UserRepository userRepository;
    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeDisposable disposables;

    private User user;

    public ProfilePresenter(ProfileView view, UserRepository userRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(userRepository.getCurrentUser()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::loadSuccessful, this::loadError)
        );
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    private void loadSuccessful(User user) {
        this.user = user;
    }

    private void loadError(Throwable e) {
        ProfileView actualView = view.get();
        if (actualView != null)
            actualView.showError();
    }
}
