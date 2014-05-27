package com.githubcardandroidapp.app;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GitHubCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_card);

        final String userName = "msiddiqi";

        new GitHubActivityUserRepositoriesAsyncTask(this).execute(userName);
        new GitHubActivityUserProfileAsyncTask(this).execute(userName);
    }

    public void updateRepositoriesList(List<String> userRepositories) {

        ListView listView = (ListView)findViewById(R.id.listViewRepositories);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        userRepositories.toArray(new String[userRepositories.size()]));

        listView.setAdapter(adapter);
    }

    public void updateUserProfile(GitHubProfileDetails profileDetails) {

        ImageView imgProfilePic = (ImageView)findViewById(R.id.imgProfilePic);
        TextView txtName = (TextView)findViewById(R.id.txtName);
        TextView txtCreatedAt = (TextView)findViewById(R.id.txtCreatedAt);
        TextView txtNumFollowers = (TextView)findViewById(R.id.txtNumFollowers);
        TextView txtBlog = (TextView)findViewById(R.id.txtBlog);

        imgProfilePic.setImageBitmap(profileDetails.getAvatarBitmap());

        txtName.setText(profileDetails.getName());
        txtCreatedAt.setText(
                String.format("Member Since %s", new SimpleDateFormat("MMM dd, yyyy").format(profileDetails.getCreatedAt())));

        txtNumFollowers.setText(
                String.format("%d Followers", profileDetails.getNumberOfFollowers()));

        txtBlog.setText(profileDetails.getBlog());
    }
}