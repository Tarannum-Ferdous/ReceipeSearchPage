package com.example.ReceipeSearchPage.Helper;

import android.content.Context;
import android.net.ConnectivityManager;

public class Constaints {

    public static final String BASE_URL = "http://www.recipepuppy.com/";
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}
