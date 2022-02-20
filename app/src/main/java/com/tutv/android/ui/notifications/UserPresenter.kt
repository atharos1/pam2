package com.tutv.android.ui.notifications

import com.tutv.android.domain.User
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class UserPresenter(
        userView: UserView,
        var userRepository: UserRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val userView: WeakReference<UserView?> = WeakReference(userView)
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onViewAttached() {
        disposables.add(userRepository.currentUser
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ user: User -> onUserLoaded(user) }) { throwable: Throwable? -> onUserLoadedError(throwable) })
    }

    private fun onUserLoadedError(throwable: Throwable?) {
        val view = userView.get()
        view?.showError("Error al cargar al usuario. Asegurese de tener conexion y de estar logueado")
    }

    private fun onUserLoaded(user: User) {
        val view = userView.get()
        view?.showUserAvatar(user.avatar)
        view?.showUserName(user.userName)
    }

    fun onViewDetached() {
        disposables.dispose()
    }

}