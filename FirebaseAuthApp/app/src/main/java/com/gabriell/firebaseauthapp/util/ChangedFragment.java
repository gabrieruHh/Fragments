package com.gabriell.firebaseauthapp.util;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ChangedFragment {

    public static void loadFragment(AppCompatActivity activity, int frame, Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(frame, fragment)
                .commit();
    }
}
