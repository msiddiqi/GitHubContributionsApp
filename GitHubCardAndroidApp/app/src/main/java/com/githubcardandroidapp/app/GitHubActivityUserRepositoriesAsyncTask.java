package com.githubcardandroidapp.app;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;

public class GitHubActivityUserRepositoriesAsyncTask extends AsyncTask<String, GitHubUserRepositories, GitHubUserRepositories> {

    GitHubCardActivity gitHubCardActivity;

    public GitHubActivityUserRepositoriesAsyncTask(GitHubCardActivity gitHubCardActivity) {
        this.gitHubCardActivity = gitHubCardActivity;
    }
        @Override
    protected void onPostExecute(GitHubUserRepositories userRepositories) {
        if (!this.isCancelled()) {
                gitHubCardActivity.updateRepositoriesList(userRepositories.getRepositories());
        }
    }

    @Override
    protected GitHubUserRepositories doInBackground(String... params) {
        GitHubUserRepositories userRepositories = null;

        try {
            List<String> repositories =
                    new HttpClientProfileDetailsDownloader().downloadUserRepositories(params[0]);

            userRepositories = new GitHubUserRepositoriesImpl(repositories);
        } catch (IOException e) {
            Log.i("IO exception : repositories", e.getMessage());
        } catch (JSONException e) {
            Log.i("JSON exception : reporities", e.getMessage());
        }

        return userRepositories;
    }
}
