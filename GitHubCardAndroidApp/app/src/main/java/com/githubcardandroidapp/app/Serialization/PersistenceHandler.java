package com.githubcardandroidapp.app.Serialization;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Muhammad on 6/4/2014.
 */
public interface PersistenceHandler {

    void serializeProfile(String userName, GitHubProfileDetails gitHubProfileDetails) throws IOException;
    void serializeRepositories(String userName, GitHubUserRepositories gitHubUserRepositories) throws IOException;

    GitHubProfileDetails readProfileFromPersistence(String userName) throws IOException;
    GitHubUserRepositories readUserRepositoriesFromPersistence(String userName) throws IOException;

    boolean isPersistedDataCurrent(String userName);
    boolean isPersistedDataAvailable(String userName);
}