package com.githubcardandroidapp.app.GitHubContributionsIO.Repositories;

import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;
import java.io.IOException;

public class GitHubInternalStorageUserRepositoriesAsyncTask extends GitHubUserRepositoriesAsyncTask {

    public GitHubInternalStorageUserRepositoriesAsyncTask(GitHubSyncService gitHubSyncService) {

        super(gitHubSyncService);
    }

    @Override
    protected GitHubUserRepositories doInBackground(String... params) {

        GitHubUserRepositories gitHubUserRepositories = null;

        try {
            String userName = params[0];
            gitHubUserRepositories = GetRepositoriesFromInternalStorage(userName);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        return gitHubUserRepositories;
    }

    private GitHubUserRepositories GetRepositoriesFromInternalStorage(String userName) throws IOException {

        PersistenceHandler persistenceHandler = new PersistenceHandlerImpl(this.gitHubSyncService.getApplicationContext());
        GitHubUserRepositories userRepositories = persistenceHandler.readUserRepositoriesFromPersistence(userName);
        return userRepositories;
    }
}
