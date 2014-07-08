package com.githubcardandroidapp.app.GitHubContributionsIO.Repositories;

import android.os.AsyncTask;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;

public abstract class GitHubUserRepositoriesAsyncTask extends AsyncTask<String, GitHubUserRepositories, GitHubUserRepositories> {

    protected GitHubSyncService gitHubSyncService;

    public GitHubUserRepositoriesAsyncTask(GitHubSyncService gitHubCardActivity) {

        this.gitHubSyncService = gitHubCardActivity;
    }

    @Override
    protected void onPostExecute(GitHubUserRepositories userRepositories) {
        if (!this.isCancelled() && userRepositories != null) {
            gitHubSyncService.updateRepositoriesList(userRepositories);
        }
    }
}
