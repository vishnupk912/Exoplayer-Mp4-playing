package com.mastervidya.mastervidya.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.mastervidya.mastervidya.ui.Login;

import java.util.HashMap;


public class SessionHandler {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "whatswork";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_LOGIN = "login";

    public static final String UNIQUE_KEY="UNIQUE_KEY";
    public static final String AVATAR_KEY="AVATAR_KEY";



//
//
//    public HashMap<String, String> getprofilessession()
//    {
//        HashMap<String, String> user=new HashMap<String, String>();
//        user.put(KEY_PROFILE,pref.getString(KEY_PROFILE,null));
//        return user;
//    }
//
//    public void createprofilesesession(String s)
//    {
//        editor.putString(KEY_PROFILE,s);
//        editor.commit();
//    }



    public void setuniquekey(String key) {
        editor.putString(UNIQUE_KEY, key);
        editor.commit();
    }


    public String getuniquekey() {
        return pref.getString(UNIQUE_KEY, null);
    }



    public void setavatarid(String id) {
        editor.putString(AVATAR_KEY, id);
        editor.commit();
    }


    public String getavatarid()
    {
        return pref.getString(AVATAR_KEY, null);
    }






    public SessionHandler(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }




    public void createLoginSession(String s)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_LOGIN, s);
        editor.commit();

    }

    public HashMap<String, String> getLoginSession()
    {
        HashMap<String, String> user=new HashMap<String, String>();
        user.put(KEY_LOGIN,pref.getString(KEY_LOGIN,null));
        return user;
    }






    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void logoutUser()
    {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}
