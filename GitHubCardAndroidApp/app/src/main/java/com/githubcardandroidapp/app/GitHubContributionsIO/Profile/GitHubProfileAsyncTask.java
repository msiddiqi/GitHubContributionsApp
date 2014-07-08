package com.githubcardandroidapp.app.GitHubContributionsIO.Profile;

import android.os.AsyncTask;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;

public abstract class GitHubProfileAsyncTask extends AsyncTask<String, GitHubProfileDetails, GitHubProfileDetails> {

    protected GitHubSyncService gitHubSyncService;

    public GitHubProfileAsyncTask(GitHubSyncService gitHubSyncService) {

        this.gitHubSyncService = gitHubSyncService;
    }

    @Override
    protected void onPostExecute(GitHubProfileDetails profileDetails) {
        if (!this.isCancelled() && profileDetails != null) {
            this.gitHubSyncService.updateUserProfile(profileDetails);
        }
    }
}