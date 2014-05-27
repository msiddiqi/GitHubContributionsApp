package com.githubcardandroidapp.app;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;

public interface GitHubProfileDetailsDownloader {
    GitHubProfileDetails downloadProfileDetails(String login) throws IOException, JSONException;
    List<String> downloadUserRepositories(String login) throws IOException, JSONException;
}
