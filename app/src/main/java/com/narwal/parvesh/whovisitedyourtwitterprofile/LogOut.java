package com.narwal.parvesh.whovisitedyourtwitterprofile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by Parvesh on 30-Sep-17.
 *
 */

public class LogOut extends Activity implements View.OnClickListener {

    Button logout, goBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);

        setup_activity();
    }

    private void setup_activity() {
        logout = (Button) findViewById(R.id.btnLogOut);
        goBack = (Button) findViewById(R.id.btnGoBack);

        logout.setTypeface(FontCache.get("font/Roboto-Light.ttf" ,this));
        goBack.setTypeface(FontCache.get("font/Roboto-Light.ttf" ,this));

        logout.setOnClickListener(this);
        goBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnLogOut:

                Intent intent = new Intent(LogOut.this, LogIn.class);
                startActivity(intent);
                break;

            case R.id.btnGoBack:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
