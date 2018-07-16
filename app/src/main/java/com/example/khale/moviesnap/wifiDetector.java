package com.example.khale.moviesnap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by khale on 11/16/2016.
 */

public class wifiDetector {

    private Context context;

    public wifiDetector(Context context)
    {
        this.context=context;
    }
    //chechk if there connection or not .....
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo nInfo = connectivity.getActiveNetworkInfo();

            if (nInfo != null && nInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

}
