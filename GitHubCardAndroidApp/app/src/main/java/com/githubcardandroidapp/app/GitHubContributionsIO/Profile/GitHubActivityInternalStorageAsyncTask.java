package com.githubcardandroidapp.app.GitHubContributionsIO.Profile;

import android.util.Log;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

import java.io.IOException;

public class GitHubActivityInternalStorageAsyncTask extends GitHubProfileAsyncTask {

    public GitHubActivityInternalStorageAsyncTask(GitHubCardActivity gitHubCardActivity) {
        super(gitHubCardActivity);
    }

    @Override
    protected GitHubProfileDetails doInBackground(String... params) {

        GitHubProfileDetails gitHubProfileDetails = null;
        try {
            gitHubProfileDetails = new PersistenceHandlerImpl(this.gitHubCardActivity).readProfileFromPersistence();
        }
        catch (IOException ex) {
            Log.i("IO exception", ex.getMessage());
        }

        return gitHubProfileDetails;
    }
}
