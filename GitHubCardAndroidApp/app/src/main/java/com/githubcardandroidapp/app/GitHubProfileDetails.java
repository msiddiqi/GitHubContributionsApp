package com.githubcardandroidapp.app;

import android.graphics.Bitmap;
import java.util.Date;

public interface GitHubProfileDetails {
    String getName();
    String getBlog();
    Date getCreatedAt();
    int getNumberOfFollowers();
    Bitmap getAvatarBitmap();
}