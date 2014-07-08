package com.githubcardandroidapp.app.GitHubContributionsIO.Repositories;

import android.util.Log;

import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositoriesImpl;
import com.githubcardandroidapp.app.GitHubContributionsIO.HttpClientProfileDetailsDownloader;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;

public class GitHubActivityOnlineUserRepositoriesAsyncTask extends GitHubUserRepositoriesAsyncTask {

    public GitHubActivityOnlineUserRepositoriesAsyncTask(GitHubSyncService gitHubSyncService) {

        super(gitHubSyncService);
    }

    @Override
    protected GitHubUserRepositories doInBackground(String... params) {

        GitHubUserRepositories userRepositories = null;

        String userName = params[0];
        try {
            userRepositories = GetOnlineRepositories(userName);
            new PersistenceHandlerImpl(this.gitHubSyncService).serializeRepositories(userName, userRepositories);
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

