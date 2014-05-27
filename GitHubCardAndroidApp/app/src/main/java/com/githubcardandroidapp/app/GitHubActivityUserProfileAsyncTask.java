package com.githubcardandroidapp.app;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import java.io.IOException;

public class GitHubActivityUserProfileAsyncTask extends AsyncTask<String, GitHubProfileDetails, GitHubProfileDetails> {

    GitHubCardActivity gitHubCardActivity;

    public GitHubActivityUserProfileAsyncTask(GitHubCardActivity gitHubCardActivity) {
        this.gitHubCardActivity = gitHubCardActivity;
    }

    @Override
    protected GitHubProfileDetails doInBackground(String... params) {

        GitHubProfileDetails gitHubProfileDetails = null;

        try {
            gitHubProfileDetails =
                    new HttpClientProfileDetailsDownloader().downloadProfileDetails(params[0]);
        } catch (IOException e) {
            Log.i("IO exception : profile", e.getMessage());
        } catch (JSONException e) {
            Log.i("JSON exception : profile", e.getMessage());
        }

        return gitHubProfileDetails;
    }

    @Override
    protected void onPostExecute(GitHubProfileDetails profileDetails) {
        if (!this.isCancelled()) {
            this.gitHubCardActivity.updateUserProfile(profileDetails);
        }
    }
}