package com.githubcardandroidapp.app;

import android.os.AsyncTask;
import android.util.Log;

import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

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
                gitHubCardActivity.updateRepositoriesList(userRepositories);
        }
    }

    @Override
    protected GitHubUserRepositories doInBackground(String... params) {

        GitHubUserRepositories userRepositories = null;

        boolean isFileContentsCurrent = false;
        PersistenceHandler persistenceHandler = new PersistenceHandlerImpl(this.gitHubCardActivity);

        try {
            if (persistenceHandler.isPersistedDataCurrent()) {
                userRepositories = GetRepositoriesFromInternalStorage(persistenceHandler);
                isFileContentsCurrent = true;
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
            isFileContentsCurrent = false;
        }

        if (!isFileContentsCurrent) {
            userRepositories = GetOnlineRepositories(params[0]);
            this.SaveUserRepositories(persistenceHandler, userRepositories);
        }

        return userRepositories;
    }

    private GitHubUserRepositories GetRepositoriesFromInternalStorage(PersistenceHandler persistenceHandler) throws IOException {

        GitHubUserRepositories userRepositories = persistenceHandler.readUserRepositoriesFromPersistence();
        return userRepositories;
    }

    private GitHubUserRepositories GetOnlineRepositories(String login) {

        GitHubUserRepositories userRepositories = null;
        try {
            List<String> repositories =
                    new HttpClientProfileDetailsDownloader().downloadUserRepositories(login);

            userRepositories = new GitHubUserRepositoriesImpl(repositories);
        } catch (IOException
                e) {
            Log.i("IO exception : repositories", e.getMessage());
        } catch (JSONException e) {
            Log.i("JSON exception : repositories", e.getMessage());
        }

        return userRepositories;
    }

    private void SaveUserRepositories(PersistenceHandler persistenceHandler, GitHubUserRepositories userRepositories){

        try {
            persistenceHandler.serializeRepositories(userRepositories);
        }
        catch (IOException exception) {
            Log.i("IO exception : repositories", exception.getMessage());
        }
        //new AsyncTask<GitHubUserRepositories, Void, Void>() {

        //    @Override
       //     protected Void doInBackground(GitHubUserRepositories... params) {
       //         try {
        //            persistenceHandler.serializeRepositories(params[0]);
         //       } catch (Exception ex) {
         //           Log.i("FileSaveError", "Cannot save repositories data");
         //       }

         //       return null;
          //  }
        //}.execute(userRepositories);
    }
}
