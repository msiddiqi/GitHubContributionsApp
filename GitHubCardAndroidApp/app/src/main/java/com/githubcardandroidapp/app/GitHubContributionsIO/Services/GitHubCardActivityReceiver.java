package com.githubcardandroidapp.app.GitHubContributionsIO.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositoriesImpl;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Muhammad on 6/21/2014.
 */
public class GitHubCardActivityReceiver extends BroadcastReceiver {

    GitHubCardActivity gitHubCardActivity;
    public static final String IntentFilter = "GitHubCardActivityReceiver.IntentFilter";

    public static final String UpdateConnectedStatusAction = "UpdateConnectedStatus";
    public static final String UpdateUserProfileAction = "UpdateUserProfile";
    public static final String UpdateRepositoriesAction = "UpdateRepositories";
    private final PersistenceHandler persistenceHandler;

    public GitHubCardActivityReceiver(GitHubCardActivity gitHubCardActivity) {
        persistenceHandler = new PersistenceHandlerImpl(gitHubCardActivity);
        this.gitHubCardActivity = gitHubCardActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(UpdateConnectedStatusAction)) {

            boolean isConnected = intent.getBooleanExtra(GitHubCardActivity.IsConnectedExtra, false);
            gitHubCardActivity.setConnected(isConnected);
        }

        if (intent.getAction().equals(UpdateUserProfileAction)) {

            String userName = intent.getStringExtra(GitHubCardActivity.ProfileExtra);
            GitHubProfileDetails gitHubProfileDetails = null;

            try {
                gitHubProfileDetails = persistenceHandler.readProfileFromPersistence(userName);
            }
            catch (IOException exception) {
                Log.i("GitHubCardActivityReceiver", exception.getMessage());
            }

            if (gitHubProfileDetails != null) {
                this.gitHubCardActivity.updateUserProfile(gitHubProfileDetails);
            }
        }

        if (intent.getAction().equals(UpdateRepositoriesAction)) {

            String userName = intent.getStringExtra(GitHubCardActivity.RepositoriesExtra);
            GitHubUserRepositories gitHubUserRepositories = null;// = new Gson().fromJson(gitHubUserRepositoriesJson, GitHubUserRepositoriesImpl.class);

            try {
                gitHubUserRepositories = persistenceHandler.readUserRepositoriesFromPersistence(userName);
            }
            catch (IOException exception) {
                Log.i("GitHubCardActivityReceiver", exception.getMessage());
            }

            if (gitHubUserRepositories != null) {
                this.gitHubCardActivity.updateRepositoriesList(gitHubUserRepositories);
            }
        }
    }
}
