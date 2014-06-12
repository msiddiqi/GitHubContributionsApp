package com.githubcardandroidapp.app.BusinessObjects;

import android.graphics.Bitmap;
import java.util.Date;

public interface GitHubProfileDetails {
    String getName();
    String getBlog();
    Date getCreatedAt();
    int getNumberOfFollowers();
    Bitmap getAvatarBitmap();
}