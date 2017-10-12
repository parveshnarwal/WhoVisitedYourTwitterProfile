package com.narwal.parvesh.whovisitedyourtwitterprofile;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Parvesh on 15-Aug-17.
 */

public class ListOfUser {

    @SerializedName("users")
    public final List<User> users;

    public ListOfUser(List<User> users) {
        this.users = users;
    }

}


class ListOfIDs{

    @SerializedName("ids")
    public final List<Long> ids;

    public ListOfIDs(List<Long> ids){
        this.ids = ids;
    }


}

class DirectMessage{
    @SerializedName("recipient_id")
    public final Long recipient_id;

    @SerializedName("sender_id")
    public final Long sender_id;

    @SerializedName("text")
    public final String directMsg;

    @SerializedName("sender")
    public final User sender;

    @SerializedName("recipient")
    public final User recipient;

    public DirectMessage(Long recipient_id, Long sender_id, String directMsg, User sender, User recipient) {
        this.recipient_id = recipient_id;
        this.sender_id = sender_id;
        this.directMsg = directMsg;
        this.sender = sender;
        this.recipient = recipient;
    }
}

