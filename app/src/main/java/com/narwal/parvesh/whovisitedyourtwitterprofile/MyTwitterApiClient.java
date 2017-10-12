package com.narwal.parvesh.whovisitedyourtwitterprofile;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public CustomService getCustomService() {

        return getService(CustomService.class);
    }

    public FollowersIDs getFollowersIDs(){
        return getService(FollowersIDs.class);
    }

    public DirectMessageListService getDMs(){
        return getService(DirectMessageListService.class);
    }

    public GetResponseBody getResponseBody(){
        return getService(GetResponseBody.class);
    }

    public GetUserInfo getUserInfo(){
        return  getService(GetUserInfo.class);
    }

}


