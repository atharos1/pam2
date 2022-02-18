package com.tutv.android.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import de.hdodenhof.circleimageview.CircleImageView
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class UserFragment : Fragment(), UserView {
    private var userPresenter: UserPresenter? = null
    private var userAvatarCircleImageView: CircleImageView? = null
    private var userNameTextView: TextView? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        userAvatarCircleImageView = root.findViewById(R.id.useravatar_circleimageview)
        userNameTextView = root.findViewById<TextView?>(R.id.username_textview)
        val appContainer = ContainerLocator.locateComponent(context)
        val userRepository = appContainer.userRepository
        val schedulerProvider = appContainer.schedulerProvider
        userPresenter = UserPresenter(this, userRepository, schedulerProvider)
        return root
    }

    override fun onStart() {
        super.onStart()
        userPresenter.onViewAttached()
    }

    override fun showUserAvatar(avatarUrl: String?) {
        Picasso.get().load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png").fit()
                .into(userAvatarCircleImageView)
    }

    override fun showUserName(userName: String?) {
        userNameTextView.setText(userName)
    }

    override fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}