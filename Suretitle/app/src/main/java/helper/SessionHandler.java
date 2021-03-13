package helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.suretitle.www.Login;

import java.util.HashMap;


public class SessionHandler {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "suretitle";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_TOKEN = "KEY_TOKEN";
    public static final String KEY_COUNT = "KEY_COUNT";
    public static final String KEY_BACK = "KEY_BACK";



    public HashMap<String,String> gettoken()
    {
        HashMap<String,String>user=new HashMap<String, String>();
        user.put(KEY_TOKEN,pref.getString(KEY_TOKEN,null));
        return user;
    }


    public void createtoken(String token)
    {
        // editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);
        editor.commit();

    }

    public HashMap<String,String> getbackpayment()
    {
        HashMap<String,String>user=new HashMap<String, String>();
        user.put(KEY_BACK,pref.getString(KEY_BACK,null));
        return user;
    }


    public void createbackpayment(String token)
    {
        editor.putString(KEY_BACK, token);
        editor.commit();

    }


    public HashMap<String,String> getcount()
    {
        HashMap<String,String>user=new HashMap<String, String>();
        user.put(KEY_COUNT,pref.getString(KEY_COUNT,null));
        return user;
    }


    public void createcount(String count)
    {
        // editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_COUNT, count);
        editor.commit();

    }


    public void createLoginSession(String s)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_LOGIN, s);
        editor.commit();

    }

    public HashMap<String,String> getLoginSession()
    {
        HashMap<String,String>user=new HashMap<String, String>();
        user.put(KEY_LOGIN,pref.getString(KEY_LOGIN,null));
        return user;
    }


    public SessionHandler(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
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
