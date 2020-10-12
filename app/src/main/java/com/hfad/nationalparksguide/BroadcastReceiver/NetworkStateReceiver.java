package com.hfad.nationalparksguide.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkStateReceiver extends BroadcastReceiver {

    private boolean connection = true;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI Connected, MOBILE Connected", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI Connected, MOBILE Not Connected", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI Not Connected, MOBILE Connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI Not Connected, MOBILE Not Connected", Toast.LENGTH_SHORT).show();
                connection = false;
            }
        }else {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Network[] networks = connMgr.getAllNetworks();

            StringBuilder sb = new StringBuilder();

            for (int i=0; i < networks.length; i++){
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                String connection = networkInfo.isConnected() ? "Connected" : "Not Connected";
                sb.append(networkInfo.getTypeName() + " is " + connection + ". ");
            }
            if (sb.length() == 0) {
                connection = false;
            }
            Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected() {
        return connection;
    }
}


