package com.example.rush0714.myapplication.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.rush0714.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class AuthHelper {
    public static Map<String, String> getAuthData(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Map<String, ?> preferences = sharedPreferences.getAll();
        String login = (String) preferences.get(activity.getString(R.string.login_key));
        String password = (String) preferences.get(activity.getString(R.string.pass_key));
        Map<String, String> result = new HashMap<String, String>();
        result.put("l", login);
        result.put("p", password);
        return result;
    }
}
