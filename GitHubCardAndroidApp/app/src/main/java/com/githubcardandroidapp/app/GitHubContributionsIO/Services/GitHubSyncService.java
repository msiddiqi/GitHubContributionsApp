package com.githubcardandroidapp.app.GitHubContributionsIO.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.GitHubContributionsIO.Profile.GitHubActivityInternalStorageAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Profile.GitHubActivityOnlineUserProfileAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Profile.GitHubProfileAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Repositories.GitHubActivityOnlineUserRepositoriesAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Repositories.GitHubInternalStorageUserRepositoriesAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Repositories.GitHubUserRepositoriesAsyncTask;
import com.githubcardandroidapp.app.Network.ConnectivityChecker;
import com.githubcardandroidapp.app.Network.ConnectivityReceiver;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;
import com.githubcardandroidapp.app.Serialization.UserNamePicker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Muhammad on 6/19/2014.
 */
public class GitHubSyncService extends Service {

    public final static String IsConnectedExtra = "isConnected";

    private ScheduledExecutorService scheduler;
    private ConnectivityChecker connectivityChecker;
    private PersistenceHandler persistenceHandler;
    private GitHubUserRepositories gitHubUserRepositories;
    private GitHubProfileDetails gitHubProfileDetails;

    public static final String GetDataReceiverAction = "GetData";

    private boolean isRegisteredForDownload;

    public GitHubSyncService() {

        //super("GitHubSyncService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isRegisteredForDownload = false;
        if (generateConnectivityStateIntent()) {
            registerForGitHubProfileDownload();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.connectivityChecker = new ConnectivityChecker(this);
        this.persistenceHandler = new PersistenceHandlerImpl(this);
        registerConnectivityReceiver();
    }

    public void registerForGitHubProfileDownload() {

        if (!isRegisteredForDownload) {
            if (scheduler != null) {
                scheduler.shutdownNow();
                scheduler = null;
            }

            scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    loadUserDetails();
                }
            }, 0, 3, TimeUnit.HOURS);

            isRegisteredForDownload = true;
        }
    }

    public void updateRepositoriesList(GitHubUserRepositories userRepositories) {

        broadCastRepositories();
    }

    public void updateUserProfile(GitHubProfileDetails profileDetails) {

        broadCastUserProfile();
    }

    public void setApplicationStateConnected() {
        generateConnectivityStateIntent();
        this.isRegisteredForDownload = false;

        this.registerForGitHubProfileDownload();
    }

    private void broadCastRepositories() {
        Intent intentActivity = getGitHubCardReceiverIntent();
        intentActivity.setAction(GitHubCardActivityReceiver.UpdateRepositoriesAction);

        String userName = new UserNamePicker().getUserName(getApplicationContext());
        intentActivity.putExtra(GitHubCardActivity.RepositoriesExtra, userName);
        sendBroadcast(intentActivity);
    }

    private void broadCastUserProfile() {
        Intent intentActivity = getGitHubCardReceiverIntent();
        intentActivity.setAction(GitHubCardActivityReceiver.UpdateUserProfileAction);

        String userName = new UserNamePicker().getUserName(getApplicationContext());
        intentActivity.putExtra(GitHubCardActivity.ProfileExtra, userName);
        sendBroadcast(intentActivity);
    }

    private void loadUserDetails() {

        String userName = new UserNamePicker().getUserName(getApplicationContext());

        boolean isConnectedToNetwork = this.connectivityChecker.isInternetAvailable();
        boolean isLoadFromPersistence =
                    this.persistenceHandler.isPersistedDataCurrent(userName) || !isConnectedToNetwork;

        GitHubUserRepositoriesAsyncTask gitHubUserRepositoriesAsyncTask = null;
        GitHubProfileAsyncTask gitHubProfileAsyncTask = null;

        if (isLoadFromPersistence) {
            gitHubUserRepositoriesAsyncTask = new GitHubInternalStorageUserRepositoriesAsyncTask(this);
            gitHubProfileAsyncTask =  new GitHubActivityInternalStorageAsyncTask(this);
        }
        else if(isConnectedToNetwork) {
            gitHubUserRepositoriesAsyncTask = new GitHubActivityOnlineUserRepositoriesAsyncTask(this);
            gitHubProfileAsyncTask = new GitHubActivityOnlineUserProfileAsyncTask(this);
        }

        if (gitHubProfileAsyncTask != null && gitHubUserRepositoriesAsyncTask != null) {
            gitHubUserRepositoriesAsyncTask.execute(userName);
            gitHubProfileAsyncTask.execute(userName);
        }
    }

    private Intent getGitHubCardReceiverIntent() {
        Intent intentActivity = new Intent();
        return intentActivity;
    }

    private boolean generateConnectivityStateIntent() {

        String userName = new UserNamePicker().getUserName(getApplicationContext());
        boolean appStateDataDisplayed =
                connectivityChecker.isInternetAvailable() || persistenceHandler.isPersistedDataAvailable(userName);

        Intent intentActivity = new Intent();
        intentActivity.setAction(GitHubCardActivityReceiver.UpdateConnectedStatusAction);
        intentActivity.putExtra(GitHubCardActivity.IsConnectedExtra, appStateDataDisplayed);

        sendBroadcast(intentActivity);

        return appStateDataDisplayed;
    }

    private void registerConnectivityReceiver() {
        this.registerReceiver(
                new ConnectivityReceiver(this),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

}