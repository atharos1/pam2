package com.tutv.android.ui.login;

public interface LoginView {
    void showToast(String message);

    void setMailError(String message);

    void setPasswordError(String message);

    void setLoadingStatus(boolean status);

    void dismiss();
}
