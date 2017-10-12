package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
/**
 * Created by Parvesh on 09-Sep-17.
 */

public class FindMyVistor extends Activity implements View.OnClickListener {

    private Button btnFindVisitors;
    private TwitterSession twitterSession;
    private Long userID;
    private CircleImageView ivProfilePic;
    private TextView welcomeMsg;
    WhoVisitedYourTwitterProfile app;
    AVLoadingIndicatorView avLoadingIndicatorView;
    BottomBar bottomBar;
    Typeface roboto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findmyvistor);

        init_app();
        setUpBottomBarEvents();
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
        ivProfilePic = (CircleImageView) findViewById(R.id.ivProfilePic);
        welcomeMsg = (TextView)  findViewById(R.id.tvWelcomMsg);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar_fmv);
        btnFindVisitors.setOnClickListener(this);

        welcomeMsg.setTypeface(FontCache.get("font/Roboto-Light.ttf", this));
        btnFindVisitors.setTypeface(FontCache.get("font/Roboto-Light.ttf", this));

        bottomBar.setTabTitleTypeface(FontCache.get("font/Roboto-Light.ttf", this));

    }

    private void setUserProfilePic(TwitterSession twitterSession) {
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twitterSession);
        Call<User> cb = myTwitterApiClient.getUserInfo().show(userID);

        cb.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                String userName  = "Hi, @" + result.data.screenName;
                welcomeMsg.setText(userName);
                Picasso.with(FindMyVistor.this).load(result.data.profileImageUrl.replace("_normal","")).into(ivProfilePic);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnFindVisitors:
                startLoading();
                findMyVisitors();
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
                if(DMs.size() > 0){
                    Intent intent = new Intent(FindMyVistor.this, Visitors.class);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(FindMyVistor.this, "Sorry! We could not find any visitors on your profile. Please check again.", Toast.LENGTH_SHORT).show();
                }

                stopLoading();
            }
            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    void startLoading(){
        ivProfilePic.setVisibility(View.GONE);
        btnFindVisitors.setVisibility(View.GONE);
        welcomeMsg.setVisibility(View.GONE);
        bottomBar.setVisibility(View.GONE);
        avLoadingIndicatorView.show();
    }

    void stopLoading(){
        avLoadingIndicatorView.hide();
        ivProfilePic.setVisibility(View.VISIBLE);
        btnFindVisitors.setVisibility(View.VISIBLE);
        welcomeMsg.setVisibility(View.VISIBLE);
        bottomBar.setVisibility(View.VISIBLE);

    }



    private void setUpBottomBarEvents() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_home){
                        //do  nothing

                }

                else if(tabId == R.id.tab_rate){
                    //open play store

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }

                else if(tabId == R.id.tab_share){
                    // open share dialoage
                    String textToShare =
                            "Find out who visited your Twitter Profile here: https://play.google.com/store/apps/details?id="
                                    + getApplicationContext().getPackageName();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, textToShare);
                    intent.setType("text/plain");
                    Intent.createChooser(intent, "Share via");
                    startActivity(intent);
                }

                else if(tabId == R.id.tab_logout){
                    // open log out
                    Intent intent = new Intent(FindMyVistor.this, LogOut.class);
                    startActivity(intent);

                }
            }
        });


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_home){
                    //do  nothing

                }

                else if(tabId == R.id.tab_rate){
                    //open play store

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }

                else if(tabId == R.id.tab_share){
                    // open share dialoage
                    String textToShare =
                            "Find out who visited your Twitter Profile here: https://play.google.com/store/apps/details?id="
                                    + getApplicationContext().getPackageName();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, textToShare);
                    intent.setType("text/plain");
                    Intent.createChooser(intent, "Share via");
                    startActivity(intent);
                }

                else if(tabId == R.id.tab_logout){
                    // open log out
                    Intent intent = new Intent(FindMyVistor.this, LogOut.class);
                    startActivity(intent);

                }

            }
        });
    }



}
