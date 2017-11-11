package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Parvesh on 09-Sep-17.
 *
 */

public class Visitors extends AppCompatActivity implements RewardedVideoAdListener {

    ListView visitors_list;
    List<DirectMessage> directMessages;
    WhoVisitedYourTwitterProfile app;
    List<String> screen_names, user_names, picURLs;

    FloatingActionButton fab;
    List<User> followers;
    LinearLayout loadVideoBanner;
    TextView loadVideoText;
    RewardedVideoAd rewardedVideoAd;
    Boolean isUserRequestedVideo = false;
    Boolean isUserRewarded = false;
    TwitterUserListAdapter twitterUserListAdapter;

    int profileIndex;


    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.visitors);

        getSupportActionBar().setTitle("Your Visitors");
        init_app();
        init_arrays();

        shuffleVisitors();

        if(!isUserRewarded){
            user_names.remove(0);
            screen_names.remove(0);
            picURLs.remove(0);
        }


        twitterUserListAdapter = new TwitterUserListAdapter(this, user_names, screen_names, picURLs, isUserRewarded);

        visitors_list.setAdapter(twitterUserListAdapter);


        visitors_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                profileIndex = i;

                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }

                else{
                    //Toast.makeText(Visitors.this, screen_names.get(i), Toast.LENGTH_SHORT).show();
                    OpenTwitterProfile(screen_names.get(i));
                }


            }
        });



    }

    private void shuffleVisitors() {

        try {
            int dms_size = screen_names.size();
            int foll_size = followers.size();

            if(foll_size > 0)    getDataFromFollowers(followers);


            if(dms_size >= 3  && foll_size >= 3) {
                Collections.swap(user_names, dms_size, 1);
                Collections.swap(screen_names, dms_size, 1);
                Collections.swap(picURLs, dms_size, 1);

                Collections.swap(user_names, 3, dms_size + 1);
                Collections.swap(screen_names, 3, dms_size + 1);
                Collections.swap(picURLs, 3, dms_size + 1);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logout();
        }
    }

    private void OpenTwitterProfile(String screenName) {
        Toast.makeText(this, screenName, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + screenName)));
    }


    private void init_app() {
        visitors_list = (ListView) findViewById(R.id.list);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        app = (WhoVisitedYourTwitterProfile) getApplicationContext();
        directMessages = app.getDirectMessageList();
        followers = app.getFollowers();

        loadVideoBanner = (LinearLayout) findViewById(R.id.llLoadVideo);

        YoYo.with(Techniques.Pulse).duration(2000).repeat(3).playOn(loadVideoBanner);

        loadVideoText = (TextView) findViewById(R.id.tvLoadVideo);

        loadVideoText.setTypeface(FontCache.get("font/Roboto-Light.ttf" ,this));

        loadVideoBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rewardedVideoAd.isLoaded()) rewardedVideoAd.show();
                
                else{
                    Toast.makeText(Visitors.this, "Loading. Please wait...", Toast.LENGTH_SHORT).show();
                    loadRewardVideoAd();
                    isUserRequestedVideo = true;
                }
            }
        });

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        rewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardVideoAd();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4327820221556313/1785141696");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                OpenTwitterProfile(screen_names.get(profileIndex));

            }


        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open share dialoage
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object


                String usersname_to_tweet = "Hi";

                for(int i=0; i< screen_names.size(); i++){
                    usersname_to_tweet = usersname_to_tweet + " @" + screen_names.get(i);

                    if(usersname_to_tweet.length() > 170) break;
                }


                String tweet = usersname_to_tweet +
                        " visited my profile! Find out yours at: https://play.google.com/store/apps/details?id=" + appPackageName;
                startTweetItFlow(tweet);
            }
        });


    }

    private void loadRewardVideoAd() {

        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("974AAE8ACE16E9601BF40DD60AA9BBC5").build();
        rewardedVideoAd.loadAd("ca-app-pub-4327820221556313/2711765463", new AdRequest.Builder().build());

        //rewardedVideoAd.loadAd("ca-app-pub-4327820221556313/2711765463", adRequest);


    }

    private void init_arrays() {

        try {
            int size = directMessages.size();

            screen_names = new ArrayList<>();
            user_names = new ArrayList<>();
            picURLs = new ArrayList<>();

            for (int i = 0; i < size; i++) {

                if (!screen_names.contains(directMessages.get(i).sender.screenName) &&  !directMessages.get(i).sender.verified ) {
                    screen_names.add(directMessages.get(i).sender.screenName);
                    user_names.add(directMessages.get(i).sender.name);



                    String picURLPath = directMessages.get(i).sender.profileImageUrl;

                    if (picURLPath.contains("_normal")) {
                        picURLPath = picURLPath.replace("_normal", "");
                    }
                    picURLs.add(picURLPath);
                }

                if (screen_names.size() == 10) break;


            }
        } catch (Exception e) {
            e.printStackTrace();

            logout();
        }


    }

    private void logout() {

        Toast.makeText(this, "Sorry! Something went wrong!", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("LogOutPressEvent", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("IsLogOutPressed", true);
        editor.apply();

        Intent intent = new Intent(Visitors.this, LogIn.class);
        startActivity(intent);
    }

    private void startTweetItFlow(String tweet) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, tweet);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }


    private void getDataFromFollowers(List<User> followers) {

        for (int i = 0; i < followers.size(); i++) {
            if (!screen_names.contains(followers.get(i).screenName)  && !followers.get(i).verified) {
                screen_names.add(followers.get(i).screenName);
                user_names.add(followers.get(i).name);


                String picURLPath = followers.get(i).profileImageUrl;

                if (picURLPath.contains("_normal")) {
                    picURLPath = picURLPath.replace("_normal", "");
                }
                picURLs.add(picURLPath);

            }

            if (screen_names.size() == 20) break;

        }

    }


    @Override
    public void onRewardedVideoAdLoaded() {
        if(isUserRequestedVideo) rewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        
        // here give user the reward
        Toast.makeText(this, "You have successfully unlocked the 1st user!", Toast.LENGTH_SHORT).show();
        isUserRewarded = true;
        init_arrays();

        shuffleVisitors();

        twitterUserListAdapter = new TwitterUserListAdapter(this, user_names, screen_names, picURLs, isUserRewarded);

        visitors_list.setAdapter(twitterUserListAdapter);

        loadVideoBanner.setVisibility(View.GONE);

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }





}
