package com.example.ReceipeSearchPage.Helper;

/**
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ReceipeSearchPage.Model.ProductModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


//here for this class we are using a singleton pattern

public class SharedPrefManager {

//the constants
private static final String SHARED_PREF_NAME = "ReceipePage";
private static final String KEY_IMAGE = "keyimage";
private static final String KEY_TITLE = "keytitle";
private static final String KEY_DESC = "keydesc";
private static final String KEY_LINK = "keylink";



private static SharedPrefManager mInstance;
private static Context mCtx;

private SharedPrefManager(Context context) {
        mCtx = context;
}

public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
                mInstance = new SharedPrefManager(context);
        }
        return mInstance;
}

//method to let the user login
//this method will store the user data in shared preferences
public void userLogin(String arrayList) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        editor.putString(KEY_IMAGE, model.getThumbnail());
//        editor.putString(KEY_TITLE, model.getTitle());
//        editor.putString(KEY_DESC, model.getIngredients());
//        editor.putString(KEY_LINK, model.getHref());
        editor.putString("set",arrayList);

        editor.apply();
}


//this method will give the logged in user
public String getData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String fetch = sharedPreferences.getString("set", null);
//        List<String> list = new ArrayList<String>(fetch);
//        List<String> list1;

//        for(int i = 0 ; i < list.size() ; i++){
//                Log.d("fetching values", "fetch value " + list.get(i));
//        }


        return  fetch ;

}

//this method will logout the user
public void clearData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
//                mCtx.startActivity(new Intent(mCtx, LetsPlayActivity.class));
}

}

