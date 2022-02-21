package com.tutv.android.ui.profile

import com.tutv.android.domain.User
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class ProfilePresenter(
        view: ProfileView?,
        var userRepository: UserRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val view: WeakReference<ProfileView?> = WeakReference(view)
    private var disposables: CompositeDisposable = CompositeDisposable()
    private var user: User? = null

    fun onViewAttached() {
        if (disposables.isDisposed) disposables = CompositeDisposable()
        view.get()?.setLoading(true)

        disposables.add(userRepository.getCurrentUser(false)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ user: User -> loadSuccessful(user) }) { e: Throwable? -> loadError(e) }
        )
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun loadSuccessful(user: User) {
        this.user = user
        view.get()?.setLoading(false)
        view.get()?.setLayout(true)
        view.get()?.setMail(user.mail)
        view.get()?.setUsername(user.userName)
        view.get()?.setProfileImage(user.avatar)
    }

    private fun loadError(e: Throwable?) {
        val actualView = view.get()
        if (actualView != null) {
            actualView.setLoading(false)
            actualView.setLayout(false)
        }
    }

    fun openCurrentUserProfileInWebApp() {
        view.get()?.openProfileInWebApp(user?.id)
    }

    fun logout() {
        view.get()?.setLayout(false)
        userRepository.logout()
    }

}