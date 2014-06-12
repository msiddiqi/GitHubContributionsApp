package com.githubcardandroidapp.app.GitHubContributionsIO.Repositories;

import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubCardActivity;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;
import java.io.IOException;

public class GitHubInternalStorageUserRepositoriesAsyncTask extends GitHubUserRepositoriesAsyncTask {

    public GitHubInternalStorageUserRepositoriesAsyncTask(GitHubCardActivity gitHubCardActivity) {

        super(gitHubCardActivity);
    }

    @Override
    protected GitHubUserRepositories doInBackground(String... params) {

        GitHubUserRepositories gitHubUserRepositories = null;

        try {
            gitHubUserRepositories = GetRepositoriesFromInternalStorage();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        return gitHubUserRepositories;
    }

    private GitHubUserRepositories GetRepositoriesFromInternalStorage() throws IOException {

        PersistenceHandler persistenceHandler = new PersistenceHandlerImpl(this.gitHubCardActivity);
        GitHubUserRepositories userRepositories = persistenceHandler.readUserRepositoriesFromPersistence();
        return userRepositories;
    }
}
