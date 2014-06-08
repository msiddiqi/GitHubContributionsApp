package com.githubcardandroidapp.app.Serialization;

import com.githubcardandroidapp.app.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubUserRepositories;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Muhammad on 6/4/2014.
 */
public interface PersistenceHandler {

    void serializeProfile(GitHubProfileDetails gitHubProfileDetails) throws IOException;
    void serializeRepositories(GitHubUserRepositories gitHubUserRepositories) throws IOException;

    GitHubProfileDetails readProfileFromPersistence() throws IOException;
    GitHubUserRepositories readUserRepositoriesFromPersistence() throws IOException;

    boolean isPersistedDataCurrent() throws FileNotFoundException;
}