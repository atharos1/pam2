package com.tutv.android.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator

class LoginActivity : AppCompatActivity(), LoginView, View.OnClickListener {
    private var presenter: LoginPresenter? = null
    private var editMail: EditText? = null
    private var editPassword: EditText? = null
    private var btnLogin: Button? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editMail = findViewById<View?>(R.id.loginTxtMail) as EditText?
        editPassword = findViewById<View?>(R.id.loginTxtPassword) as EditText?
        btnLogin = findViewById<View?>(R.id.btlogin) as Button?
        btnLogin.setOnClickListener(this)
        val container = ContainerLocator.locateComponent(this)
        val userRepository = container.userRepository
        val schedulerProvider = container.schedulerProvider
        presenter = LoginPresenter(this, userRepository, schedulerProvider)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setMailError(message: String?) {
        editMail.setError(message)
    }

    override fun setPasswordError(message: String?) {
        editPassword.setError(message)
    }

    override fun setLoadingStatus(status: Boolean) {
        if (status == true) progressDialog = ProgressDialog.show(this, "Iniciando sesión...", "Espere mientras iniciamos sesión...") else progressDialog.cancel()
    }

    override fun onClick(v: View?) {
        when (v.getId()) {
            R.id.btlogin -> presenter.loginBegin(editMail.getText().toString(), editPassword.getText().toString())
        }
    }

    override fun dismiss() {
        finish()
    }
}