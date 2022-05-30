package com.example.learningmate.common_activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.learningmate.R;
import com.example.learningmate.common_activity.LoginActivity;
import com.example.learningmate.common_activity.SearchActivity;
import com.example.learningmate.model.User;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView connectUser;
    private TextView userName;
    private TextView disconnectUser;
    private TextView logoutTextView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        logoutTextView = findViewById(R.id.setting_logout_tv);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.currentUser = null;
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
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
