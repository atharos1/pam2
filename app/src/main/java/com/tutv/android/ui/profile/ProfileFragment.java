package com.tutv.android.ui.profile;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.tutv.android.MainActivity;
import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.ui.login.LoginActivity;
import com.tutv.android.ui.series.SeriesActivity;
import com.tutv.android.ui.series_carrousel.SeriesCarrouselComponent;
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

public class ProfileFragment extends Fragment implements ProfileView {
    private ProfilePresenter presenter;

    private ImageView profileImage;
    private TextView username;
    private TextView mail;

    private Button signInButton;
    private Button logoutButton;
    private Button viewProfileInWebAppButton;

    private ConstraintLayout loggedInLayout;
    private LinearLayout loggedOutLayout;

    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Container diContainer = ContainerLocator.locateComponent(getContext());
        final UserRepository userRepository = diContainer.getUserRepository();
        final BaseSchedulerProvider schedulerProvider = diContainer.getSchedulerProvider();
        presenter = new ProfilePresenter(this, userRepository, schedulerProvider);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = root.findViewById(R.id.profile_image);
        username = root.findViewById(R.id.profile_username);
        mail = root.findViewById(R.id.profile_mail);

        logoutButton = root.findViewById(R.id.profile_logout_button);
        logoutButton.setOnClickListener(this::logoutButtonOnClick);

        viewProfileInWebAppButton = root.findViewById(R.id.profile_go_to_web_button);
        viewProfileInWebAppButton.setOnClickListener(this::viewProfileInWebAppButtonOnClick);

        loggedInLayout = root.findViewById(R.id.profile_logged_in_layout);
        loggedOutLayout = root.findViewById(R.id.profile_not_logged_in_layout);

        signInButton = root.findViewById(R.id.profile_sign_in_button);
        signInButton.setOnClickListener(this::signInButtonOnClick);

        progressBar = root.findViewById(R.id.profile_progressbar);

        return root;
    }

    private void signInButtonOnClick(View view) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(intent);
    }

    private void logoutButtonOnClick(View view) {
        presenter.logout();

        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

    private void viewProfileInWebAppButtonOnClick(View view) {
        presenter.openCurrentUserProfileInWebApp();
    }

    public void setLayout(boolean isLoggedIn) {
        if(isLoggedIn) {
            loggedInLayout.setVisibility(View.VISIBLE);
            loggedOutLayout.setVisibility(View.GONE);
        } else {
            loggedInLayout.setVisibility(View.GONE);
            loggedOutLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.onViewAttached();
    }

    @Override
    public void onStop() {
        super.onStop();

        presenter.onViewDetached();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfileImage(String imageBase64) {
        byte[] imageAsBytes = Base64.decode(imageBase64.getBytes(), Base64.DEFAULT);
        profileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
    }

    @Override
    public void setUsername(String text) {
        username.setText(text);
    }

    @Override
    public void setMail(String text) {
        mail.setText(text);
    }

    @Override
    public void openProfileInWebApp(int userId) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://tutv-pam.herokuapp.com/profiles/" + userId));
        startActivity(intent);
    }
}