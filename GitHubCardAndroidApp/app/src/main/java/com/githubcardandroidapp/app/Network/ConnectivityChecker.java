package com.githubcardandroidapp.app.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.githubcardandroidapp.app.GitHubCardActivity;

/**
 * Created by Muhammad on 6/13/2014.
 */
public class ConnectivityChecker {

    private Context context;

    public ConnectivityChecker(Context context) {
        this.context = context;
    }

    public boolean isInternetAvailable() {
        boolean ret = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null){
                ret = networkInfo.isAvailable() && networkInfo.isConnected();
            }
        }

        return ret;
    }

    /*public void registerNetworkStateChange() {
        this.context.registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }*/
}


