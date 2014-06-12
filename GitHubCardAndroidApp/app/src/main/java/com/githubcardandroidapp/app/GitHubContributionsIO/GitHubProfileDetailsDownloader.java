package com.githubcardandroidapp.app.GitHubContributionsIO;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;

public interface GitHubProfileDetailsDownloader {
    GitHubProfileDetails downloadProfileDetails(String login) throws IOException, JSONException;
    List<String> downloadUserRepositories(String login) throws IOException, JSONException;
}
