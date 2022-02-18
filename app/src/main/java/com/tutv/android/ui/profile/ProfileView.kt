package com.tutv.android.ui.profile

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface ProfileView {
    open fun showToast(text: String?)
    open fun setProfileImage(imageBase64: String?)
    open fun setUsername(text: String?)
    open fun setMail(text: String?)
    open fun openProfileInWebApp(userId: Int)
    open fun setLoading(loading: Boolean)
    open fun setLayout(isLoggedIn: Boolean)
}