package helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetwork {

    private static final String TAG = CheckNetwork.class.getName();

    public static  boolean isInternetAvailable(Context context)
    {
        NetworkInfo info=(NetworkInfo)((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(info==null)
        {
            Log.d(TAG,"No Internet Connection");
            return  false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG,"Connection Available");
                return true;
            }
            else
            {
                Log.d(TAG,"Connection Available");
                return true;
            }
        }
    }
}
