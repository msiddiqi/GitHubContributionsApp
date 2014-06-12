package com.githubcardandroidapp.app.GitHubContributionsIO.Profile;

import android.os.AsyncTask;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubCardActivity;

public abstract class GitHubProfileAsyncTask extends AsyncTask<String, GitHubProfileDetails, GitHubProfileDetails> {

    protected GitHubCardActivity gitHubCardActivity;

    public GitHubProfileAsyncTask(GitHubCardActivity gitHubCardActivity) {

        this.gitHubCardActivity = gitHubCardActivity;
    }

    @Override
    protected void onPostExecute(GitHubProfileDetails profileDetails) {
        if (!this.isCancelled() && profileDetails != null) {
            this.gitHubCardActivity.updateUserProfile(profileDetails);
        }
    }
}