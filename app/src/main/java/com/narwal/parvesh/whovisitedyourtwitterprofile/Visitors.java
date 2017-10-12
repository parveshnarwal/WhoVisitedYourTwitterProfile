package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parvesh on 09-Sep-17.
 */

public class Visitors extends Activity {

    ListView visitors_list;
    List<DirectMessage> directMessages;
    WhoVisitedYourTwitterProfile app;
    List<String> screen_names;
    List<String> user_names;
    List<String> picURLs;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.visitors);

        init_app();

        setUpBottomBarEvents();

        init_arrays();


        bottomBar.getTabWithId(R.id.tab_default).setVisibility(View.GONE);


        visitors_list.setAdapter(new TwitterUserListAdapter(this, user_names, screen_names, picURLs));


        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.single_row, R.id.tvSongTitle, user_names);
        //visitors_list.setAdapter(arrayAdapter);




    }



    private void init_app() {
        visitors_list = (ListView) findViewById(R.id.list);
        app  = (WhoVisitedYourTwitterProfile) getApplicationContext();
        directMessages = app.getDirectMessageList();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar_visitors);

        bottomBar.setDefaultTab(R.id.tab_default);


        bottomBar.setTabTitleTypeface(FontCache.get("font/Roboto-Light.ttf", this));



    }

    private void init_arrays() {
        int size  = directMessages.size();

        screen_names = new ArrayList<>();
        user_names = new ArrayList<>();
        picURLs = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            if(!screen_names.contains(directMessages.get(i).sender.screenName)){
                screen_names.add(directMessages.get(i).sender.screenName);
                user_names.add(directMessages.get(i).sender.name);

                String picURLPath = directMessages.get(i).sender.profileImageUrl;

                if(picURLPath.contains("_normal")){
                    picURLPath = picURLPath.replace("_normal", "");
                }
                picURLs.add(picURLPath);
            }

            if(screen_names.size() == 10) break;

        }

    }


    private void setUpBottomBarEvents() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_home_v){
                    // open log out
                    Intent intent = new Intent(Visitors.this, FindMyVistor.class);
                    startActivity(intent);

                }

                else if(tabId == R.id.tab_rate_v){
                    //open play store

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }

                else if(tabId == R.id.tab_tweet){
                    // open share dialoage
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                    String usersname_to_tweet = "";

                    if(screen_names.size() > 3)
                        usersname_to_tweet =  "Hi @" + screen_names.get(0) + " @" + screen_names.get(1) + " @" + screen_names.get(2) + " and others ";

                    else if(screen_names.size() == 3)
                        usersname_to_tweet =  "Hi @" + screen_names.get(0) + " @" + screen_names.get(1) + "and @" + screen_names.get(2);

                    else if(screen_names.size() == 2) usersname_to_tweet = "Hi @" + screen_names.get(0) + "and @" + screen_names.get(1);

                    else if(screen_names.size() == 1) usersname_to_tweet = "Hi @" + screen_names.get(0);


                    String tweet = usersname_to_tweet +
                            "visited my profile! Find out yours at: https://play.google.com/store/apps/details?id=" + appPackageName;
                    startTweetItFlow(tweet);
                }

                else if(tabId == R.id.tab_logout_v){
                    // open log out
                    Intent intent = new Intent(Visitors.this, LogOut.class);
                    startActivity(intent);

                }
            }
        });



        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {

                if(tabId == R.id.tab_home_v){
                    // open log out
                    Intent intent = new Intent(Visitors.this, FindMyVistor.class);
                    startActivity(intent);

                }

                else if(tabId == R.id.tab_rate_v){
                    //open play store

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }

                else if(tabId == R.id.tab_tweet){
                    // open share dialoage
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                    String usersname_to_tweet = "";

                    if(screen_names.size() > 3)
                        usersname_to_tweet =  "Hi @" + screen_names.get(0) + " @" + screen_names.get(1) + " @" + screen_names.get(2) + " and others ";

                    else if(screen_names.size() == 3)
                        usersname_to_tweet =  "Hi @" + screen_names.get(0) + " @" + screen_names.get(1) + "and @" + screen_names.get(2);

                    else if(screen_names.size() == 2) usersname_to_tweet = "Hi @" + screen_names.get(0) + "and @" + screen_names.get(1);

                    else if(screen_names.size() == 1) usersname_to_tweet = "Hi @" + screen_names.get(0);


                    String tweet = usersname_to_tweet +
                            "visited my profile! Find out yours at: https://play.google.com/store/apps/details?id=" + appPackageName;
                    startTweetItFlow(tweet);
                }

                else if(tabId == R.id.tab_logout_v){
                    // open log out
                    Intent intent = new Intent(Visitors.this, LogOut.class);
                    startActivity(intent);

                }

            }
        });

    }

    private void startTweetItFlow(String tweet) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, tweet);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(tweetIntent);
        }else{
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }
}
