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
        if(view.get() != null)
            view.get().setLoading(true);

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

        if(view.get() != null) {
            view.get().setLoading(false);

            view.get().setLayout(true);
            view.get().setMail(user.getMail());
            view.get().setUsername(user.getUserName());
            view.get().setProfileImage(user.getAvatar());
        }
    }

    private void loadError(Throwable e) {
        ProfileView actualView = view.get();
        if (actualView != null) {
            view.get().setLoading(false);

            view.get().setLayout(false);
            actualView.showToast("Error loading user data");
        }
    }

    public void openCurrentUserProfileInWebApp() {
        if(view.get() != null)
            view.get().openProfileInWebApp(user.getId());
    }

    public void logout() {
        if(view.get() != null)
            view.get().setLayout(false);

        userRepository.logout();
    }
}
