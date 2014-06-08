package com.githubcardandroidapp.app;

import android.os.AsyncTask;
import android.util.Log;

import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

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
        PersistenceHandler persistenceHandler = new PersistenceHandlerImpl(this.gitHubCardActivity);

        try {

            if (persistenceHandler.isPersistedDataCurrent()) {
                gitHubProfileDetails = persistenceHandler.readProfileFromPersistence();
            }
            else {
                gitHubProfileDetails =
                        new HttpClientProfileDetailsDownloader().downloadProfileDetails(params[0]);

                persistenceHandler.serializeProfile(gitHubProfileDetails);
            }
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