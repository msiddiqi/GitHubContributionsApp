package com.githubcardandroidapp.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubCardActivityReceiver;
import com.githubcardandroidapp.app.GitHubContributionsIO.Services.GitHubSyncService;

import java.text.SimpleDateFormat;
import java.util.List;

public class GitHubCardActivity extends Activity {

    private boolean isDataLoaded = false;

    public final static String ProfileExtra = "profile";
    public final static String RepositoriesExtra = "repositories";
    public final static String IsConnectedExtra = "isConnected";

    private final String ConnectedToastText = "Connected to network";
    private final String DisconnectedToastText = "Disconnected from network";

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setConnected(boolean isConnected) {

        if(!isDataLoaded) {
            if (isConnected) {
                setContentView(R.layout.activity_git_hub_card);
                isDataLoaded = true;
            } else {
                setContentView(R.layout.not_connected_card);
            }
        }
        else {
            showToast(isConnected);
        }
    }

    public void updateRepositoriesList(GitHubUserRepositories userRepositories) {

        List<String> userRepositoriesList = userRepositories.getRepositories();

        ListView listView = (ListView) findViewById(R.id.listViewRepositories);

        ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_list_item_1,
                            userRepositoriesList.toArray(new String[userRepositoriesList.size()]));

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

    private void openSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        generateIntentForLoadingInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading_card);
        this.registerReceiver(new GitHubCardActivityReceiver(this), new IntentFilter(GitHubCardActivityReceiver.UpdateConnectedStatusAction));
        this.registerReceiver(new GitHubCardActivityReceiver(this), new IntentFilter(GitHubCardActivityReceiver.UpdateUserProfileAction));
        this.registerReceiver(new GitHubCardActivityReceiver(this), new IntentFilter(GitHubCardActivityReceiver.UpdateRepositoriesAction));

        generateIntentForLoadingInfo();
    }

    private void requestGetDataFromService() {
        Intent intent = new Intent(GitHubSyncService.GetDataReceiverAction);
        this.startService(intent);
    }
    private void generateIntentForLoadingInfo() {
        Intent intent = new Intent("GitHubContributionsIO.GitHubSyncService");
        this.startService(intent);
    }

    private void showToast(boolean isConnectedNow)
    {
        String message = isConnectedNow ? ConnectedToastText : DisconnectedToastText;

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

        toast.setDuration(Toast.LENGTH_LONG);

        toast.show();
    }
}