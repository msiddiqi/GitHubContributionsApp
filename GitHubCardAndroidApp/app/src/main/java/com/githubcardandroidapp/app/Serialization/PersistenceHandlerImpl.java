package com.githubcardandroidapp.app.Serialization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetails;
import com.githubcardandroidapp.app.BusinessObjects.GitHubProfileDetailsImpl;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositories;
import com.githubcardandroidapp.app.BusinessObjects.GitHubUserRepositoriesImpl;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by Muhammad on 6/5/2014.
 */
public class PersistenceHandlerImpl implements PersistenceHandler {

    String ProfileFileName = "profile";
    String RepositoriesFileName = "repositories";
    String AvatarBitmapFileName = "avatar.bmp";
    Context context;

    public PersistenceHandlerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void serializeProfile(String userName, GitHubProfileDetails gitHubProfileDetails) throws IOException {

        if (gitHubProfileDetails instanceof GitHubProfileDetailsImpl) {
            GitHubProfileDetailsImpl.GitHubProfileMemento memento = ((GitHubProfileDetailsImpl) gitHubProfileDetails).GetProfileMemento();

            String jsonProfile = new Gson().toJson(memento);
            saveObjectToFile(getUserProfileFileName(userName), jsonProfile.getBytes());

            // save avatar bitmap
            FileOutputStream outStream = new FileOutputStream(this.context.getFilesDir() + "/" + getAvatarProfileFileName(userName));
            gitHubProfileDetails.getAvatarBitmap().compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        }

        //String jsonProfile = new Gson().toJson((GitHubProfileDetailsImpl)gitHubProfileDetails);
        //saveObjectToFile(ProfileFileName, jsonProfile.getBytes());
    }

    @Override
    public void serializeRepositories(String userName, GitHubUserRepositories gitHubUserRepositories) throws IOException {

        String jsonRepositories = new Gson().toJson(gitHubUserRepositories);
        String userRepositoriesFileName = getUserRepositoriesFileName(userName);

        saveObjectToFile(userRepositoriesFileName, jsonRepositories.getBytes());
    }

    private void saveObjectToFile(String fileName, byte[] objectData) throws IOException {

        OutputStream outputStream = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(objectData);
        outputStream.close();
    }

    private String getObjectDataFromFile(String fileName) throws IOException{

        InputStream inputStream = this.context.openFileInput(fileName);

        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        inputStream.close();
        isr.close();
        bufferedReader.close();

        return sb.toString();
    }

    @Override
    public GitHubProfileDetails readProfileFromPersistence(String userName) throws IOException {

        String userFileName = getUserProfileFileName(userName);
        String profileData = getObjectDataFromFile(userFileName);
        GitHubProfileDetailsImpl.GitHubProfileMemento memento = new Gson().fromJson(profileData, GitHubProfileDetailsImpl.GitHubProfileMemento.class);

        FileInputStream inputStream = new FileInputStream(this.context.getFilesDir() + "/" + getAvatarProfileFileName(userName));
        Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);

        GitHubProfileDetails gitHubProfileDetails = new GitHubProfileDetailsImpl(memento, avatarBitmap);

        return gitHubProfileDetails;
    }

    private String getUserProfileFileName(String userName) {
        String userProfileFileName = String.format("%s_%s", this.ProfileFileName, userName);
        return userProfileFileName;
    }

    private String getAvatarProfileFileName(String userName) {
        String userAvatarFileName = String.format("%s_%s", this.AvatarBitmapFileName, userName);
        return userAvatarFileName;
    }

    private String getUserRepositoriesFileName(String userName) {
        String userRepositoriesFileName = String.format("%s_%s", this.RepositoriesFileName, userName);
        return userRepositoriesFileName;
    }

    @Override
    public GitHubUserRepositories readUserRepositoriesFromPersistence(String userName) throws IOException {

        String repositoriesData = getObjectDataFromFile(getUserRepositoriesFileName(userName));

        GitHubUserRepositories gitHubUserRepositories = new Gson().fromJson(repositoriesData, GitHubUserRepositoriesImpl.class);
        return gitHubUserRepositories;
    }

    @Override
    public boolean isPersistedDataCurrent(String userName) {

        boolean isDataCurrent = false;

        String fileName = getUserProfileFileName(userName);
        File file = getLastPersistedFile(fileName);

        if (file != null && file.exists() &&
                (Calendar.getInstance().getTime().getTime() - file.lastModified()) < 10800000) {
            isDataCurrent = true;
        }

        return isDataCurrent;
    }

    @Override
    public boolean isPersistedDataAvailable(String userName) {

        boolean isDataAvailable = false;
        File file = getLastPersistedFile(userName);

        if (file != null && file.exists()) {
            isDataAvailable = true;
        }

        return isDataAvailable;
    }

    private File getLastPersistedFile(String fileName) {
        File file = null;
        File[] filesInInternalStorage = this.context.getFilesDir().listFiles();

        for (int i = 0; i < filesInInternalStorage.length; i++) {
            if (filesInInternalStorage[i].getName().equals(fileName)) {
                file = filesInInternalStorage[i];
                break;
            }
        }

        return file;
    }
}