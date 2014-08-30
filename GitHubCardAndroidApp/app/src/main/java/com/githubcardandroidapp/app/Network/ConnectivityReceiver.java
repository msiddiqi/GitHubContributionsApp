package com.githubcardandroidapp.app.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;

public class ConnectivityReceiver extends BroadcastReceiver {

    private GitHubSyncService gitHubSyncService;
    private ConnectivityChecker connectivityChecker;
    private boolean isConnected;

    public ConnectivityReceiver(GitHubSyncService gitHubSyncService){
        this.gitHubSyncService = gitHubSyncService;
        this.connectivityChecker = new ConnectivityChecker(gitHubSyncService.getApplicationContext());
        this.isConnected = this.connectivityChecker.isInternetAvailable();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //boolean previousState = this.isConnected;
        //this.isConnected = this.connectivityChecker.isInternetAvailable();
        //if (!previousState && isConnected) {
        //    this.gitHubSyncService.setApplicationStateConnected();
        // }

        boolean isConnectedNow = this.connectivityChecker.isInternetAvailable();

        if (isConnectedNow != isConnected) {
            isConnected = isConnectedNow;
            this.gitHubSyncService.setApplicationState(isConnected);
        }
    }
}