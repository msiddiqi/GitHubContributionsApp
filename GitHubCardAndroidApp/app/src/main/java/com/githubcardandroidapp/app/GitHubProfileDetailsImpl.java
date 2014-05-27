package com.githubcardandroidapp.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class GitHubProfileDetailsImpl implements GitHubProfileDetails
{
    // Private Fields
    String loginName;
    String name;
    String avatarURL;
    String blog;
    Date createdAt;
    int numberOfFollowers;
    Bitmap avatarBitmap;

    // Constructors
    public GitHubProfileDetailsImpl(String loginName, String name, String avatarURL, String blog, Date createdAt, int numberOfFollowers){
        this.loginName = loginName;
        this.name = name;
        this.avatarURL = avatarURL;
        this.blog = blog;
        this.numberOfFollowers = numberOfFollowers;
        this.createdAt = createdAt;

        try {
            this.avatarBitmap =
                    BitmapFactory.decodeStream(new URL(avatarURL).openConnection().getInputStream());
        }
        catch (MalformedURLException exception){
            Log.e("BitmapLoad",exception.getMessage());
        }
        catch (IOException exception) {
            Log.e("BitmapLoad", exception.getMessage());
        }
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getBlog() {
        return blog;
    }

    @Override
    public Date getCreatedAt() { return createdAt; }

    @Override
    public int getNumberOfFollowers() { return numberOfFollowers; }

    @Override
    public Bitmap getAvatarBitmap() { return avatarBitmap;}
}