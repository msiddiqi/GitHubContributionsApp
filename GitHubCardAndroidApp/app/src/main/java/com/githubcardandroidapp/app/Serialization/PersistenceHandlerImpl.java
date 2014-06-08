package com.githubcardandroidapp.app.Serialization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.githubcardandroidapp.app.GitHubProfileDetails;
import com.githubcardandroidapp.app.GitHubProfileDetailsImpl;
import com.githubcardandroidapp.app.GitHubUserRepositories;
import com.githubcardandroidapp.app.GitHubUserRepositoriesImpl;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

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
    public void serializeProfile(GitHubProfileDetails gitHubProfileDetails) throws IOException {

        if (gitHubProfileDetails instanceof GitHubProfileDetailsImpl) {
            GitHubProfileDetailsImpl.GitHubProfileMemento memento = ((GitHubProfileDetailsImpl) gitHubProfileDetails).GetProfileMemento();

            String jsonProfile = new Gson().toJson(memento);
            saveObjectToFile(ProfileFileName, jsonProfile.getBytes());

            // save avatar bitmap
            FileOutputStream outStream = new FileOutputStream(this.context.getFilesDir() + "/" + AvatarBitmapFileName);
            gitHubProfileDetails.getAvatarBitmap().compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        }

        //String jsonProfile = new Gson().toJson((GitHubProfileDetailsImpl)gitHubProfileDetails);
        //saveObjectToFile(ProfileFileName, jsonProfile.getBytes());
    }

    @Override
    public void serializeRepositories(GitHubUserRepositories gitHubUserRepositories) throws IOException {

        String jsonProfile = new Gson().toJson(gitHubUserRepositories);
        saveObjectToFile(RepositoriesFileName, jsonProfile.getBytes());
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

        return sb.toString();
    }

    @Override
    public GitHubProfileDetails readProfileFromPersistence() throws IOException {

        String profileData = getObjectDataFromFile(this.ProfileFileName);
        GitHubProfileDetailsImpl.GitHubProfileMemento memento = new Gson().fromJson(profileData, GitHubProfileDetailsImpl.GitHubProfileMemento.class);

        FileInputStream inputStream = new FileInputStream(this.context.getFilesDir() + "/" + this.AvatarBitmapFileName);
        Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);

        GitHubProfileDetails gitHubProfileDetails = new GitHubProfileDetailsImpl(memento, avatarBitmap);

        return gitHubProfileDetails;
    }

    @Override
    public GitHubUserRepositories readUserRepositoriesFromPersistence() throws IOException {

        String repositoriesData = getObjectDataFromFile(this.RepositoriesFileName);

        GitHubUserRepositories gitHubUserRepositories = new Gson().fromJson(repositoriesData, GitHubUserRepositoriesImpl.class);
        return gitHubUserRepositories;
    }

    @Override
    public boolean isPersistedDataCurrent() throws FileNotFoundException {

        boolean isDataCurrent = false;

        File file = null;
        File[] filesInInternalStorage = this.context.getFilesDir().listFiles();
        for (int i = 0; i < filesInInternalStorage.length; i++){
            if (filesInInternalStorage[i].getName().equals(RepositoriesFileName)) {
                file = filesInInternalStorage[i];
                break;
            }
        }

        if (file != null && file.exists() &&
                (Calendar.getInstance().getTime().getTime() - file.lastModified()) < 10800000) {
            isDataCurrent = true;
        }

        return isDataCurrent;
    }
}
