package com.tutv.android.ui.profile;

public interface ProfileView {
    void showToast(String text);

    void setProfileImage(String imageBase64);

    void setUsername(String text);

    void setMail(String text);

    void openProfileInWebApp(int userId);

    void setLoading(boolean loading);

    void setLayout(boolean isLoggedIn);
}
