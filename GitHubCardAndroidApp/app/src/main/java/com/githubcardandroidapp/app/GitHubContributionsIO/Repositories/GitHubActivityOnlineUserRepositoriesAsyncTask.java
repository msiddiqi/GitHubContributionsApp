package com.githubcardandroidapp.app.GitHubContributionsIO.Repositories;

import android.util.Log;

import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositoriesImpl;
import com.githubcardandroidapp.app.GitHubContributionsIO.HttpClientProfileDetailsDownloader;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;

public class GitHubActivityOnlineUserRepositoriesAsyncTask extends GitHubUserRepositoriesAsyncTask {

    //GitHubCardActivity gitHubCardActivity;

    public GitHubActivityOnlineUserRepositoriesAsyncTask(GitHubCardActivity gitHubCardActivity) {

        super(gitHubCardActivity);
    }

    @Override
    protected GitHubUserRepositories doInBackground(String... params) {

        GitHubUserRepositories userRepositories = null;

        try {
            userRepositories = GetOnlineRepositories(params[0]);
            new PersistenceHandlerImpl(this.gitHubCardActivity).serializeRepositories(userRepositories);
        }
        catch (IOException exception) {
            Log.i("IO exception : repositories", exception.getMessage());
        }
        catch(Exception ex) {
            Log.i("Exception", ex.getMessage());
        }

        return userRepositories;
    }

    private GitHubUserRepositories GetOnlineRepositories(String login) throws IOException{

        GitHubUserRepositories userRepositories = null;
        try {
            List<String> repositories =
                    new HttpClientProfileDetailsDownloader().downloadUserRepositories(login);

            userRepositories = new GitHubUserRepositoriesImpl(repositories);

        } catch (JSONException e) {

            Log.i("JSON exception : repositories", e.getMessage());
        }

        return userRepositories;
    }
}

