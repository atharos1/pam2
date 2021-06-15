package com.tutv.android.ui.login;

import com.tutv.android.datasource.retrofit.endpoint.UserAPI;
import com.tutv.android.domain.User;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter {
    private final WeakReference<LoginView> view;

    private final UserRepository userRepository;
    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeDisposable disposables;

    public LoginPresenter(LoginView view, UserRepository userRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    public void loginBegin(String mail, String password) {
        if(mail.isEmpty())
            view.get().setMailError("El campo es obligatorio");

        if(password.isEmpty())
            view.get().setPasswordError("El campo es obligatorio");

        if(view.get() != null) {
            view.get().setMailError(null);
            view.get().setPasswordError(null);

            view.get().setLoadingStatus(true);
        }

        disposables.add(userRepository.login(mail, password)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally(() -> { if(view.get() != null) view.get().setLoadingStatus(false); })
                .subscribe(this::loginSuccessful, this::loginError));
    }

    private void loginSuccessful(final User user) {
        if(view.get() != null) {
            view.get().showToast("¡Bienvenido, " + user.getUserName() + "!");
            view.get().dismiss();
        }
    }

    private void loginError(final Throwable e) {
        if(view.get() != null) {
            view.get().setPasswordError("Correo electrónico o contraseña incorrectos");
            view.get().setMailError("Correo electrónico o contraseña incorrectos");
        }
    }
}
