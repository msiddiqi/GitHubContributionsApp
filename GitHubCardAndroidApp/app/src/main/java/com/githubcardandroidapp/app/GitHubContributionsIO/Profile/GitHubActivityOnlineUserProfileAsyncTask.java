package com.githubcardandroidapp.app.GitHubContributionsIO.Profile;

import android.util.Log;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubContributionsIO.HttpClientProfileDetailsDownloader;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

import org.json.JSONException;
import java.io.IOException;

public class GitHubActivityOnlineUserProfileAsyncTask extends GitHubProfileAsyncTask {

    public GitHubActivityOnlineUserProfileAsyncTask(GitHubSyncService gitHubSyncService) {

        super(gitHubSyncService);
    }

    @Override
    protected GitHubProfileDetails doInBackground(String... params) {

        GitHubProfileDetails gitHubProfileDetails = null;
        PersistenceHandler persistenceHandler = new PersistenceHandlerImpl(this.gitHubSyncService.getApplicationContext());

        String userName = params[0];

        try {

            gitHubProfileDetails =
                    new HttpClientProfileDetailsDownloader().downloadProfileDetails(userName);

            persistenceHandler.serializeProfile(userName, gitHubProfileDetails);

        } catch (IOException e) {
            Log.i("IO exception : profile", e.getMessage());
        } catch (JSONException e) {
            Log.i("JSON exception : profile", e.getMessage());
        }

        return gitHubProfileDetails;
    }
}