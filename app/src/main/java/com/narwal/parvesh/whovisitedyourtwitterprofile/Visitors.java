package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Parvesh on 09-Sep-17.
 *
 */

public class Visitors extends AppCompatActivity {

    ListView visitors_list;
    List<DirectMessage> directMessages;
    WhoVisitedYourTwitterProfile app;
    List<String> screen_names;
    List<String> user_names;
    List<String> picURLs;
    FloatingActionButton fab;
    List<User> followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.visitors);

        getSupportActionBar().setTitle("Your Visitors");
        init_app();
        init_arrays();

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



        visitors_list.setAdapter(new TwitterUserListAdapter(this, user_names, screen_names, picURLs));
    }


    private void init_app() {
        visitors_list = (ListView) findViewById(R.id.list);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open share dialoage
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                String usersname_to_tweet = "";

                if (screen_names.size() > 3)
                    usersname_to_tweet = "Hi @" + screen_names.get(0) + " @" + screen_names.get(1) + " @" + screen_names.get(2) + " and others ";

                else if (screen_names.size() == 3)
                    usersname_to_tweet = "Hi @" + screen_names.get(0) + " @" + screen_names.get(1) + "and @" + screen_names.get(2);

                else if (screen_names.size() == 2)
                    usersname_to_tweet = "Hi @" + screen_names.get(0) + "and @" + screen_names.get(1);

                else if (screen_names.size() == 1)
                    usersname_to_tweet = "Hi @" + screen_names.get(0);


                String tweet = usersname_to_tweet +
                        "visited my profile! Find out yours at: https://play.google.com/store/apps/details?id=" + appPackageName;
                startTweetItFlow(tweet);
            }
        });

        app = (WhoVisitedYourTwitterProfile) getApplicationContext();
        directMessages = app.getDirectMessageList();
        followers = app.getFollowers();
    }

    private void init_arrays() {
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


}
