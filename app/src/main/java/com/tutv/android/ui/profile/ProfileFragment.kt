package com.tutv.android.ui.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tutv.android.MainActivity
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import com.tutv.android.ui.login.LoginActivity

class ProfileFragment : Fragment(), ProfileView {
    private var presenter: ProfilePresenter? = null
    private var profileImage: ImageView? = null
    private var username: TextView? = null
    private var mail: TextView? = null
    private var signInButton: Button? = null
    private var logoutButton: Button? = null
    private var viewProfileInWebAppButton: Button? = null
    private var loggedInLayout: ConstraintLayout? = null
    private var loggedOutLayout: LinearLayout? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val diContainer = ContainerLocator.locateContainer(requireContext())
        val userRepository = diContainer.userRepository
        val schedulerProvider = diContainer.schedulerProvider
        presenter = ProfilePresenter(this, userRepository, schedulerProvider)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId)
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context?.theme?.resolveAttribute(android.R.attr.actionBarSize, tv, true) == true)
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        root.setPadding(0, statusBarHeight + actionBarHeight, 0, 0)
        profileImage = root.findViewById(R.id.profile_image)
        username = root.findViewById(R.id.profile_username)
        mail = root.findViewById(R.id.profile_mail)
        logoutButton = root.findViewById(R.id.profile_logout_button)
        logoutButton?.setOnClickListener { view: View -> logoutButtonOnClick(view) }
        viewProfileInWebAppButton = root.findViewById(R.id.profile_go_to_web_button)
        viewProfileInWebAppButton?.setOnClickListener{ view: View? -> viewProfileInWebAppButtonOnClick(view) }
        loggedInLayout = root.findViewById(R.id.profile_logged_in_layout)
        loggedOutLayout = root.findViewById(R.id.profile_not_logged_in_layout)
        signInButton = root.findViewById(R.id.profile_sign_in_button)
        signInButton?.setOnClickListener { view: View -> signInButtonOnClick(view) }
        progressBar = root.findViewById(R.id.profile_progressbar)
        return root
    }

    private fun signInButtonOnClick(view: View) {
        val intent = Intent(context, LoginActivity::class.java)
        context?.startActivity(intent)
    }

    private fun logoutButtonOnClick(view: View) {
        presenter?.logout()
        val intent = Intent(context, MainActivity::class.java)
        context?.startActivity(intent)
    }

    private fun viewProfileInWebAppButtonOnClick(view: View?) {
        presenter?.openCurrentUserProfileInWebApp()
    }

    override fun setLayout(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            loggedInLayout?.visibility = View.VISIBLE
            loggedOutLayout?.visibility = View.GONE
        } else {
            loggedInLayout?.visibility = View.GONE
            loggedOutLayout?.visibility = View.VISIBLE
        }
    }

    override fun setLoading(loading: Boolean) {
        progressBar?.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onViewDetached()
    }

    override fun showToast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun setProfileImage(imageBase64: String?) {
        val imageAsBytes = Base64.decode(imageBase64?.toByteArray(), Base64.DEFAULT)
        profileImage?.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
    }

    override fun setUsername(text: String?) {
        username?.text = text
    }

    override fun setMail(text: String?) {
        mail?.text = text
    }

    override fun openProfileInWebApp(userId: Int?) {
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://tutv-pam.herokuapp.com/profiles/$userId"))
        startActivity(intent)
    }
}