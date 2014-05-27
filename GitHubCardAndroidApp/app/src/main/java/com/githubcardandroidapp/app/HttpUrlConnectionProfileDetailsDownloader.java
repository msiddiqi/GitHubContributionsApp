package com.githubcardandroidapp.app;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Muhammad on 5/17/2014.
 */
public class HttpUrlConnectionProfileDetailsDownloader extends GitHubProfileDetailsDownloaderBase {
    @Override
    protected List<String> downloadUserRepositoriesCore(String repositoriesUri) {
        return null;
    }

    @Override
    protected String downloadProfileDetailsCore(String login) throws IOException, JSONException {

        String uriTextWithLogin = getUriForProfileDetails(login); //String.format("%s%s", UriText, login);
        String ret = null;

        try {

            URL url = new URL(uriTextWithLogin);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            InputStream inputStream = connection.getInputStream();

            try {
                ret = convertStreamToString(inputStream);
                //profileDetails = ParseJSONToProfileDetails(content);
            }
            finally {
                inputStream.close();
            }
        }
        catch (MalformedURLException exception) {
        }

        return ret;
    }

    private static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
