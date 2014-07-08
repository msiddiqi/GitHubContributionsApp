package com.githubcardandroidapp.app.GitHubContributionsIO.Profile;

import android.util.Log;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

import java.io.IOException;

public class GitHubActivityInternalStorageAsyncTask extends GitHubProfileAsyncTask {

    public GitHubActivityInternalStorageAsyncTask(GitHubSyncService gitHubSyncService) {
        super(gitHubSyncService);
    }

    @Override
    protected GitHubProfileDetails doInBackground(String... params) {

        GitHubProfileDetails gitHubProfileDetails = null;
        String userName = params[0];

        try {
            gitHubProfileDetails = new PersistenceHandlerImpl(this.gitHubSyncService.getApplicationContext()).readProfileFromPersistence(userName);
        }
        catch (IOException ex) {
            Log.i("IO exception", ex.getMessage());
        }

        return gitHubProfileDetails;
    }
}
