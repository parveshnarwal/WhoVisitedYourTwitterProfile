package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Parvesh on 09-Sep-17.
 */

public class FindMyVisitors extends AppCompatActivity implements View.OnClickListener {

    private Button btnFindVisitors, btnLogOut;
    private TwitterSession twitterSession;
    private Long userID;
    private ImageView ivProfilePic;
    private TextView welcomeMsg;
    WhoVisitedYourTwitterProfile app;
    AVLoadingIndicatorView avLoadingIndicatorView;
    Typeface roboto;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findmyvisitor);

        init_app();
        getTwitterDetails();

    }


    private void getTwitterDetails() {

        app = (WhoVisitedYourTwitterProfile) getApplicationContext();

        twitterSession = app.getTwitterSession();

        userID = app.getUserID();

        setUserProfilePic(twitterSession);


    }


    private void init_app() {

        btnFindVisitors = (Button) findViewById(R.id.btnFindVisitors);
        btnLogOut = (Button) findViewById(R.id.LogOut);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        welcomeMsg = (TextView) findViewById(R.id.tvWelcomMsg);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
        btnFindVisitors.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        welcomeMsg.setTypeface(FontCache.get("font/Roboto-Light.ttf", this));
        btnFindVisitors.setTypeface(FontCache.get("font/Roboto-Light.ttf", this));
        btnLogOut.setTypeface(FontCache.get("font/Roboto-Light.ttf", this));


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4327820221556313/1196061324");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void setUserProfilePic(TwitterSession twitterSession) {
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twitterSession);
        Call<User> cb = myTwitterApiClient.getUserInfo().show(userID);

        cb.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                String userName = "Hi, @" + result.data.screenName;
                welcomeMsg.setText(userName);
                Picasso.with(FindMyVisitors.this).load(result.data.profileImageUrl.replace("_normal", "")).into(ivProfilePic);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnFindVisitors:
                startLoading();
                findMyVisitors();
                break;

            case R.id.LogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("LogOutPressEvent", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("IsLogOutPressed", true);
                editor.apply();

                Intent intent = new Intent(FindMyVisitors.this, LogIn.class);
                startActivity(intent);
                break;
        }


    }

    private void findMyVisitors() {
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twitterSession);

        Call<List<DirectMessage>> cb = myTwitterApiClient.getDMs().show(200);

        cb.enqueue(new Callback<List<DirectMessage>>() {
            @Override
            public void success(Result<List<DirectMessage>> result) {
                List<DirectMessage> DMs = result.data;
                app.setDirectMessageList(DMs);
                getFollowersList();

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(FindMyVisitors.this, "Sorry! Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        // we have got the DMs now get some followers

    }

    public void getFollowersList() {

        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twitterSession);

        Call<ListOfUser> cb_followers = myTwitterApiClient.getFollowersList().show();
        cb_followers.enqueue(new Callback<ListOfUser>() {
            @Override
            public void success(Result<ListOfUser> result) {
                List<User> listOfUsers = result.data.users;
                app.setFollowers(listOfUsers);
                if (listOfUsers.size() > 0) {
                    Intent intent = new Intent(FindMyVisitors.this, Visitors.class);
                    stopLoading();
                    startActivity(intent);
                } else {
                    Toast.makeText(FindMyVisitors.this, "Sorry! We could not find any visitors on your profile. Please check again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(FindMyVisitors.this, "Sorry! Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void startLoading() {
        ivProfilePic.setVisibility(View.GONE);
        btnFindVisitors.setVisibility(View.GONE);
        btnLogOut.setVisibility(View.GONE);
        welcomeMsg.setVisibility(View.GONE);
        avLoadingIndicatorView.show();

        getSupportActionBar().hide();

    }

    void stopLoading() {
        avLoadingIndicatorView.hide();
        ivProfilePic.setVisibility(View.VISIBLE);
        btnLogOut.setVisibility(View.VISIBLE);
        btnFindVisitors.setVisibility(View.VISIBLE);
        welcomeMsg.setVisibility(View.VISIBLE);
        getSupportActionBar().show();

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

}

