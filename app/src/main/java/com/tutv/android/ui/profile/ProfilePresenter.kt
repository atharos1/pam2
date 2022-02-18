package com.tutv.android.ui.profile

import com.tutv.android.domain.User
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class ProfilePresenter(view: ProfileView?, userRepository: UserRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val view: WeakReference<ProfileView?>?
    private val userRepository: UserRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private var disposables: CompositeDisposable?
    private var user: User? = null
    fun onViewAttached() {
        if (disposables.isDisposed()) disposables = CompositeDisposable()
        if (view.get() != null) view.get().setLoading(true)
        disposables.add(userRepository.getCurrentUser(false)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ user: User? -> loadSuccessful(user) }) { e: Throwable? -> loadError(e) }
        )
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun loadSuccessful(user: User?) {
        this.user = user
        if (view.get() != null) {
            view.get().setLoading(false)
            view.get().setLayout(true)
            view.get().setMail(user.getMail())
            view.get().setUsername(user.getUserName())
            view.get().setProfileImage(user.getAvatar())
        }
    }

    private fun loadError(e: Throwable?) {
        val actualView = view.get()
        if (actualView != null) {
            view.get().setLoading(false)
            view.get().setLayout(false)
        }
    }

    fun openCurrentUserProfileInWebApp() {
        if (view.get() != null) view.get().openProfileInWebApp(user.getId())
    }

    fun logout() {
        if (view.get() != null) view.get().setLayout(false)
        userRepository.logout()
    }

    init {
        this.view = WeakReference(view)
        this.userRepository = userRepository
        this.schedulerProvider = schedulerProvider
        disposables = CompositeDisposable()
    }
}