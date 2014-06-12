package com.githubcardandroidapp.app.GitHubContributionsIO;

import android.util.Log;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetailsImpl;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class GitHubProfileDetailsDownloaderBase implements GitHubProfileDetailsDownloader {

    protected final String UriText = "https://api.github.com/users/";

    @Override
    public GitHubProfileDetails downloadProfileDetails(String login) throws IOException, JSONException {

        String result = downloadProfileDetailsCore(getUriForProfileDetails(login));
        GitHubProfileDetails profileDetails = ParseJSONToProfileDetails(result);
        return profileDetails;
    }

    @Override
    public List<String> downloadUserRepositories(String login) throws IOException, JSONException {

        String repositoriesUri = getUriForUserRepositories(login);
        return downloadUserRepositoriesCore(repositoriesUri);
    }

    protected abstract List<String> downloadUserRepositoriesCore(String repositoriesUri) throws IOException, JSONException;

    protected abstract String downloadProfileDetailsCore(String login) throws IOException, JSONException;

    protected static GitHubProfileDetails ParseJSONToProfileDetails(String result) throws JSONException
    {
        GitHubProfileDetails profileDetails = null;

        try {
            JSONObject json = new JSONObject(result);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

            String name = json.getString("name");
            String blog = json.getString("blog");
            String login = json.getString("login");
            Date createdAt = formatter.parse(json.getString("created_at"));
            int numFollowers = json.getInt("followers");

            String avatarUrlText = json.getString("avatar_url");

            profileDetails =
                    new GitHubProfileDetailsImpl(login, name, avatarUrlText, blog, createdAt, numFollowers );
        }
        catch (ParseException exception) {
            Log.i("JSON Exception", "Parsing exception");
        }

        return profileDetails;
    }

    protected String getUriForProfileDetails(String login)
    {
        return String.format("%s%s", UriText, login);
    }

    protected String getUriForUserRepositories(String login)
    {
        return String.format("%s/repos", getUriForProfileDetails(login));
    }
}