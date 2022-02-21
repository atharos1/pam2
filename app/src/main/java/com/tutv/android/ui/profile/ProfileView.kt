package com.tutv.android.ui.profile

interface ProfileView {
    fun showToast(text: String?)
    fun setProfileImage(imageBase64: String?)
    fun setUsername(text: String?)
    fun setMail(text: String?)
    fun openProfileInWebApp(userId: Int?)
    fun setLoading(loading: Boolean)
    fun setLayout(isLoggedIn: Boolean)
}