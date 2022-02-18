package com.tutv.android.ui.notifications

import com.tutv.android.domain.User
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class UserPresenter(userView: UserView?, userRepository: UserRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val userRepository: UserRepository?
    private val userView: WeakReference<UserView?>?
    private val schedulerProvider: BaseSchedulerProvider?
    private val disposables: CompositeDisposable?
    fun onViewAttached() {
        disposables.add(userRepository.getCurrentUser()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ user: User? -> onUserLoaded(user) }) { throwable: Throwable? -> onUserLoadedError(throwable) })
    }

    private fun onUserLoadedError(throwable: Throwable?) {
        val view = userView.get()
        view?.showError("Error al cargar al usuario. Asegurese de tener conexion y de estar logueado")
    }

    private fun onUserLoaded(user: User?) {
        val view = userView.get()
        if (view != null) {
            view.showUserAvatar(user.getAvatar())
            view.showUserName(user.getUserName())
        }
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    init {
        this.userView = WeakReference(userView)
        this.userRepository = userRepository
        this.schedulerProvider = schedulerProvider
        disposables = CompositeDisposable()
    }
}