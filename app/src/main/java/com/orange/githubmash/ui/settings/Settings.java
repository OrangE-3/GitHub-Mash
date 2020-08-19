package com.orange.githubmash.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Settings extends AppCompatActivity {
    public static final String me_owner="me_owner";
    public static final String me_descr="me_descr";
    public static final String me_watchers="me_watchers";
    public static final String fav_owner="fav_owner";
    public static final String fav_descr="fav_descr";
    public static final String fav_watchers="fav_watchers";
    public static final String user_urrl="user_urrl";
    public static final String user_avatar="user_avatar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}