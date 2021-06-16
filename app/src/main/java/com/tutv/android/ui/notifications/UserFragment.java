package com.tutv.android.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment implements UserView {

    private UserPresenter userPresenter;
    private CircleImageView userAvatarCircleImageView;
    private TextView userNameTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user, container, false);

        this.userAvatarCircleImageView = root.findViewById(R.id.useravatar_circleimageview);
        this.userNameTextView = root.findViewById(R.id.username_textview);

        Container appContainer = ContainerLocator.locateComponent(getContext());
        UserRepository userRepository = appContainer.getUserRepository();
        BaseSchedulerProvider schedulerProvider = appContainer.getSchedulerProvider();
        this.userPresenter = new UserPresenter(this, userRepository, schedulerProvider);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        userPresenter.onViewAttached();
    }

    @Override
    public void showUserAvatar(String avatarUrl) {
        Picasso.get().load("https://tutv-pam.herokuapp.com/" + avatarUrl).fit()
                .into(userAvatarCircleImageView);
    }

    @Override
    public void showUserName(String userName) {
        userNameTextView.setText(userName);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG);
    }

}