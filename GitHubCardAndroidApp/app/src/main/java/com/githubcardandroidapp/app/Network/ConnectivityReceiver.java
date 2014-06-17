package com.githubcardandroidapp.app.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.githubcardandroidapp.app.GitHubCardActivity;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        GitHubCardActivity gitHubCardActivity = (GitHubCardActivity)context;

        if (gitHubCardActivity != null) {

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnected()) {
                gitHubCardActivity.registerForGitHubProfileDownload();
            } else {
                gitHubCardActivity.registerForOffline();
            }
        }
    }
}