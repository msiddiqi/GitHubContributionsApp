package com.githubcardandroidapp.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientProfileDetailsDownloader extends GitHubProfileDetailsDownloaderBase {

    @Override
    protected List<String> downloadUserRepositoriesCore(String repositoriesUri) throws IOException, JSONException {

        List<String> resultList = new ArrayList<String>();
        HttpResponse httpResponse = getHttpResponse(repositoriesUri);
        String result = EntityUtils.toString(httpResponse.getEntity());

        JSONArray resultArray = new JSONArray(result);

        for (int i = 0; i < resultArray.length(); i++) {
            String repositoryName = resultArray.getJSONObject(i).getString("name");
            resultList.add(repositoryName);
        }

        return resultList;
    }

    protected String downloadProfileDetailsCore(String uriProfile) throws IOException, JSONException
    {
        HttpResponse response = getHttpResponse(uriProfile);
        String result = EntityUtils.toString(response.getEntity());

        return result;
    }

    private HttpResponse getHttpResponse(String uri) throws IOException
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);

        return httpClient.execute(httpGet);
    }
}
