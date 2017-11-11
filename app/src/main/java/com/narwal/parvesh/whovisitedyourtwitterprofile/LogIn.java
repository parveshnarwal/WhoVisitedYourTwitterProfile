package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LogIn extends Activity {

    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initializeTwitter();

        setContentView(R.layout.activity_log_in);

        final SharedPreferences sharedPreferences = getSharedPreferences("LogOutPressEvent", Context.MODE_PRIVATE);

        Boolean IsLogOutPressed = sharedPreferences.getBoolean("IsLogOutPressed", false);


        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null && !IsLogOutPressed) {

            TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();

            Toast.makeText(this, "Already Logged In As: @" + twitterSession.getUserName(), Toast.LENGTH_SHORT).show();

            WhoVisitedYourTwitterProfile app = (WhoVisitedYourTwitterProfile) getApplication();

            app.setTwitterSession(twitterSession);
            app.setUserID(twitterSession.getUserId());


            Intent intent = new Intent(LogIn.this, FindMyVisitors.class);

            startActivity(intent);


        }

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);


        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                final TwitterSession activeSession = TwitterCore.getInstance()
                        .getSessionManager().getActiveSession();

                WhoVisitedYourTwitterProfile app = (WhoVisitedYourTwitterProfile) getApplication();

                app.setTwitterSession(activeSession);

                app.setUserID(result.data.getUserId());

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("IsLogOutPressed", false);

                editor.apply();

                Intent intent = new Intent(LogIn.this, FindMyVisitors.class);

                startActivity(intent);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.CONSUMER_KEY), getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }
}
