package com.githubcardandroidapp.app.GitHubContributionsIO.Repositories;

import android.os.AsyncTask;

import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubCardActivity;

public abstract class GitHubUserRepositoriesAsyncTask extends AsyncTask<String, GitHubUserRepositories, GitHubUserRepositories> {

    protected GitHubCardActivity gitHubCardActivity;

    public GitHubUserRepositoriesAsyncTask(GitHubCardActivity gitHubCardActivity) {

        this.gitHubCardActivity = gitHubCardActivity;
    }

    @Override
    protected void onPostExecute(GitHubUserRepositories userRepositories) {
        if (!this.isCancelled() && userRepositories != null) {
            gitHubCardActivity.updateRepositoriesList(userRepositories);
        }
    }
}
