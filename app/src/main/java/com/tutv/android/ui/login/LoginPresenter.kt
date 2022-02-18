package com.tutv.android.ui.login

import com.tutv.android.domain.User
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class LoginPresenter(view: LoginView, userRepository: UserRepository, schedulerProvider: BaseSchedulerProvider) {
    private val view: WeakReference<LoginView>
    private val userRepository: UserRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private val disposables: CompositeDisposable?
    fun onViewAttached() {}
    fun onViewDetached() {
        disposables.dispose()
    }

    fun loginBegin(mail: String, password: String?) {
        if (mail.isEmpty()) view.get().setMailError("El campo es obligatorio")
        if (password.isEmpty()) view.get().setPasswordError("El campo es obligatorio")
        if (view.get() != null) {
            view.get().setMailError(null)
            view.get().setPasswordError(null)
            view.get().setLoadingStatus(true)
        }
        disposables.add(userRepository.login(mail, password)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally { if (view.get() != null) view.get().setLoadingStatus(false) }
                .subscribe({ user: User? -> loginSuccessful(user) }) { e: Throwable? -> loginError(e) })
    }

    private fun loginSuccessful(user: User?) {
        if (view.get() != null) {
            view.get().showToast("¡Bienvenido, " + user.getUserName() + "!")
            view.get().dismiss()
        }
    }

    private fun loginError(e: Throwable?) {
        if (view.get() != null) {
            view.get().setPasswordError("Correo electrónico o contraseña incorrectos")
            view.get().setMailError("Correo electrónico o contraseña incorrectos")
        }
    }

    init {
        this.view = WeakReference(view)
        this.userRepository = userRepository
        this.schedulerProvider = schedulerProvider
        disposables = CompositeDisposable()
    }
}