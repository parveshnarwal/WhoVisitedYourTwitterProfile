package com.narwal.parvesh.whovisitedyourtwitterprofile;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Parvesh on 14-Aug-17.
 */

public interface CustomService {


    @GET("/1.1/followers/list.json")
    Call<ListOfUser>     show(@Query("cursor") Long cursor,
                         @Query("screen_name") String var,
                         @Query("skip_status") Boolean var1,
                         @Query("include_user_entities") Boolean var2);
}


interface FollowersList {

    @GET("/1.1/followers/list.json")
    Call<ListOfUser>     show();
}

interface FollowersIDs{
    @GET("/1.1/followers/ids.json")
    Call<ListOfIDs>     show(
            @Query("user_id") Long userID,
            @Query("count") int count,
            @Query("cursor") int cusor
    );
}


interface DirectMessageListService{
    @GET("/1.1/direct_messages.json")
    Call<List<DirectMessage>> show(
            @Query("count") int count
    );

}

interface GetResponseBody{
    @GET("/1.1/direct_messages/events/list.json")
    Call<ResponseBody> show();
}

interface GetUserInfo{
    @GET("/1.1/users/show.json")
    Call<User> show(
            @Query("user_id") Long userID
    );
}
