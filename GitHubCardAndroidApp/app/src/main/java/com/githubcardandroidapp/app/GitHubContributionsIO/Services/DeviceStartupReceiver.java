package com.githubcardandroidapp.app.GitHubContributionsIO.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Muhammad on 6/20/2014.
 */
public class DeviceStartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            Intent serviceIntent = new Intent();
            serviceIntent.setAction("GitHubContributionsIO.GitHubSyncService");

            context.startService(serviceIntent);
        }
    }
}
