package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.visitors);

        init_app();
        init_arrays();

        visitors_list.setAdapter(new TwitterUserListAdapter(this, user_names, screen_names, picURLs));
    }


    private void init_app() {
        visitors_list = (ListView) findViewById(R.id.list);
        app = (WhoVisitedYourTwitterProfile) getApplicationContext();
        directMessages = app.getDirectMessageList();
    }

    private void init_arrays() {
        int size = directMessages.size();

        screen_names = new ArrayList<>();
        user_names = new ArrayList<>();
        picURLs = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            if (!screen_names.contains(directMessages.get(i).sender.screenName)) {
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


}
