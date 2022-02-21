package com.tutv.android.ui.login

import com.tutv.android.domain.User
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class LoginPresenter(
        view: LoginView,
        var userRepository: UserRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val view: WeakReference<LoginView?> = WeakReference(view)
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onViewAttached() {}
    fun onViewDetached() {
        disposables.dispose()
    }

    fun loginBegin(mail: String, password: String) {
        if (mail.isEmpty()) view.get()?.setMailError("El campo es obligatorio")
        if (password.isEmpty()) view.get()?.setPasswordError("El campo es obligatorio")

        view.get()?.setMailError(null)
        view.get()?.setPasswordError(null)
        view.get()?.setLoadingStatus(true)

        disposables.add(userRepository.login(mail, password)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally { if (view.get() != null) view.get()?.setLoadingStatus(false) }
                .subscribe({ user: User -> loginSuccessful(user) }) { e: Throwable? -> loginError(e) })
    }

    private fun loginSuccessful(user: User) {
        view.get()?.showToast("¡Bienvenido, " + user.userName + "!")
        view.get()?.dismiss()
    }

    private fun loginError(e: Throwable?) {
        view.get()?.setPasswordError("Correo electrónico o contraseña incorrectos")
        view.get()?.setMailError("Correo electrónico o contraseña incorrectos")
    }

}