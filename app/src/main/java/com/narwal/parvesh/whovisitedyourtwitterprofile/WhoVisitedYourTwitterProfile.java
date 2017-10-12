package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.app.Application;

import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by Parvesh on 09-Sep-17.
 */

public class WhoVisitedYourTwitterProfile extends Application {


    private TwitterSession twitterSession;
    private List<DirectMessage> directMessageList;
    private User user;

    Long userID;

    public TwitterSession getTwitterSession() {
        return twitterSession;
    }

    public void setTwitterSession(TwitterSession twitterSession) {
        this.twitterSession = twitterSession;
    }

    public List<DirectMessage> getDirectMessageList() {
        return directMessageList;
    }

    public void setDirectMessageList(List<DirectMessage> directMessageList) {
        this.directMessageList = directMessageList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


}
