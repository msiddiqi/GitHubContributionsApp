package com.githubcardandroidapp.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubContributionsIO.Profile.GitHubActivityInternalStorageAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Profile.GitHubActivityOnlineUserProfileAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Profile.GitHubProfileAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Repositories.GitHubActivityOnlineUserRepositoriesAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Repositories.GitHubInternalStorageUserRepositoriesAsyncTask;
import com.githubcardandroidapp.app.GitHubContributionsIO.Repositories.GitHubUserRepositoriesAsyncTask;
import com.githubcardandroidapp.app.Serialization.PersistenceHandler;
import com.githubcardandroidapp.app.Serialization.PersistenceHandlerImpl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GitHubCardActivity extends Activity {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    PersistenceHandler persistenceHandler;

    boolean isLoaded = false;

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
        loadUserDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_card);

        this.persistenceHandler = new PersistenceHandlerImpl(this);

        scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                loadUserDetails();
            }
        }, 0, 3, TimeUnit.HOURS);
    }

    private void loadUserDetails() {

        String userName = getUserNameFromPreference();

        boolean isPersistedDataCurrent = this.persistenceHandler.isPersistedDataCurrent();

        GitHubUserRepositoriesAsyncTask gitHubUserRepositoriesAsyncTask =
                isPersistedDataCurrent && !isLoaded?
                        new GitHubInternalStorageUserRepositoriesAsyncTask(this) :
                        new GitHubActivityOnlineUserRepositoriesAsyncTask(this);

        GitHubProfileAsyncTask gitHubProfileAsyncTask =
                isPersistedDataCurrent && !isLoaded?
                        new GitHubActivityInternalStorageAsyncTask(this) :
                        new GitHubActivityOnlineUserProfileAsyncTask(this);

        gitHubUserRepositoriesAsyncTask.execute(userName);
        gitHubProfileAsyncTask.execute(userName);

        isLoaded = true;
    }

    private String getUserNameFromPreference() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String retUserName = sharedPrefs.getString("pref_loginName", "msiddiqi");
        return retUserName;
    }
}