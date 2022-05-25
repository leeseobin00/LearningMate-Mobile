package com.example.learningmate;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView connectUser;
    private TextView userName;
    private TextView disconnectUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        connectUser = findViewById(R.id.setting_link_tv);
        userName = findViewById(R.id.setting_username_tv);
        disconnectUser = findViewById(R.id.setting_disconnect_tv);
        userName.setText(User.currentUser.getUserName() + " [" + (User.currentUser.getIdentity() == 0 ? "멘티" : "멘토") + "]");
        if (!User.currentUser.getPairId().equals("null")) {
            connectUser.setVisibility(View.INVISIBLE);
            disconnectUser.setVisibility(View.VISIBLE);
            disconnectUser.setText("[" + User.currentUser.getPairId() + "] " + (User.currentUser.getIdentity() == 0 ? "멘토 해제" : "멘티 해제"));
        } else {
            disconnectUser.setVisibility(View.INVISIBLE);
            connectUser.setVisibility(View.VISIBLE);

        }
        connectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!User.currentUser.getPairId().equals("null")) {
            connectUser.setVisibility(View.INVISIBLE);
            disconnectUser.setVisibility(View.VISIBLE);
            disconnectUser.setText("[" + User.currentUser.getPairId() + "] " + (User.currentUser.getIdentity() == 0 ? "멘토 해제" : "멘티 해제"));
        } else {
            disconnectUser.setVisibility(View.INVISIBLE);
            connectUser.setVisibility(View.VISIBLE);
        }
    }
}
