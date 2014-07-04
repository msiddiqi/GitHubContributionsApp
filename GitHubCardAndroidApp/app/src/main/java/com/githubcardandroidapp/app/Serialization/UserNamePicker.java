package com.githubcardandroidapp.app.Serialization;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Muhammad on 6/21/2014.
 */
public class UserNamePicker  {

    private final String USER_NAME_PREFERENCE_ID = "pref_loginName";
    private final String DEFAULT_USERNAME = "msiddiqi";

    public String getUserName(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String retUserName = sharedPrefs.getString(USER_NAME_PREFERENCE_ID, DEFAULT_USERNAME);

        return retUserName;
    }
}
