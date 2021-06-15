package com.tutv.android.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;
import com.tutv.android.ui.series.SeriesActivity;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {
    private LoginPresenter presenter;

    private EditText editMail;
    private EditText editPassword;
    private Button btnLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        editMail = (EditText) this.findViewById(R.id.loginTxtMail);
        editPassword = (EditText) this.findViewById(R.id.loginTxtPassword);
        btnLogin = (Button) this.findViewById(R.id.btlogin);

        btnLogin.setOnClickListener(this);

        Container container = ContainerLocator.locateComponent(this);
        final UserRepository userRepository = container.getUserRepository();
        final BaseSchedulerProvider schedulerProvider = container.getSchedulerProvider();
        presenter = new LoginPresenter(this, userRepository, schedulerProvider);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setMailError(String message) {
        editMail.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        editPassword.setError(message);
    }

    @Override
    public void setLoadingStatus(boolean status) {
        if(status == true)
            progressDialog = ProgressDialog.show(this, "Iniciando sesión...", "Espere mientras iniciamos sesión...");
        else
            progressDialog.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btlogin:
                presenter.loginBegin(editMail.getText().toString(), editPassword.getText().toString());
                break;
        }
    }

    @Override
    public void dismiss() {
        Intent i = new Intent(this, SeriesActivity.class);
        i.putExtra("series_id", 66732);
        startActivity(i);
    }

}